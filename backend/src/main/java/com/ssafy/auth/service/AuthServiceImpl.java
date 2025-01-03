package com.ssafy.auth.service;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Objects;

import com.ssafy.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.address.model.AddressDto;
import com.ssafy.address.model.mapper.AddressMapper;
import com.ssafy.auth.model.request.LoginRequest;
import com.ssafy.auth.model.request.ResetPasswordRequest;
import com.ssafy.auth.model.request.SendResetPasswordEmailRequest;
import com.ssafy.auth.model.request.SignUpRequest;
import com.ssafy.auth.model.request.SignUpVerificationRequest;
import com.ssafy.email.service.PasswordResetEmailService;
import com.ssafy.email.service.SignUpEmailService;
import com.ssafy.exception.ApiException;
import com.ssafy.exception.ErrorCode;
import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.mapper.MemberMapper;
import com.ssafy.member.model.service.MemberService;
import com.ssafy.util.PasswordUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	//	MailSenderUtil mailSenderUtil;
	private final SignUpEmailService signUpEmailService;
	private final RedisService redisService;
	private final PasswordResetEmailService passwordResetEmailService;
	private final AddressMapper addressMapper;
	private final MemberService memberService;
	private final MemberMapper memberMapper;
	private final StringRedisTemplate redisTemplate;
	private final BCryptPasswordEncoder passwordEncoder;

	private final String VERIFICATION_SIGNUP = "signup: ";
	private final String VERIFICATION_PASSWORD_RESET = "passwordReset: ";

	@Value("${smtp.reset_password.expiration_time}")
	private long resetPasswordExpirationTime;
	@Value("${smtp.signup.expiration_time}")
	private long signUpExpirationTime;

	@Override
	public MemberDto login(LoginRequest loginInfo) throws SQLException {
		try {
			MemberDto member = memberMapper.getMemberByMemberId(loginInfo.getId());
			if (member == null) {
				throw new ApiException("존재하지 않는 사용자입니다.", ErrorCode.MEMBER_NOT_FOUND);
			}
			if (!PasswordUtil.verifyPassword(loginInfo.getPassword(), member.getPassword())) {
				throw new ApiException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
			}
			return member;
		} catch (ApiException e) {
			throw e;
		} catch (Exception e) {
			throw new ApiException("로그인 중 오류가 발생했습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@Override
	public void signUp(SignUpRequest signUpRequest) throws SQLException {
		// 기본 역할 설정
		MemberDto memberDto = new MemberDto();
		memberDto.setId(signUpRequest.getId());
		memberDto.setEmail(signUpRequest.getEmail());
		memberDto.setName(signUpRequest.getName());
		String hashedPassword = PasswordUtil.encodePassword(signUpRequest.getPassword());
		memberDto.setPassword(hashedPassword);

		if (Objects.equals(signUpRequest.getRole(), "") || Objects.equals(signUpRequest.getRole(), null)) {
			memberDto.setRole("USER"); // 기본값으로 "USER" 설정
		} else {
			memberDto.setRole(signUpRequest.getRole());
		}

		memberMapper.insertMember(memberDto);

		// 기본 주소 저장
		if (signUpRequest.getAddress() != null && signUpRequest.getAddress() != "") {
			AddressDto addressDto = new AddressDto();
			addressDto.setMemberId(memberDto.getId());
			addressDto.setName("기본 주소");
			addressDto.setRoadNameAddress(signUpRequest.getAddress());

			addressMapper.insertAddress(addressDto);
		}

	}

	@Override
	public String sendSignUpMail(String email) throws MessagingException {
		String code = signUpEmailService.send(email);

		redisService.setDataWithExpiration(VERIFICATION_SIGNUP + code, email, signUpExpirationTime);
		return code;
	}

	@Override
	public boolean verifySignUpCode(SignUpVerificationRequest request) {
		String email = redisService.getData(VERIFICATION_SIGNUP + request.getCode());
		
		if (email != null && email.equals(request.getEmail())) {
			return true;
		}
		return false;
	}

	@Override
	public String findId(String name, String email) {
		MemberDto member = memberMapper.getMemberByNameAndEmail(name, email);
		if (member != null) {
			return member.getId();
		}
		return "";
	}

	@Override
	public String sendResetPasswordEmail(SendResetPasswordEmailRequest request) throws MessagingException {
		if (!memberService.existsByEmailAndId(request.getEmail(), request.getId())) {
			throw new ApiException(ErrorCode.MEMBER_NOT_FOUND);
		}

		String uuid = passwordResetEmailService.send(request.getEmail());

		redisService.setDataWithExpiration(VERIFICATION_PASSWORD_RESET + uuid, request.getEmail(), resetPasswordExpirationTime);

		return uuid;
	}

	@Override
	public boolean verifyResetPasswordCode(String uuid) {
		String email = redisService.getData(VERIFICATION_PASSWORD_RESET + uuid);
		if (email == null) {
			return false;
		}

		return true;
	}

	@Override
	public void updatePassword(ResetPasswordRequest request) {
		if (request.getConfirmNewPassword().equals(request.getNewPassword())) {
			memberMapper.updatePassword(request.getEmail(), passwordEncoder.encode(request.getNewPassword()));
			redisTemplate.delete(VERIFICATION_PASSWORD_RESET + request.getUuid());
		}
	}

}
