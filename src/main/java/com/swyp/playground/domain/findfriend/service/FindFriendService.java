package com.swyp.playground.domain.findfriend.service;

import com.swyp.playground.domain.findfriend.domain.*;
import com.swyp.playground.domain.findfriend.dto.*;
import com.swyp.playground.domain.findfriend.repository.FindFriendRepository;
import com.swyp.playground.domain.findfriend.repository.ParticipationHistoryRepository;
import com.swyp.playground.domain.findfriend.repository.PlayHistoryRepository;
import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FindFriendService {

    private final FindFriendRepository findFriendRepository;

    private final ParentRepository parentRepository;

    private final PlayHistoryRepository playHistoryRepository;

    private final ParticipationHistoryRepository participationHistoryRepository;


    //놀이터 친구 모집글 목록 반환
    public List<FindFriendListResponse> getFindFriendList(String playgroundId) {
        List<FindFriend> findFriendList = findFriendRepository.findAllByPlaygroundIdOrderByCreatedAtDesc(playgroundId);
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

    //친구 모집글 목록 3개 반환(메인페이지 최신 순)
    public List<FindFriendListResponse> getMainFindFriendList(Pageable top3) {
        List<FindFriend> findFriendList = findFriendRepository.findTop3ByCreatedAtDesc(top3);
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

        Parent owner;
        List<Parent> participants;

        // 모집글 상태가 COMPLETE라면 PlayHistory에서 데이터를 가져옴
        if (findFriend.getStatus() == RecruitmentStatus.COMPLETE) {
            List<PlayHistory> playHistories = playHistoryRepository.findByFindFriend(findFriend);

            // PlayHistory에서 LEADER를 owner로 설정
            owner = playHistories.stream()
                    .filter(playHistory -> playHistory.getUserRole() == UserRole.LEADER)
                    .map(PlayHistory::getParent)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("완료된 모집글에 작성자가 존재하지 않습니다."));

            // PlayHistory에서 PARTICIPANT를 participants로 설정
            participants = playHistories.stream()
                    .filter(playHistory -> playHistory.getUserRole() == UserRole.PARTICIPANT)
                    .map(PlayHistory::getParent)
                    .toList();
        } else {
            // 모집글 상태가 COMPLETE가 아니라면 FindFriend에서 바로 가져옴
            owner = findFriend.getOwner();
            participants = findFriend.getParticipants();
        }

        //모집글 정보 생성
        return FindFriendInfoResponse.builder()
                .findFriendId(findFriendId)
                .playgroundName(findFriend.getPlaygroundName())
                .recruitmentStatus(findFriend.getStatus().name())
                .title(findFriend.getTitle())
                .description(findFriend.getDescription())
                .scheduleTime(formatSchedule(findFriend.getStartTime(), findFriend.getEndTime()))
                .owner(FindFriendOwnerInfoResponse.builder()
                        .nickname(owner.getNickname())
                        .role(owner.getRole().name())
                        .address(owner.getAddress())
                        .profileImg(owner.getProfileImg())
                        .build()
                )
                .participants(participants.stream()
                        .map(participant -> FindFriendParticipantsListResponse.builder()
                                .nickname(participant.getNickname())
                                .profileImg(participant.getProfileImg())
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
    public Long registerFindFriend(String playgroundId, String email, FindFriendRegisterRequest findFriendRegisterRequest) {
        LocalDateTime startTime = findFriendRegisterRequest.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(findFriendRegisterRequest.getDuration());
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        Optional<FindFriend> ownerParent = findFriendRepository.findByOwner_ParentId(parent.getParentId());
        if(ownerParent.isPresent())
            throw new IllegalArgumentException("이미 만든 친구 모집글이 있습니다.");

        if(parent.getFindFriend() != null){
            throw new IllegalArgumentException("이미 참가한 방이 있습니다.");
        }

        // FindFriend 엔티티 객체 생성
        FindFriend findFriend = FindFriend.builder()
                .playgroundId(playgroundId)
                .playgroundName(findFriendRegisterRequest.getPlaygroundName())
                .nickname(parent.getNickname())
                .title(findFriendRegisterRequest.getTitle())
                .startTime(startTime)
                .endTime(endTime)
                .createdAt(LocalDateTime.now())
                .currentCount(1)
                .status(RecruitmentStatus.RECRUITING)
                .description(findFriendRegisterRequest.getDescription())
                .owner(parent)
                .build();

        return findFriendRepository.save(findFriend).getFindFriendId();
    }

    //친구 모집글 수정
    public FindFriendInfoResponse modifyFindFriendInfo(String playgroundId, Long findFriendId, String email, FindFriendModifyRequest findFriendModifyRequest) {

        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        FindFriend findFriend = findFriendRepository.findById(findFriendId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글을 찾을 수 없습니다."));


        //친구 모집글 수정 권한 검증
        if(findFriend.getOwner().getParentId() != parent.getParentId()){
            throw new IllegalArgumentException("해당 글을 수정할 권한이 없습니다.");
        }

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
    public void deleteFindFriend(Long findFriendId, String email) {

        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        FindFriend findFriend = findFriendRepository.findById(findFriendId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글을 찾을 수 없습니다."));

        //친구 모집글 삭제 권한 검증
        if(findFriend.getOwner().getParentId() != parent.getParentId()){
            throw new IllegalArgumentException("해당 글을 삭제할 권한이 없습니다.");
        }
        findFriendRepository.deleteById(findFriendId);
    }

    // 10초마다 상태 업데이트
    @Scheduled(fixedRate = 10000)
    public void updateFindFriendStatus() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 상태가 RECRUITING이고 startTime으로부터 10분이 지난 항목을 PLAYING으로 변경
        List<FindFriend> toComplete = findFriendRepository.findByStatusAndStartTimeBefore(RecruitmentStatus.RECRUITING, now.minusMinutes(10));
        for (FindFriend findFriend : toComplete) {
            findFriend.setStatus(RecruitmentStatus.PLAYING);
        }

        // 상태가 PLAYING이고 endTime이 지난 항목을 CLOSED로 변경 및 참여 해제
        List<FindFriend> toClose = findFriendRepository.findByStatusAndEndTimeBefore(RecruitmentStatus.PLAYING, now);
        for (FindFriend findFriend : toClose) {

            findFriend.setStatus(RecruitmentStatus.COMPLETE);

            //프록시 초기화 코드
            findFriend.getOwner().getAddress();

            PlayHistory playHistoryLeader = PlayHistory.builder()
                    .findFriend(findFriend)
                    .parent(findFriend.getOwner())
                    .userRole(UserRole.LEADER)
                    .build();
            playHistoryRepository.save(playHistoryLeader);
            List<Parent> participants = findFriend.getParticipants();
            for (Parent parent : participants) {

                //누가 어떤 FindFriend에서 놀았는지 기록
                PlayHistory playHistoryParticipants = PlayHistory.builder()
                        .findFriend(findFriend)
                        .parent(parent)
                        .userRole(UserRole.PARTICIPANT)
                        .build();
                playHistoryRepository.save(playHistoryParticipants);

                //Parent와 FindFriend 관계 해제
                parent.setFindFriend(null);
            }
            findFriend.setOwner(null);
            findFriend.getParticipants().clear();
        }
    }

    //친구 모집글 참가 및 취소
    public void actionFindFriend(String playgroundId, Long findFriendId, String email, String action) {

        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        FindFriend findFriend = findFriendRepository.findById(findFriendId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글을 찾을 수 없습니다."));

        if (parent.getMannerTemp().divide(BigDecimal.valueOf(parent.getMannerTempCount()), 1, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(40.0)) < 0) {
            log.info("40도 이하");
            throw new IllegalArgumentException("매너온도가 40.0도 미만인 사용자는 참가할 수 없습니다.");
        }

        log.info("안걸림");

        if("participate".equalsIgnoreCase(action.trim())){
            Optional<FindFriend> ownerParent = findFriendRepository.findByOwner_ParentId(parent.getParentId());
            if(ownerParent.isPresent())
                throw new IllegalArgumentException("이미 만든 친구 모집글이 있습니다.");

            if(parent.getFindFriend() != null){
                throw new IllegalArgumentException("이미 참가한 방이 있습니다.");
            }

            // 참가 기록 조회
            Optional<ParticipationHistory> existingHistory = participationHistoryRepository.findByParentAndFindFriend(parent, findFriend);
            if (existingHistory.isPresent()) {
                ParticipationHistory participationHistory = existingHistory.get();

                if (participationHistory.getParticipationCount() == 2) {
                    throw new IllegalArgumentException("같은 방에 3번 이상 참가 신청은 불가능합니다.");
                }
                //참가신청 기록 +1
                participationHistory.setParticipationCount(participationHistory.getParticipationCount() + 1);
            }else{ //참가 기록이 없다면 만들어 두기
                ParticipationHistory newParticipationHistory = ParticipationHistory.builder()
                        .participationCount(1)
                        .parent(parent)
                        .findFriend(findFriend)
                        .build();
                participationHistoryRepository.save(newParticipationHistory);
            }

            parent.setFindFriend(findFriend);
            findFriend.setCurrentCount(findFriend.getCurrentCount() + 1);
        }

        if("cancel".equalsIgnoreCase(action.trim())){
            parent.setFindFriend(null);
            findFriend.setCurrentCount(findFriend.getCurrentCount() - 1);
        }

    }

    //내가 모집한 글들 조회
    public List<FindFriendListResponse> getMyFindFriendList(String email) {

        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        List<FindFriend> myFindFriendList = findFriendRepository.findByNickname(parent.getNickname());
        // startTime이 느린 순서대로 정렬 (내림차순)
        myFindFriendList.sort(Comparator.comparing(FindFriend::getStartTime).reversed());

        return myFindFriendList.stream()
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

    //나랑 논 친구 목록 조회
    public List<MyRecentFriendResponse> getRecentFriend(String email) {

        // 사용자 정보 조회
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // 해당 Parent가 참여한 모든 PlayHistory 조회
        List<PlayHistory> playHistories = playHistoryRepository.findByParent(parent);

        // 중복을 제거하기 위해 Set을 사용하여 참여한 부모들을 저장
        Set<Parent> allParticipants = new HashSet<>();
        List<FindFriend> findFriends = new ArrayList<>();

        // 각 PlayHistory에서 FindFriend 정보를 가져오고, 그에 참여한 모든 Parent를 찾음
        for (PlayHistory playHistory : playHistories) {
            FindFriend findFriend = playHistory.getFindFriend();

            // 해당 FindFriend가 COMPLETE 상태라면, 그에 참여한 Parent들을 찾음
            if (findFriend.getStatus() == RecruitmentStatus.COMPLETE) {
                // FindFriend에 참여한 Parent들을 PlayHistory에서 찾아서 추가
                List<PlayHistory> relatedPlayHistories = playHistoryRepository.findByFindFriend(findFriend);
                for (PlayHistory relatedHistory : relatedPlayHistories) {
                    Parent participant = relatedHistory.getParent();

                    // 자기 자신은 제외하고, 중복을 피하기 위해 Set에 추가
                    if (!participant.getEmail().equals(email)) {
                        allParticipants.add(participant);
                    }
                }

                // FindFriend를 리스트에 추가
                findFriends.add(findFriend);
            }
        }

        // FindFriend의 startTime 기준으로 내림차순 정렬
        findFriends.sort((f1, f2) -> f2.getStartTime().compareTo(f1.getStartTime()));

        // 중복을 제거한 Parent들을 MyRecentFriendResponse로 변환하여 반환
        return allParticipants.stream()
                .map(p -> new MyRecentFriendResponse(
                        p.getNickname(),
                        p.getRole(),
                        p.getAddress(),
                        p.getIntroduce(),
                        p.getProfileImg()
                ))
                .collect(Collectors.toList());
    }

    //온도 남기기
    public void leaveMannerTemp(String email, LeaveMannerTempRequest leaveMannerTempRequest) {
        // 내 정보 조회
        Parent me = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("온도를 남기려는 사용자 정보를 찾을 수 없습니다."));
        //온도가 남겨질 사용자 정보 조회
        Parent parent = parentRepository.findByNickname(leaveMannerTempRequest.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("온도가 남겨질 사용자 정보를 찾을 수 없습니다."));

        // 누적 온도 업데이트
        BigDecimal newCumulativeTemp = parent.getMannerTemp()
                .add(leaveMannerTempRequest.getMannerTemp());
        parent.setMannerTemp(newCumulativeTemp);

        // 온도 카운트 증가
        parent.setMannerTempCount(parent.getMannerTempCount() + 1);
    }



    // 부모와 FindFriend의 startTime을 함께 저장할 수 있는 클래스
    public static class ParentWithStartTime {
        private Parent parent;
        private LocalDateTime startTime;

        public ParentWithStartTime(Parent parent, LocalDateTime startTime) {
            this.parent = parent;
            this.startTime = startTime;
        }

        public Parent getParent() {
            return parent;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }
    }
}

