package com.mjuAppSW.joA.geography.location.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NearByInfo {
    private Long id;
    private String name;
    private String urlCode;
    private String bio;
    private Boolean isLiked;
}
