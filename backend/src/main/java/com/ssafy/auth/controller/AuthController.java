package com.ssafy.auth.controller;

import java.util.Map;

import com.ssafy.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.auth.model.request.LoginRequest;
import com.ssafy.auth.model.request.ResetPasswordRequest;
import com.ssafy.auth.model.request.SendResetPasswordEmailRequest;
import com.ssafy.auth.model.request.SignUpRequest;
import com.ssafy.auth.model.request.SignUpVerificationRequest;
import com.ssafy.auth.service.AuthService;
import com.ssafy.exception.ApiException;
import com.ssafy.exception.ErrorCode;
import com.ssafy.member.model.MemberDto;
import com.ssafy.token.TokenProvider;
import com.ssafy.util.CookieUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;
	private final RedisService redisService;

	@Value("${jwt.refresh_expiration_time}")
	private long refreshTokenExpirationTime;

	@Operation(summary = "로그인", description = "사용자 로그인 처리")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "401", description = "로그인 실패 - 인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 오류")})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody
	LoginRequest request, HttpServletResponse response) {
		try {
			MemberDto memberDto = authService.login(request);

			String accessToken = tokenProvider.generateAccessToken(memberDto.getId(), memberDto.getRole());
			String refreshToken = tokenProvider.generateRefreshToken(memberDto.getId());
			redisService.setDataWithExpiration("refreshToken:" + memberDto.getId(), refreshToken, refreshTokenExpirationTime);

			CookieUtil.addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60, "/");

			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

			Map<String, Object> responseBody = Map.of(
				"name", memberDto.getName(),
				"id", memberDto.getId(),
				"role", memberDto.getRole(),
				"refreshToken", refreshToken);
			return ResponseEntity.ok().headers(headers).body(responseBody);
		} catch (ApiException e) {
			throw e;
		} catch (Exception e) {
			throw new ApiException("로그인 처리 중 예상치 못한 오류가 발생했습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Access Token 재발급", description = "Refresh Token으로 Access Token을 재발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Access Token 재발급 성공"),
		@ApiResponse(responseCode = "403", description = "Refresh Token이 유효하지 않음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken", required = false)
	String refreshToken, HttpServletResponse response) {
		try {
			if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token");
			}

			String memberId = tokenProvider.getMemberIdFromToken(refreshToken);
			String role = tokenProvider.getMemberRoleFromToken(refreshToken);

			String newAccessToken = tokenProvider.generateAccessToken(memberId, role);
			String newRefreshToken = tokenProvider.generateRefreshToken(memberId);

			CookieUtil.addCookie(response, "refreshToken", newRefreshToken, 7 * 24 * 60 * 60, "/");

			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

			return ResponseEntity.ok().headers(headers).body("새로운 Access Token이 발급되었습니다.");
		} catch (Exception e) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "로그아웃", description = "사용자가 로그아웃을 하면 서버에서 세션을 종료하고, 클라이언트에서 토큰을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response, @AuthenticationPrincipal
	String memberId) {
		try {
			redisService.deleteData(memberId);
			CookieUtil.deleteCookie(response, "refreshToken", "/");
			return ResponseEntity.ok().body("로그아웃 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
		}
	}

	@Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
	@ApiResponses({@ApiResponse(responseCode = "201", description = "회원 가입 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류 - 회원 가입 중 오류 발생")})
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody
	SignUpRequest signUpRequest) {
		try {
			authService.signUp(signUpRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입이 완료되었습니다. 로그인 해주세요.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 가입 중 오류가 발생했습니다.");
		}
	}

	@PostMapping("/signup-email")
	public ResponseEntity<String> sendSignUpMail(@RequestParam("email")
	String email) throws MessagingException {
		return ResponseEntity.ok(authService.sendSignUpMail(email));
	}

	@PostMapping("/signup-verification")
	public ResponseEntity<Boolean> verifySignUpCode(@RequestBody
	SignUpVerificationRequest request) {
		return ResponseEntity.ok(authService.verifySignUpCode(request));
	}

	@GetMapping("/id")
	public ResponseEntity<String> findId(@RequestParam("name")
	String name, @RequestParam("email")
	String email) {
		return ResponseEntity.ok(authService.findId(name, email));
	}

	@PostMapping("/password-reset-email")
	public ResponseEntity<String> sendResetPasswordEmail(@RequestBody
	SendResetPasswordEmailRequest request) {
		try {
			return ResponseEntity.ok(authService.sendResetPasswordEmail(request));
		} catch (MessagingException e) {
			throw new ApiException(ErrorCode.FAILED_SEND_EMAIL);
		}
	}

	@PostMapping("/password-reset-verification")
	public ResponseEntity<Boolean> verifyResetPasswordCode(@RequestParam("uuid")
	String uuid) {
		return ResponseEntity.ok(authService.verifyResetPasswordCode(uuid));
	}

	@PutMapping("/password")
	public ResponseEntity<Void> updatePassword(@RequestBody
	ResetPasswordRequest request) {
		authService.updatePassword(request);
		return ResponseEntity.ok().build();
	}

}
