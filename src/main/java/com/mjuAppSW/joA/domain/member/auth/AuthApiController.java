package com.mjuAppSW.joA.domain.member.auth;

import com.mjuAppSW.joA.domain.member.dto.request.FindIdRequest;
import com.mjuAppSW.joA.domain.member.dto.request.FindPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.request.JoinRequest;
import com.mjuAppSW.joA.domain.member.dto.request.LoginRequest;
import com.mjuAppSW.joA.domain.member.dto.response.LoginResponse;
import com.mjuAppSW.joA.domain.member.dto.request.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.response.StatusResponse;
import com.mjuAppSW.joA.domain.member.dto.request.TransPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SendCertifyNumRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SendCertifyNumResponse;
import com.mjuAppSW.joA.domain.member.dto.request.VerifyCertifyNumRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/joa/auths")
public class AuthApiController {

    private final AuthService authService;

    @Operation(summary = "인증 번호 전송", description = "회원가입 시 학교 웹메일을 확인하기 위해 해당 웹메일로 인증번호를 전송하는 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 번호 웹메일 전송 후 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/send/certify-num")
    public ResponseEntity<SendCertifyNumResponse> sendCertifyNum(@RequestBody @Valid SendCertifyNumRequest request) {
        log.info("sendCertifyNum : collegeId = {}, email = {}", request.getCollegeId(), request.getUEmail());
        SendCertifyNumResponse response = authService.sendCertifyNum(request);
        log.info("sendCertifyNum Return : OK, status = {}, id = {}", response.getStatus(), response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "인증 번호 검증", description = "회원가입 시 학교 웹메일을 확인하기 위해 전송된 인증번호를 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 번호 검증 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/verify/certify-num")
    public ResponseEntity<StatusResponse> verifyCertifyNum(@RequestBody @Valid VerifyCertifyNumRequest request) {
        log.info("authCertifyNum : id = {}, certifyNum = {}, uEmail = {}, collegeId = {}",
                request.getId(), request.getCertifyNum(), request.getUEmail(), request.getCollegeId());
        StatusResponse response = authService.verifyCertifyNum(request);
        log.info("authCertifyNum Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이디 중복 검증", description = "회원가입 시 중복 아이디가 존재하는지 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "아이디 중복 검증 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/verify/id")
    public ResponseEntity<StatusResponse> verifyId
            (@Parameter(description = "사용자 세션 id", in = ParameterIn.QUERY) @RequestParam Long sessionId,
             @Parameter(description = "검증할 로그인 id", in = ParameterIn.PATH) @RequestParam String loginId) {
        log.info("verifyId : sessionId = {}, login id = {}", sessionId, loginId);
        StatusResponse response = authService.verifyId(sessionId, loginId);
        log.info("verifyId Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 가입", description = "회원 가입 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 가입 완료 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/join")
    public ResponseEntity<StatusResponse> join(@RequestBody @Valid JoinRequest request) {
        log.info("join : id = {}, name = {}, loginId = {}, password ={}",
                request.getId(), request.getName(), request.getLoginId(), request.getPassword());
        StatusResponse response = authService.join(request);
        log.info("join Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("login : id = {}, password = {}", request.getLoginId(), request.getPassword());
        LoginResponse response = authService.login(request);
        log.info("login Return : OK, status = {}, id = {}", response.getStatus(), response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/logout")
    public ResponseEntity<StatusResponse> logout(@RequestBody @Valid SessionIdRequest request) {
        log.info("logout : id = {}", request.getId());
        StatusResponse response = authService.logout(request);
        log.info("logout Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이디 찾기", description = "아이디 찾기 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 아이디 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/find/id")
    public ResponseEntity<StatusResponse> findId(@RequestBody @Valid FindIdRequest request) {
        log.info("findId : college Id = {}, uEmail = {}", request.getCollegeId(), request.getUEmail());
        StatusResponse response = authService.findId(request);
        if (response != null) {
            log.info("findId Return : OK, status = {}", response.getStatus());
            return ResponseEntity.ok(response);
        }
        log.warn("findId Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "비밀번호 찾기", description = "비밀번호 찾기 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "임시 비밀번호 웹메일 전송 후 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/find/password")
    public ResponseEntity<StatusResponse> findPassword(@RequestBody @Valid FindPasswordRequest request) {
        log.info("findPassword : Login Id = {}", request.getLoginId());
        StatusResponse response = authService.findPassword(request);
        if(response.getStatus() == 0) {
            log.info("findPassword Return : OK, status = {}", response.getStatus());
            return ResponseEntity.ok(response);
        }
        log.info("findPassword Return : BAD_REQUEST, status = {}", response.getStatus());
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 변경 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/trans/password")
    public ResponseEntity<StatusResponse> transPassword(@RequestBody @Valid TransPasswordRequest request) {
        log.info("transPassword : id = {}, current password = {}, new password = {}",
                request.getId(), request.getCurrentPassword(), request.getNewPassword());
        StatusResponse response = authService.transPassword(request);
        log.info("transPassword Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "탈퇴 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/withdrawal")
    public ResponseEntity<StatusResponse> withdrawal(@RequestBody @Valid SessionIdRequest request) {
        log.info("withdrawal : id = {}", request.getId());
        StatusResponse response = authService.withdrawal(request);
        log.info("withdrawal Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
