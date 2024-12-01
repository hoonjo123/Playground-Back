package com.swyp.playground.domain.findfriend.service;

import com.swyp.playground.domain.child.repository.ChildRepository;
import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.domain.RecruitmentStatus;
import com.swyp.playground.domain.findfriend.dto.*;
import com.swyp.playground.domain.findfriend.repository.FindFriendRepository;
import com.swyp.playground.domain.parent.domain.Parent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FindFriendService {

    private final FindFriendRepository findFriendRepository;

    private final ChildRepository childRepository;


    //놀이터 친구 모집글 목록 반환
    public List<FindFriendListResponse> getFindFriendList(String playgroundId) {
        List<FindFriend> findFriendList = findFriendRepository.findAllByPlaygroundId(playgroundId);
        return findFriendList.stream()
                .map(f -> FindFriendListResponse.builder()
                        .id(f.getFindFriendId())
                        .playgroundName(f.getPlaygroundName())
                        .title(f.getTitle())
                        .description(f.getDescription())
                        .scheduleTime(formatSchedule(f.getStartTime(), f.getEndTime())) //포맷팅 시간으로 변경하여 추가
                        .recruitmentStatus(f.getStatus().name())
                        .currentCount(f.getCurrentCount())
                        .build())
                .collect(Collectors.toList());
    }

    //친구 모집글 정보 조회
    public FindFriendInfoResponse getFindFriendInfo(Long findFriendId) {
        FindFriend findFriend = findFriendRepository.findById(findFriendId)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구 모집글을 찾을 수 없습니다."));
        List<Parent> participants = findFriend.getParticipants();

        return FindFriendInfoResponse.builder()
                .playgroundName(findFriend.getPlaygroundName())
                .recruitmentStatus(findFriend.getStatus().name())
                .title(findFriend.getTitle())
                .description(findFriend.getDescription())
                .scheduleTime(formatSchedule(findFriend.getStartTime(), findFriend.getEndTime()))
                .owner(FindFriendOwnerInfoResponse.builder()
                        .nickname(findFriend.getOwner().getNickname())
                        .role(findFriend.getOwner().getRole().name())
                        .address(findFriend.getOwner().getAddress())
                        .build()
                )
                .participants(findFriend.getParticipants().stream()
                        .map(participant -> FindFriendParticipantsListResponse.builder()
                                .nickname(participant.getNickname())
                                .build()
                        )
                        .toList()
                )
                .build();
    }

    //LocalDateTime을 포맷팅
    private String formatSchedule(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd EEEE", Locale.KOREAN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN);

        String startDate = startTime.format(dateFormatter);
        String startTimeFormatted = startTime.format(timeFormatter).replace("AM", "오전").replace("PM", "오후");
        String endTimeFormatted = endTime.format(timeFormatter).replace("AM", "오전").replace("PM", "오후");

        return startDate + " " + startTimeFormatted + "~" + endTimeFormatted;
    }


    //친구 모집글 등록
    public Long registerFindFriend(String playgroundId, FindFriendRegisterRequest findFriendRegisterRequest) {
        LocalDateTime startTime = findFriendRegisterRequest.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(findFriendRegisterRequest.getDuration());

        // FindFriend 엔티티 객체 생성
        FindFriend findFriend = FindFriend.builder()
                .playgroundId(playgroundId)
                .playgroundName(findFriendRegisterRequest.getPlaygroundName())
                .title(findFriendRegisterRequest.getTitle())
                .startTime(startTime)
                .endTime(endTime)
                .currentCount(1)
                .status(RecruitmentStatus.RECRUITING)
                .description(findFriendRegisterRequest.getDescription())
                .build();

        return findFriendRepository.save(findFriend).getFindFriendId();
    }

    //친구 모집글 수정
    public FindFriendInfoResponse modifyFindFriendInfo(String playgroundId, Long findFriendId, FindFriendModifyRequest findFriendModifyRequest) {
        // 모집글 조회
        FindFriend findFriend = findFriendRepository.findById(findFriendId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글을 찾을 수 없습니다."));

        // 놀이터 ID 검증
        if (!findFriend.getPlaygroundId().equals(playgroundId)) {
            throw new IllegalArgumentException("잘못된 놀이터 ID입니다.");
        }

        // 필드 수정 (Dirty Checking)
        if (findFriendModifyRequest.getTitle() != null) {
            findFriend.setTitle(findFriendModifyRequest.getTitle());
        }
        if (findFriendModifyRequest.getDescription() != null) {
            findFriend.setDescription(findFriendModifyRequest.getDescription());
        }
        if (findFriendModifyRequest.getStartTime() != null) {
            LocalDateTime startTime = findFriendModifyRequest.getStartTime();
            findFriend.setStartTime(startTime);
        }
        if(findFriendModifyRequest.getDuration() != null){
            LocalDateTime startTime = findFriend.getStartTime();
            findFriend.setEndTime(startTime.plusMinutes(findFriendModifyRequest.getDuration()));
        }

        return getFindFriendInfo(findFriendId);
    }

    //친구 모집글 삭제
    public void deleteFindFriend(Long findFriendId) {
        findFriendRepository.deleteById(findFriendId);
    }

    //일정 시간마다 친구 모집글 확인(시작 시간에서 10분이 지나면 친구 모집글 상태를 노는중으로 변경
    // 10초마다 상태 업데이트
    @Scheduled(fixedRate = 10000)
    public void updateFindFriendStatus() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 상태가 RECRUITING이고 startTime으로부터 10분이 지난 항목을 COMPLETE로 변경
        List<FindFriend> toComplete = findFriendRepository.findByStatusAndStartTimeBefore(RecruitmentStatus.RECRUITING, now.minusMinutes(10));
        for (FindFriend findFriend : toComplete) {
            findFriend.setStatus(RecruitmentStatus.PLAYING);
        }

        // 상태가 PLAYING이고 endTime이 지난 항목을 CLOSED로 변경
        List<FindFriend> toClose = findFriendRepository.findByStatusAndEndTimeBefore(RecruitmentStatus.PLAYING, now);
        for (FindFriend findFriend : toClose) {
            findFriend.setStatus(RecruitmentStatus.COMPLETE);
        }
    }
}
