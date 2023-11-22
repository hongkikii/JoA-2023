package com.mjuAppSW.joA.geography.location.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class NearByListResponse {
    private Integer status;
    private List<NearByInfo> nearByList;

    public NearByListResponse(Integer status) {
        this.status = status;
        this.nearByList = null;
    }
    public NearByListResponse(Integer status, List<NearByInfo> nearByList) {
        this.status = status;
        this.nearByList = nearByList;
    }
}
