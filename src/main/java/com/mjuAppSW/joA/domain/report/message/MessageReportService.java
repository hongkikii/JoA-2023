package com.mjuAppSW.joA.domain.report.message;


import com.mjuAppSW.joA.domain.message.Message;
import com.mjuAppSW.joA.domain.message.MessageRepository;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.report.ReportCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageReportService {

    private MessageReportRepository messageReportRepository;
    private MessageRepository messageRepository;
    private ReportCategoryRepository reportCategoryRepository;

    @Autowired
    public MessageReportService(MessageReportRepository message_report_repository, MessageRepository messageRepository,
                                ReportCategoryRepository reportCategoryRepository){
        this.messageReportRepository = message_report_repository;
        this.messageRepository = messageRepository;
        this.reportCategoryRepository = reportCategoryRepository;
    }

    public boolean messageReport(Long messageId, Long categoryId, String content){
        Message message = messageRepository.findById(messageId).orElse(null);
        ReportCategory reportCategory = reportCategoryRepository.findById(categoryId).orElse(null);
        if(message != null && reportCategory != null){
            MessageReport messageReport = MessageReport.builder()
                    .message_id(message)
                    .category_id(reportCategory)
                    .content(content)
                    .date(new Date())
                    .build();
            messageReportRepository.save(messageReport);
            return true;
        }
        return false;
    }
}