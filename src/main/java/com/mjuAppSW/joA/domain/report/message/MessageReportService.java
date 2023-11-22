package com.mjuAppSW.joA.domain.report.message;


import com.mjuAppSW.joA.domain.message.Message;
import com.mjuAppSW.joA.domain.message.MessageRepository;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.report.ReportCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
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
        MessageReport check = messageReportRepository.findByMessage(message);
        if(check != null){return false;}
        if(message != null && reportCategory != null){
            MessageReport messageReport = MessageReport.builder()
                    .message_id(message)
                    .category_id(reportCategory)
                    .content(content)
                    .date(LocalDateTime.now())
                    .build();
            MessageReport saveMessageReport = messageReportRepository.save(messageReport);
            if(saveMessageReport != null) return true;
        }
        return false;
    }

    public void deleteMessageReportAdmin(Long id){
        MessageReport messageReport = messageReportRepository.findById(id).orElse(null);
        if(messageReport != null){
            log.info("deleteMessageReportAdmin : id = {}, message_id = {}, category_id = {}, content = {}, date = {}",
                    messageReport.getId(), messageReport.getMessage_id().getId(), messageReport.getCategory_id().getId(),
                    messageReport.getContent(), messageReport.getDate());
            messageReportRepository.delete(messageReport);
        }
    }

    public Long calculationHour(LocalDateTime getTime){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(getTime, currentDateTime);
        Long hours = duration.toHours();
        return hours;
    }

    @Scheduled(cron = "0 55 23 14,L * ?")
    public void deleteMessageReport(){
        log.info("23:55 1 or 15, deleteMessageReport");
        List<MessageReport> messageReports = messageReportRepository.findAll();
        List<MessageReport> deleteMessageReports = new ArrayList<>();
        for(MessageReport mr : messageReports){
            Long hours = calculationHour(mr.getDate());
            if(hours >= 2160){deleteMessageReports.add(mr);}
        }

        if(deleteMessageReports != null){
            for(MessageReport mr : deleteMessageReports){
                log.info("deleteMessageReport : id = {}, message_id = {}, category_id = {}, content = {}, date = {}",
                        mr.getId(), mr.getMessage_id().getId(), mr.getCategory_id().getId(), mr.getContent(), mr.getDate());
                messageReportRepository.delete(mr);
            }
        }
    }
}
