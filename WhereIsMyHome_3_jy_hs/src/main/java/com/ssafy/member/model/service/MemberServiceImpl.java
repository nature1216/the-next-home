package com.ssafy.member.model.service;

import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.mapper.MemberMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public MemberDto login(Map<String, String> request) throws SQLException {
        return memberMapper.getMember(request);
    }

    @Override
    public void join(MemberDto memberDto) throws SQLException {
        memberMapper.insertMember(memberDto);
    }
}