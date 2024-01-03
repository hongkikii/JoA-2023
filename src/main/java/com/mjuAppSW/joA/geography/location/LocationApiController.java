package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.dto.StatusResponse;
import com.mjuAppSW.joA.geography.location.dto.response.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.response.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.request.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.request.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.response.UpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/joa/locations")
public class LocationApiController {

    private final LocationServiceImpl locationService;

    @Operation(summary = "사용자 위치 업데이트", description = "사용자 위치 업데이트 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "학교 내 위치 여부 정보 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/update")
    public ResponseEntity<UpdateResponse> updateLocation(@RequestBody @Valid UpdateRequest request) {
        log.info("updateLocation : id = {}, latitude = {}, longitude = {}, altitude = {}",
                request.getId(), request.getLatitude(), request.getLongitude(), request.getAltitude());
        UpdateResponse response = locationService.updateLocation(request);
        log.info("updateLocation Return : OK, status = {}, isContained = {}",
                response.getStatus(), response.getIsContained());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주변 사람 목록 조회", description = "주변 사람 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주변 사람 목록 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/near-by-list")
    public ResponseEntity<NearByListResponse> getNearByList(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.QUERY) @RequestParam @NotNull Long sessionId,
            @Parameter(description = "사용자 현재 위도", in = ParameterIn.QUERY) @RequestParam @NotBlank Double latitude,
            @Parameter(description = "사용자 현재 경도", in = ParameterIn.QUERY) @RequestParam @NotBlank Double longitude,
            @Parameter(description = "사용자 현재 고도", in = ParameterIn.QUERY) @RequestParam @NotBlank Double altitude) {
        log.info("getNearByList : sessionId = {}, latitude = {}, longitude = {}, altitude = {}",
                sessionId, latitude, longitude, altitude);
        UpdateRequest request = new UpdateRequest(sessionId, latitude, longitude, altitude);
        NearByListResponse response = locationService.getNearByList(request);
        if(response.getStatus() == 0) {
            log.info("getNearByList Return : OK, nearByList size = {}", response.getNearByList().size());
        }
        else {
            log.warn("getNearByList Return :  OK, status = {}", response.getStatus());
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "학교 범위 설정", description = "학교 범위 설정 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주변 사람 목록 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("set/polygon")
    public void setPolygon(@RequestBody @Valid PolygonRequest request) {
        locationService.setPolygon(request);
    }

    @Operation(summary = "주변 사람 목록 화면 사용자 정보 조회", description = "주변 사람 목록 화면 사용자 정보 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @GetMapping("/owner")
    public ResponseEntity<OwnerResponse> getOwner(
            @Parameter(description = "사용자 세션 id", in = ParameterIn.QUERY) @RequestParam @NotNull Long sessionId) {

        log.info("getLocationOwnerInfo : sessionId = {}", sessionId);
        OwnerResponse response = locationService.getOwner(sessionId);
        if(response.getStatus() == 0) {
            log.info("getLocationOwnerInfo Return : OK, status = {}, name = {}, urlCode = {}, bio = {}",
                    response.getStatus(), response.getName(), response.getUrlCode(), response.getBio());
        }
        else {
            log.warn("getLocationOwnerInfo Return :  OK, status = {}", response.getStatus());
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 차단", description = "사용자 차단 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 차단 확인 코드 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/block")
    public ResponseEntity<StatusResponse> block(@RequestBody BlockRequest blockRequest) {
        log.info("block : blockerId = {}, blockedId = {}", blockRequest.getBlockerId(), blockRequest.getBlockedId());
        StatusResponse response = locationService.block(blockRequest);
        log.info("block return : status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
