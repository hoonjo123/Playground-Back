package com.swyp.playground.domain.email.service;

public interface EmailService {
    String sendPasswordResetEmail(String to)throws Exception;
}