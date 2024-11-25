package com.swyp.playground.domain.playground.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse {
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private Header header;
        private Body body;
    }

    @Getter
    @Setter
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class Body {
        private int recordCountPerPage;
        private int pageIndex;
        private int totalPageCnt;
        private int totalCnt;
        private List<Item> items;
    }


    @Getter
    @Setter
    public static class Item {
        private String pfctSn;
        private String pfctNm;
        private String zip;
        private String lotnoAddr;
        private String lotnoDaddr;
        private String ronaAddr;
        private String ronaDaddr;
        private String instlYmd;
        private String clsgYmd;
        private String acptnYmd;
        private String etcSufa;
        private String exfcYn;
        private String fcar;
        private String instlPlaceCd;
        private String instlPlaceCdNm;
        private String dutyCd;
        private String dutyCdNm;
        private String prvtPblcYnCd;
        private String prvtPblcYnCdNm;
        private String operYnCd;
        private String operYnCdNm;
        private String idrodrCd;
        private String idrodrCdNm;
        private String exfcDsgnYmd;
        private String rgnCd;
        private String rgnCdNm;
        private String latCrtsVl;
        private String lotCrtsVl;
    }
}
