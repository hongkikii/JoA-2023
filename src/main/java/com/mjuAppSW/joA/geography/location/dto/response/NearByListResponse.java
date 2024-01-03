package com.mjuAppSW.joA.geography.location.dto.response;

import com.mjuAppSW.joA.geography.location.dto.response.NearByInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "주변 사람 목록 Response")
public class NearByListResponse {
    private Integer status;
    private List<NearByInfo> nearByList;

    public NearByListResponse(Integer status) {
        this.status = status;
        this.nearByList = new ArrayList<>();
    }
    public NearByListResponse(Integer status, List<NearByInfo> nearByList) {
        this.status = status;
        this.nearByList = nearByList;
    }
}
