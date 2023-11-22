package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.member.dto.FindIdRequest;
import com.mjuAppSW.joA.domain.member.dto.FindPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.IdRequest;
import com.mjuAppSW.joA.domain.member.dto.JoinRequest;
import com.mjuAppSW.joA.domain.member.dto.LoginRequest;
import com.mjuAppSW.joA.domain.member.dto.LoginResponse;
import com.mjuAppSW.joA.domain.member.dto.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.StatusResponse;
import com.mjuAppSW.joA.domain.member.dto.TransPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.UMailRequest;
import com.mjuAppSW.joA.domain.member.dto.UMailResponse;
import com.mjuAppSW.joA.domain.member.dto.UNumRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberServiceImpl memberService;

    @PostMapping("/mail/send")
    public ResponseEntity<UMailResponse> sendCertifyNum(@RequestBody @Valid UMailRequest request) {
        log.info("sendCertifyNum : collegeId = {}, email = {}", request.getCollegeId(), request.getUEmail());
        UMailResponse response = memberService.sendCertifyNum(request);
        log.info("sendCertifyNum Return : OK, status = {}, id = {}", response.getStatus(), response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mail/auth")
    public ResponseEntity<StatusResponse> authCertifyNum(@RequestBody @Valid UNumRequest request) {
        log.info("authCertifyNum : id = {}, certifyNum = {}, uEmail = {}, collegeId = {}",
                request.getId(), request.getCertifyNum(), request.getUEmail(), request.getCollegeId());
        StatusResponse response = memberService.authCertifyNum(request);
        log.info("authCertifyNum Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/join/verify/id")
    public ResponseEntity<StatusResponse> verifyId(@RequestParam Long id, @RequestParam String loginId) {
        log.info("verifyId : id = {}, login id = {}", id, loginId);
        StatusResponse response = memberService.verifyId(id, loginId);
        log.info("verifyId Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<StatusResponse> join(@RequestBody @Valid JoinRequest request) {
        log.info("join : id = {}, name = {}, loginId = {}, password ={}",
                request.getId(), request.getName(), request.getLoginId(), request.getPassword());
        StatusResponse response = memberService.join(request);
        log.info("join Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("login : id = {}, password = {}", request.getLoginId(), request.getPassword());
        LoginResponse response = memberService.login(request);
        log.info("login Return : OK, status = {}, id = {}", response.getStatus(), response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<StatusResponse> logout(@RequestBody @Valid IdRequest request) {
        log.info("logout : id = {}", request.getId());
        StatusResponse response = memberService.logout(request);
        log.info("logout Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/find/id")
    public ResponseEntity<StatusResponse> findId(@RequestBody @Valid FindIdRequest request) {
        log.info("findId : college Id = {}, uEmail = {}", request.getCollegeId(), request.getUEmail());
        StatusResponse response = memberService.findId(request);
        if (response != null) {
            log.info("findId Return : OK, status = {}", response.getStatus());
            return ResponseEntity.ok(response);
        }
        log.warn("findId Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login/find/password")
    public ResponseEntity<StatusResponse> findPassword(@RequestBody @Valid FindPasswordRequest request) {
        log.info("findPassword : Login Id = {}", request.getLoginId());
        StatusResponse response = memberService.findPassword(request);
        if(response.getStatus() == 0) {
            log.info("findPassword Return : OK, status = {}", response.getStatus());
            return ResponseEntity.ok(response);
        }
        log.info("findPassword Return : BAD_REQUEST, status = {}", response.getStatus());
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login/password")
    public ResponseEntity<StatusResponse> transPassword(@RequestBody @Valid TransPasswordRequest request) {
        log.info("transPassword : id = {}, current password = {}, new password = {}",
                request.getId(), request.getCurrentPassword(), request.getNewPassword());
        StatusResponse response = memberService.transPassword(request);
        log.info("transPassword Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/withdrawal")
    public ResponseEntity<StatusResponse> withdrawal(@RequestBody @Valid SessionIdRequest request) {
        log.info("withdrawal : id = {}", request.getId());
        StatusResponse response = memberService.withdrawal(request);
        log.info("withdrawal Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
