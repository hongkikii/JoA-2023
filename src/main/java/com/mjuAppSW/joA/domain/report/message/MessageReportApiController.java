package com.mjuAppSW.joA.domain.report.message;

import java.time.LocalDateTime;

import com.mjuAppSW.joA.common.dto.SuccessResponse;
import com.mjuAppSW.joA.domain.report.message.dto.request.CheckMessageReportRequest;
import com.mjuAppSW.joA.domain.report.message.dto.request.ReportRequest;
import com.mjuAppSW.joA.domain.report.message.dto.response.CheckMessageReportResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/joa/reports")
@Slf4j
public class MessageReportApiController {

    private final MessageReportService messageReportService;

    // @PostMapping("/report/message")
    // public ResponseEntity<String> messageReport(@RequestBody ReportRequest request){
    //     log.info("messageReport : messageId = {}, categoryId = {}, content = {}", request.getMessageId(), request.getCategoryId(), request.getContent());
    //     String save = messageReportService.messageReport(request.getMessageId(), request.getCategoryId(), request.getContent());
    //     if(save.equals("0")){
    //         log.info("messageReport Return : OK, success to report");
    //         return ResponseEntity.ok().build();
    //     }else if(save.equals("1")){
    //         log.warn("messageReport Return : BAD_REQUEST, getValue's not correct / messageId = {}, categoryId = {}", request.getMessageId(), request.getCategoryId());
    //         return ResponseEntity.badRequest().build();
    //     }else{
    //         log.info("messageReport Return : BAD_REQUEST, existed MessageReport / messageId = {}", request.getMessageId());
    //         return ResponseEntity.badRequest().build();
    //     }
    // }

    @Operation(summary = "메시지 신고", description = "메시지 신고 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상태 코드 응답"),
        @ApiResponse(responseCode = "404", description = "RC001: 신고 카테고리가 존재하지 않습니다."),
        @ApiResponse(responseCode = "404", description = "MG001: 메시지를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "409", description = "MR001: 이미 신고된 메시지가 존재합니다."),
    })
    @PostMapping("/message")
    public ResponseEntity<Void> messageReport(@RequestBody @Valid ReportRequest request){
        LocalDateTime messageReportDate = LocalDateTime.now();
        messageReportService.messageReport(request, messageReportDate);
        return ResponseEntity.ok().build();
    }

    // @PostMapping("/report/admin/delete")
    // public void deleteMessageReportAdmin(@RequestParam("id") Long id){
    //     log.info("deleteMessageReportAdmin : id = {}", id);
    //     messageReportService.deleteMessageReportAdmin(id);
    // }
    @Operation(summary = "신고 메시지 삭제", description = "신고 메시지 삭제 API")
    @PostMapping("/{messageReportId}/message/admin-delete")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상태 코드 응답"),
        @ApiResponse(responseCode = "404", description = "MR002: 신고된 메시지를 찾을 수 없습니다."),
    })
    public ResponseEntity<Void> deleteMessageReportAdmin(@PathVariable("messageReportId") Long messageReportId){
        messageReportService.deleteMessageReportAdmin(messageReportId);
        return ResponseEntity.ok().build();
    }

    // @PostMapping("/check/messageReport")
    // public ResponseEntity<StatusResponse> checkMessageReport(@RequestBody CheckMessageReportRequest request){
    //     log.info("checkMessageReport : memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //     StatusResponse check = messageReportService.checkMessageReport(request.getMemberId1(), request.getMemberId2());
    //     if(check.getStatus() == 0){
    //         log.warn("checkMessageReport Return : BAD_REQUEST, memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //         return ResponseEntity.ok(check);
    //     }
    //     else if(check.getStatus() == 1){
    //         log.info("checkMessageReport Return : BAD_REQUEST Reported, memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //         return ResponseEntity.ok(check);
    //     }else if(check.getStatus() == 2){
    //         log.info("checkMessageReport Return : BAD_REQUEST Report, memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //         return ResponseEntity.ok(check);
    //     }
    //     log.info("checkMessageReport Return : OK, memberId1 = {}, memberId2 = {}", request.getMemberId1(), request.getMemberId2());
    //     return ResponseEntity.ok(check);
    // }
    @Operation(summary = "채팅방 생성 전 신고된 메시지 확인", description = "채팅방 생성 전 신고된 메시지가 존재하는 사용자 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상태 코드 응답"),
        @ApiResponse(responseCode = "404", description = "M001: 사용자를 찾을 수 없습니다."),
    })
    @PostMapping("/message/check")
    public ResponseEntity<SuccessResponse<CheckMessageReportResponse>> checkMessageReport(@RequestBody @Valid CheckMessageReportRequest request){
        return SuccessResponse.of(messageReportService.checkMessageReport(request))
            .asHttp(HttpStatus.OK);
    }
}
