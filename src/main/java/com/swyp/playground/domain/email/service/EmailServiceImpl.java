package com.swyp.playground.domain.email.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    JavaMailSender emailSender;

    public static String ePw = createTemporaryPassword();


    public static String createTemporaryPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "@$!%*?&";
        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        // 나머지 문자 랜덤 추가
        for (int i = 4; i < 8; i++) { // 총 8자리
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return password.toString();
    }
    private MimeMessage createPasswordResetMessage(String to, String newPassword) throws Exception {
        System.out.println("보내는 대상: " + to);
        System.out.println("새 비밀번호: " + newPassword);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // 받는 사람
        message.setSubject("Playground 비밀번호 초기화 안내"); // 이메일 제목

        String msg = "<div style='margin:100px;'>";
        msg += "<h1>안녕하세요, Playground입니다.</h1>";
        msg += "<p>요청하신 비밀번호가 초기화되었습니다. 아래 임시 비밀번호로 로그인해주세요:</p>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>임시 비밀번호</h3>";
        msg += "<div style='font-size:130%'><strong>" + newPassword + "</strong></div><br/>";
        msg += "</div>";
        msg += "<p>로그인 후 반드시 비밀번호를 변경해주세요.</p>";
        msg += "</div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("playgroundsmtp@gmail.com", "Playground"));

        return message;
    }

    // 비밀번호 초기화 이메일 발송 메서드예욤
    @Override
    public String sendPasswordResetEmail(String email) throws Exception {
        String newPassword = createTemporaryPassword(); // 임시 비밀번호 생성
        MimeMessage message = createPasswordResetMessage(email, newPassword);

        try {
            emailSender.send(message);
        } catch (MailException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("이메일 발송 중 문제가 발생했습니다.");
        }

        return newPassword; // 생성된 임시 비밀번호 반환해줌쓰
    }
}