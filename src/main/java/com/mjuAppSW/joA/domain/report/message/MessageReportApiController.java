package com.mjuAppSW.joA.domain.report.message;

import com.mjuAppSW.joA.domain.report.message.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MessageReportApiController {

    private MessageReportService messageReportService;

    @Autowired
    public MessageReportApiController(MessageReportService messageReportService){
        this.messageReportService = messageReportService;
    }

    @PostMapping("/report/message")
    public HttpStatus messageReport(@RequestBody ReportRequest request){
        log.info("messageReport : messageId = {}, categoryId = {}, content = {}", request.getMessageId(), request.getCategoryId(), request.getContent());
        Boolean save = messageReportService.messageReport(request.getMessageId(), request.getCategoryId(), request.getContent());
        if(save){
            log.info("messageReport Return : OK, success to report");
            return HttpStatus.OK;
        }
        log.warn("messageReport Return : BAD_REQUEST, getValue's not correct");
        log.warn("messageReport : messageId = {}, categoryId = {}", request.getMessageId(), request.getCategoryId());
        return HttpStatus.BAD_REQUEST;
    }
}
