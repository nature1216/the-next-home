package com.ssafy.dongCode.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.dongCode.model.DongCodeDto;
import com.ssafy.dongCode.model.mapper.DongCodeMapper;

@Service
public class DongCodeServiceImpl implements DongCodeService {
	
	private final DongCodeMapper dongCodeMapper;
	
	public DongCodeServiceImpl(DongCodeMapper dongCodeMapper) {
		this.dongCodeMapper = dongCodeMapper;
	}

	@Override
	public List<DongCodeDto> getFilters(String parentCode) {
		return dongCodeMapper.getFilters(parentCode);
	}

}