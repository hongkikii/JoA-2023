package com.mjuAppSW.joA.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "JoA",
        description = "JoA의 API 문서입니다.",
        version = "v1"),
        servers = @Server(url = "https://real.najoa.net/", description = "운영 서버")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allOpenApi() {
        String[] paths = {"/joa/**"};

        return GroupedOpenApi
                .builder()
                .group("전체 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi authOpenApi() {
        String[] paths = {"/joa/auth/**"};

        return GroupedOpenApi
                .builder()
                .group("사용자 인증 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi memberProfileOpenApi() {
        String[] paths = {"/joa/member-profiles/**"};

        return GroupedOpenApi
                .builder()
                .group("사용자 정보 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi heartOpenApi() {
        String[] paths = {"/joa/hearts/**"};

        return GroupedOpenApi
                .builder()
                .group("하트 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi voteOpenApi() {
        String[] paths = {"/joa/votes/**"};

        return GroupedOpenApi
                .builder()
                .group("투표 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi voteReportOpenApi() {
        String[] paths = {"/joa/vote-reports/**"};

        return GroupedOpenApi
                .builder()
                .group("투표 신고 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi locationOpenApi() {
        String[] paths = {"/joa/locations/**"};

        return GroupedOpenApi
                .builder()
                .group("위치 API")
                .pathsToMatch(paths)
                .build();
    }


    @Bean
    public GroupedOpenApi messageOpenApi() {
        String[] paths = {"/joa/messages/**"};

        return GroupedOpenApi
                .builder()
                .group("채팅 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi messageReportOpenApi() {
        String[] paths = {"/joa/message-reports/**"};

        return GroupedOpenApi
                .builder()
                .group("채팅 신고 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi roomOpenApi() {
        String[] paths = {"/joa/rooms/**"};

        return GroupedOpenApi
                .builder()
                .group("채팅방 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi roomInMemberOpenApi() {
        String[] paths = {"/joa/messages/**"};

        return GroupedOpenApi
                .builder()
                .group("??? API")
                .pathsToMatch(paths)
                .build();
    }

}
