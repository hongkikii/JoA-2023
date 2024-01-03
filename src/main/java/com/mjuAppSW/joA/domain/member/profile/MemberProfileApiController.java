package com.mjuAppSW.joA.domain.member.profile;

import com.mjuAppSW.joA.domain.member.dto.request.BioRequest;
import com.mjuAppSW.joA.domain.member.dto.response.MyPageResponse;
import com.mjuAppSW.joA.domain.member.dto.request.PictureRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SetPageResponse;
import com.mjuAppSW.joA.domain.member.dto.response.StatusResponse;
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
@RequestMapping("/joa/member-profiles")
public class MemberProfileApiController {

    private final MemberProfileServiceImpl setService;

    @Operation(summary = "설정 페이지 정보 조회", description = "설정 페이지에서 필요한 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "설정 페이지 필요 정보 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/setting-page")
    public ResponseEntity<SetPageResponse> getSettingPage(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.QUERY) @RequestParam Long sessionId) {

        log.info("getSettingPage : sessionId = {}", sessionId);
        SetPageResponse response = setService.getSettingPage(sessionId);
        log.info("getSettingPage Return : OK, status = {}, name = {}, urlCode = {}",
                response.getStatus(), response.getName(), response.getUrlCode());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "마이 페이지 정보 조회", description = "마이 페이지에서 필요한 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "마이 페이지 필요 정보 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/my-page")
    public ResponseEntity<MyPageResponse> getMyPage(
            @Parameter(description = "세션 id", in = ParameterIn.QUERY) @RequestParam Long sessionId) {

        log.info("getMyPage : sessionId = {}", sessionId);
        MyPageResponse response = setService.getMyPage(sessionId);
        if (response != null) {
            log.info("getMyPage Return : OK, "
                    + "name = {}, urlCode = {}, bio = {}, todayHeart = {}, totalHeart = {}, voteTop3 size = {}",
                    response.getName(), response.getUrlCode(), response.getBio(),
                    response.getTodayHeart(), response.getTotalHeart(), response.getVoteTop3().size());
            return ResponseEntity.ok(response);
        }
        log.warn("getMyPage Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "한 줄 소개 변경", description = "사용자 한 줄 소개 변경 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "한 줄 소개 변경 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/trans/bio")
    public ResponseEntity<StatusResponse> transBio(@RequestBody @Valid BioRequest request) {
        log.info("transBio : id = {}, bio = {}", request.getId(), request.getBio());
        StatusResponse response = setService.transBio(request);
        log.info("transBio Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "한 줄 소개 삭제", description = "한 줄 소개 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "한 줄 소개 삭제 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/set/myPage/bio/delete")
    public ResponseEntity<StatusResponse> deleteBio(@RequestBody @Valid SessionIdRequest request) {
        log.info("deleteBio : id = {}", request.getId());
        StatusResponse response = setService.deleteBio(request);
        log.info("deleteBio Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "프로필 사진 변경", description = "프로필 사진 변경 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 사진 변경 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/set/myPage/picture")
    public ResponseEntity<StatusResponse> transPicture(@RequestBody @Valid PictureRequest request) {
        log.info("transPicture : id = {}, base64Image is null = {}",
                request.getId(), request.getBase64Picture().isEmpty());
        StatusResponse response = setService.transPicture(request);
        log.info("transPicture Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "프로필 사진 삭제", description = "프로필 사진 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 사진 삭제 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/set/myPage/picture/delete")
    public ResponseEntity<StatusResponse> deletePicture(@RequestBody @Valid SessionIdRequest request) {
        log.info("deletePicture : id = {}", request.getId());
        StatusResponse response = setService.deletePicture(request);
        log.info("deletePicture Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
