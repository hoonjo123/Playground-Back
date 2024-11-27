package com.swyp.playground.domain.findfriend.service;

import com.swyp.playground.domain.child.repository.ChildRepository;
import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.domain.RecruitmentStatus;
import com.swyp.playground.domain.findfriend.dto.FindFriendListResponse;
import com.swyp.playground.domain.findfriend.dto.FindFriendRegisterRequest;
import com.swyp.playground.domain.findfriend.repository.FindFriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FindFriendService {

    private final FindFriendRepository findFriendRepository;

    private final ChildRepository childRepository;


    //놀이터 친구 모집글 목록 반환
    public List<FindFriendListResponse> getFindFriendList(String playgroundId) {
        List<FindFriend> findFriendList = findFriendRepository.findAllByPlaygroundId(playgroundId);
        return findFriendList.stream()
                .map(f -> FindFriendListResponse.builder()
                        .title(f.getPlaygroundName())
                        .description(f.getDescription())
                        .scheduleTime(formatSchedule(f.getStartTime(), f.getEndTime())) //포맷팅 시간으로 변경하여 추가
                        .recruitmentStatus(f.getStatus().name())
                        .currentCount(f.getCurrentCount())
                        .build())
                .collect(Collectors.toList());
    }

    //LocalDateTime을 포맷팅
    private String formatSchedule(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. EEEE", Locale.KOREAN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN);

        String startDate = startTime.format(dateFormatter);
        String startTimeFormatted = startTime.format(timeFormatter).replace("AM", "오전").replace("PM", "오후");
        String endTimeFormatted = endTime.format(timeFormatter).replace("AM", "오전").replace("PM", "오후");

        return startDate + " " + startTimeFormatted + " ~ " + endTimeFormatted;
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
}
