package com.mjuAppSW.joA.domain.memberProfile;

import com.mjuAppSW.joA.common.dto.SuccessResponse;
import com.mjuAppSW.joA.domain.memberProfile.dto.request.BioRequest;
import com.mjuAppSW.joA.domain.memberProfile.dto.response.MyPageResponse;
import com.mjuAppSW.joA.domain.memberProfile.dto.request.PictureRequest;
import com.mjuAppSW.joA.domain.memberProfile.dto.response.SettingPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/joa/member-profiles")
public class MemberProfileApiController {

    private final MemberProfileService memberProfileService;

    @Operation(summary = "설정 페이지 정보 조회", description = "설정 페이지에서 필요한 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "설정 페이지 필요 정보 반환"),
            @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "M002: 접근 권한이 없는 계정입니다.")
    })
    @GetMapping("/{id}/setting-page")
    public ResponseEntity<SuccessResponse<SettingPageResponse>> getSettingPage(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.PATH) @PathVariable("id") Long sessionId) {
        return SuccessResponse.of(memberProfileService.getSettingPage(sessionId))
                .asHttp(HttpStatus.OK);
    }

    @Operation(summary = "마이 페이지 정보 조회", description = "마이 페이지에서 필요한 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이 페이지 필요 정보 반환"),
            @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "M002: 접근 권한이 없는 계정입니다.")
    })
    @GetMapping("/{id}/my-page")
    public ResponseEntity<SuccessResponse<MyPageResponse>> getMyPage(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.PATH) @PathVariable("id") Long sessionId) {
        return SuccessResponse.of(memberProfileService.getMyPage(sessionId))
                .asHttp(HttpStatus.OK);
    }

    @Operation(summary = "한 줄 소개 변경", description = "한 줄 소개 변경 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 코드 반환"),
            @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "M002: 접근 권한이 없는 계정입니다."),
            @ApiResponse(responseCode = "500", description = "M003: 이미지 업로드에 실패하였습니다."),
    })
    @PatchMapping("/bio")
    public ResponseEntity<SuccessResponse<Void>> transBio(@RequestBody @Valid BioRequest request) {
        memberProfileService.transBio(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "한 줄 소개 삭제", description = "한 줄 소개 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 코드 반환"),
            @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "M002: 접근 권한이 없는 계정입니다.")
    })
    @PatchMapping("/{id}/bio")
    public ResponseEntity<SuccessResponse<Void>> deleteBio(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.PATH) @PathVariable("id") Long sessionId) {
        memberProfileService.deleteBio(sessionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 사진 변경", description = "프로필 사진 변경 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 코드 반환"),
            @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "M002: 접근 권한이 없는 계정입니다.")
    })
    @PatchMapping("/picture")
    public ResponseEntity<SuccessResponse<Void>> transPicture(@RequestBody @Valid PictureRequest request) {
        memberProfileService.transPicture(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 사진 삭제", description = "프로필 사진 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 코드 반환"),
            @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "M002: 접근 권한이 없는 계정입니다.")
    })
    @PatchMapping("/{id}/picture")
    public ResponseEntity<Void> deletePicture(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.PATH) @PathVariable("id") Long sessionId) {
        memberProfileService.deletePicture(sessionId);
        return ResponseEntity.ok().build();
    }
}
