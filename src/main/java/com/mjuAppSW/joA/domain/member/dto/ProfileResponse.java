package com.mjuAppSW.joA.domain.member.dto;

import lombok.Getter;

@Getter
public class ProfileResponse {
    private String name;
    private String urlCode;
    private String bio;

    public ProfileResponse(String name, String urlCode, String bio) {
        this.name = name;
        this.urlCode = urlCode;
        this.bio = bio;
    }
}
