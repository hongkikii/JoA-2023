package com.mjuAppSW.joA.domain.heart;

import com.mjuAppSW.joA.domain.heart.dto.HeartRequest;
import com.mjuAppSW.joA.domain.heart.dto.HeartResponse;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/joa/hearts")
public class HeartApiController {

    private final HeartServiceImpl heartService;

    @Operation(summary = "하트 전송", description = "하트 전송 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "하트 정보 반환"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Z001: id에 해당하는 사용자를 찾을 수 없습니다.")
    })
    @PostMapping("/send")
    public ResponseEntity<HeartResponse> sendHeart(@RequestBody @Valid HeartRequest request) {
        log.info("sendHeart : giveId = {}, takeId = {}, named = {}",
                request.getGiveId(), request.getTakeId(), request.getNamed());

        HeartResponse response = heartService.sendHeart(request);

        log.info("sendHeart Return : OK, "
                + "status = {}, isMatched = {}, giveName = {}, takeName = {}, giveUrlCode= {}, takeUrlCode = {}",
                response.getStatus(), response.getIsMatched(), response.getGiveName(), response.getTakeName(),
                response.getGiveUrlCode(), response.getTakeUrlCode());

        return ResponseEntity.ok(response);
    }
}
