package com.mjuAppSW.joA.domain.heart;

import com.mjuAppSW.joA.domain.heart.dto.HeartRequest;
import com.mjuAppSW.joA.domain.heart.dto.HeartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HeartApiController {

    private final HeartServiceImpl heartService;

    @PostMapping("heart/send")
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
