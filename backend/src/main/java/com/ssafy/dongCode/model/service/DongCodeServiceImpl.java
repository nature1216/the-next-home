package com.ssafy.dongCode.model.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.ssafy.dongCode.model.DongCodeDto;
import com.ssafy.dongCode.model.mapper.DongCodeMapper;
import com.ssafy.dongCode.model.request.SearchRequest;
import com.ssafy.dongCode.model.response.HouseDealSearchResult;
import com.ssafy.dongCode.model.response.SearchResultResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class DongCodeServiceImpl implements DongCodeService {

	private final DongCodeMapper dongCodeMapper;
	private final StringRedisTemplate redisTemplate;
	private static final String RECENT_SEARCH_KEY = "recentSearch: ";
	private static final int MAX_RECENT_SEARCHES = 10;

	@Override
	public List<DongCodeDto> getFilters(String parentCode) {
		return dongCodeMapper.getFilters(parentCode);
	}

	@Override
	public SearchResultResponse searchByKeyword(SearchRequest request) {
		List<HouseDealSearchResult> houses = dongCodeMapper.getHouseByKeyword(request);
		List<DongCodeDto> regions = dongCodeMapper.getDongCodeByKeyword(request);

		return SearchResultResponse.builder()
				.regions(regions)
				.houses(houses)
				.build();
	}

	@Override
	public String getOrCreateGuestMemberId(String memberId, HttpServletRequest request, HttpServletResponse response) {
		// 비회원 ID가 없을 경우 처리
		if (memberId == null) {
			// 세션에서 memberId 찾기
			memberId = (String) request.getSession().getAttribute("guestMemberId");

			if (memberId == null) {
				// 세션에 memberId 없으면 UUID로 고유 ID 생성
				memberId = UUID.randomUUID().toString();
				// 세션에 저장
				request.getSession().setAttribute("guestMemberId", memberId);

				// 쿠키에 저장
				Cookie cookie = new Cookie("guestMemberId", memberId);
				cookie.setMaxAge(60 * 60 * 24 * 30);  // 30일 동안 유효
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		return memberId;
	}

	@Override
	public void saveRecentSearch(String keyword, String memberId) {
		String key = getUserSearchKey(memberId);
		System.out.println(keyword);

		// 최근 검색어 저장 (중복 제거)
		redisTemplate.opsForList().remove(key, 1, keyword);
		redisTemplate.opsForList().leftPush(key, keyword);
		redisTemplate.opsForList().trim(key, 0, MAX_RECENT_SEARCHES - 1);

		// 키의 만료 시간 설정 (예: 7일)
		redisTemplate.expire(key, 7, TimeUnit.DAYS);
	}

	@Override
	public List<String> getRecentSearchKeywords(String memberId) {
		String key = getUserSearchKey(memberId);
		return (List<String>) (Object) redisTemplate.opsForList().range(key, 0, -1);
	}

	private String getUserSearchKey(String memberId) {
			return RECENT_SEARCH_KEY + memberId;
	}
}
