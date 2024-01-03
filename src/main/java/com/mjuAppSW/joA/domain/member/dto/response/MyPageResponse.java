package com.mjuAppSW.joA.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "마이페이지 Response")
public class MyPageResponse {
    private String name;
    private String urlCode;
    private String bio;
    private Integer todayHeart;
    private Integer totalHeart;
    private List<String> voteTop3;
}
