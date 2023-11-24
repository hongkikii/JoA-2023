package com.mjuAppSW.joA.geography.block;

import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.dto.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BlockApiController {

    private final BlockService blockService;

    @PostMapping("/block")
    public ResponseEntity<StatusResponse> execute(@RequestBody BlockRequest blockRequest) {
        StatusResponse response = blockService.execute(blockRequest);
        return ResponseEntity.ok(response);
    }
}
