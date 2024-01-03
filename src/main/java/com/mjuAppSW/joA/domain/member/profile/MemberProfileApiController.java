package com.mjuAppSW.joA.domain.member.profile;

import com.mjuAppSW.joA.domain.member.dto.BioRequest;
import com.mjuAppSW.joA.domain.member.dto.IdRequest;
import com.mjuAppSW.joA.domain.member.dto.MyPageResponse;
import com.mjuAppSW.joA.domain.member.dto.NameRequest;
import com.mjuAppSW.joA.domain.member.dto.PictureRequest;
import com.mjuAppSW.joA.domain.member.dto.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.SetResponse;
import com.mjuAppSW.joA.domain.member.dto.StatusResponse;
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
public class MemberProfileApiController {

    private final MemberProfileServiceImpl setService;

    @GetMapping("/set")
    public ResponseEntity<SetResponse> set(@RequestParam Long id) {
        log.info("set : id = {}", id);
        SetResponse response = setService.set(id);
        log.info("set Return : OK, status = {}, name = {}, urlCode = {}",
                response.getStatus(), response.getName(), response.getUrlCode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/set/myPage")
    public ResponseEntity<MyPageResponse> sendMyPage(@RequestParam Long id) {
        log.info("sendMyPage : id = {}", id);
        MyPageResponse response = setService.sendMyPage(id);
        if (response != null) {
            log.info("sendMyPage Return : OK, "
                    + "name = {}, urlCode = {}, bio = {}, todayHeart = {}, totalHeart = {}, voteTop3 size = {}",
                    response.getName(), response.getUrlCode(), response.getBio(),
                    response.getTodayHeart(), response.getTotalHeart(), response.getVoteTop3().size());
            return ResponseEntity.ok(response);
        }
        log.warn("sendMyPage Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/set/myPage/name")
    public ResponseEntity<StatusResponse> transName(@RequestBody @Valid NameRequest request) {
        log.info("transName : id = {}, name = {}", request.getId(), request.getName());
        StatusResponse response = setService.transName(request);
        log.info("transName Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/myPage/bio")
    public ResponseEntity<StatusResponse> deleteBio(@RequestBody @Valid BioRequest request) {
        log.info("transBio : id = {}, bio = {}", request.getId(), request.getBio());
        StatusResponse response = setService.transBio(request);
        log.info("transBio Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/myPage/bio/delete")
    public ResponseEntity<StatusResponse> deleteBio(@RequestBody @Valid IdRequest request) {
        log.info("transBio : id = {}", request.getId());
        StatusResponse response = setService.deleteBio(request);
        log.info("transBio Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/myPage/picture")
    public ResponseEntity<StatusResponse> transPicture(@RequestBody @Valid PictureRequest request) {
        log.info("transPicture : id = {}, base64Image is null = {}",
                request.getId(), request.getBase64Picture().isEmpty());
        StatusResponse response = setService.transPicture(request);
        log.info("transPicture Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/myPage/picture/delete")
    public ResponseEntity<StatusResponse> deletePicture(@RequestBody @Valid SessionIdRequest request) {
        log.info("deletePicture : id = {}", request.getId());
        StatusResponse response = setService.deletePicture(request);
        log.info("deletePicture Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }
}
