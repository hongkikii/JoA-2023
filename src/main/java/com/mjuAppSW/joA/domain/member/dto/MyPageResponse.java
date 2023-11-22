package com.mjuAppSW.joA.domain.member.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyPageResponse {
    private String name;
    private String urlCode;
    private String bio;
    private Integer todayHeart;
    private Integer totalHeart;
    private List<String> voteTop3;
}
