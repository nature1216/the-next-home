package com.ssafy.houseDeal.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetHouseDealWithKeywordRequest {
    private String type;
    private String code;
    private int pgSize;
    private int pgno;
    private int offset;
}