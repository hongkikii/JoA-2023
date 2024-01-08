package com.mjuAppSW.joA.geography.college;

import com.mjuAppSW.joA.geography.college.dto.PolygonRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/joa/colleges")
public class PCollegeApiController {

    private final PCollegeService pCollegeService;

    @Operation(summary = "학교 범위 등록", description = "학교 범위 등록 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 코드 반환"),
    })
    @PostMapping
    public ResponseEntity<Void> setPolygon(@RequestBody @Valid PolygonRequest request) {
        pCollegeService.setPolygon(request);
        return ResponseEntity.ok().build();
    }
}
