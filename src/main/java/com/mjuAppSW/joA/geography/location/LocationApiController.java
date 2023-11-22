package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.location.dto.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LocationApiController {

    private final LocationServiceImpl locationService;

    @PostMapping("geo/update")
    public ResponseEntity<UpdateResponse> updateLocation(@RequestBody @Valid UpdateRequest request) {
        log.info("updateLocation : id = {}, latitude = {}, longitude = {}, altitude = {}",
                request.getId(), request.getLatitude(), request.getLongitude(), request.getAltitude());
        UpdateResponse response = locationService.updateLocation(request);
        log.info("updateLocation Return : OK, status = {}, isContained = {}",
                response.getStatus(), response.getIsContained());
        return ResponseEntity.ok(response);
    }

    @GetMapping("geo/get")
    public ResponseEntity<NearByListResponse> getNearByList(@RequestParam @NotNull Long id,
                                                            @RequestParam @NotBlank Double latitude,
                                                            @RequestParam @NotBlank Double longitude,
                                                            @RequestParam @NotBlank Double altitude) {
        log.info("getNearByList : id = {}, latitude = {}, longitude = {}, altitude = {}",
                id, latitude, longitude, altitude);
        UpdateRequest request = new UpdateRequest(id, latitude, longitude, altitude);
        NearByListResponse response = locationService.getNearByList(request);
        if(response.getStatus() == 0) {
            log.info("getNearByList Return : OK, nearByList size = {}", response.getNearByList().size());
        }
        else {
            log.warn("getNearByList Return :  OK, status = {}", response.getStatus());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("geo/set/polygon")
    public void setPolygon(@RequestBody @Valid PolygonRequest request) {
        locationService.setPolygon(request);
    }

//    @PostMapping("geo/delete")
//    public void deleteLocation(@RequestBody @Valid IdRequest request) {
//        log.info("위치 삭제 api 요청");
//        log.info("id = {}", request.getId());
//        geoService.deleteLocation(request);
//    }

    @GetMapping("/geo/get/owner")
    public ResponseEntity<OwnerResponse> getLocationOwnerInfo(@RequestParam @NotNull Long id) {
        log.info("getLocationOwnerInfo : id = {}", id);
        OwnerResponse response = locationService.getOwnerInfo(id);
        if(response.getStatus() == 0) {
            log.info("getLocationOwnerInfo Return : OK, status = {}, name = {}, urlCode = {}, bio = {}",
                    response.getStatus(), response.getName(), response.getUrlCode(), response.getBio());
        }
        else {
            log.warn("getLocationOwnerInfo Return :  OK, status = {}", response.getStatus());
        }
        return ResponseEntity.ok(response);
    }
}
