package com.ssafy.favoriteProperty.model.service;

import com.ssafy.favoriteProperty.model.FavoritePropertyDetailsDto;
import com.ssafy.favoriteProperty.model.FavoritePropertyDto;

import java.util.List;

public interface FavoritePropertyService {
	void addFavoriteProperty(FavoritePropertyDto favoritePropertyDto); // 관심 매물 추가

	List<FavoritePropertyDetailsDto> getFavoriteProperties(String memberId);  // 관심 매물 전체 조회

	boolean existsFavoritePropertyByAptSeqAndId(String memberId, String aptSeq); // 관심 매물 조건 조회

	void deleteFavoriteProperty(String memberId, String aptSeq); // 관심 매물 삭제
}
