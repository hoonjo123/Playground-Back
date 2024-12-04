package com.swyp.playground.domain.report.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddReportDto {

    private Long targetId;

    private Long findFriendId;

    private String cause;

    private String writtenBy;

}
