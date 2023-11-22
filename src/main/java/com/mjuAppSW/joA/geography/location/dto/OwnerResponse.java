package com.mjuAppSW.joA.geography.location.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OwnerResponse {
    private Integer status;
    private String name;
    private String urlCode;
    private String bio;
}
