package com.ssafy.houseDeal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.houseDeal.model.HouseDealDto;
import com.ssafy.houseDeal.model.service.HouseDealService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/house")
@AllArgsConstructor
@Tag(name = "House Deal controller", description = "매물 검색 기능을 제공하는 HouseDeal API")
public class HouseDealController {

    private final HouseDealService houseDealService;

    @Operation(summary = "필터링 조건에 따른 주택 매물 검색", description = "특정 필터 조건에 맞는 주택 매물 목록을 조회합니다.")
    @ApiResponses({
    	@ApiResponse(responseCode="200", description = "필터링된 매물 목록 조회 성공"),
    	@ApiResponse(responseCode="400", description = "잘못된 요청 - 필터 파라미터가 잘못되었거나 누락됨")
    })
    @Parameters({
    	@Parameter(name="sidoCode", description="시도코드", example="11"),
    	@Parameter(name="gugunCode", description="구군코드", example="110"),
    	@Parameter(name="dongCode", description="동코드", example="17400")
    })
    @GetMapping
    public ResponseEntity<List<HouseDealDto>> getHouseDeals(
    		@RequestParam Map<String, String> request) {
        List<HouseDealDto> houseDeals = houseDealService.getHouseDeals(request);
        return ResponseEntity.ok(houseDeals);
    }

}