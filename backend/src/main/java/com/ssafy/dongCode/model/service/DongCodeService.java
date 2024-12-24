package com.ssafy.dongCode.model.service;

import java.util.List;

import com.ssafy.dongCode.model.DongCodeDto;
import com.ssafy.dongCode.model.request.SearchRequest;
import com.ssafy.dongCode.model.response.SearchResultResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface DongCodeService {
	List<DongCodeDto> getFilters(String parentCode);

	SearchResultResponse searchByKeyword(SearchRequest request);
	String getOrCreateGuestMemberId(String memberId, HttpServletRequest request, HttpServletResponse response);

	void saveRecentSearch(String keyword, String memberId);
	List<String> getRecentSearchKeywords(String memberId);
}
