package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.college.MCollege;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "Login_id", nullable = false)
    private String loginId;

    private String password;

    private String salt;

    @Column(name = "U_email")
    private String uEmail;

    @Column(length = 15)
    private String bio;

    @Column(name = "Url_code")
    private String urlCode;

    @Column(name = "Basic_profile")
    private Boolean basicProfile;

    private Boolean isWithdrawal;

    @Column(name = "Session_id")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "College_id")
    private MCollege college;

    @Column(name = "Report_count", nullable = false)
    private Integer reportCount;

    @Column(nullable = false)
    private Integer status;

    private LocalDate stopStartDate;

    private LocalDate stopEndDate;

    @Builder
    public Member(String name, String loginId, String password, String salt, String uEmail, MCollege college, Long sessionId) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.salt = salt;
        this.uEmail = uEmail;
        this.college = college;
        this.basicProfile = true;
        this.isWithdrawal = false;
        this.bio = "";
        this.urlCode = "";
        this.sessionId = sessionId;
        this.reportCount = 0;
        this.status = 0;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeBasicProfile(boolean basicProfile) {
        this.basicProfile = basicProfile;
    }

    public void changeWithdrawal(boolean withdrawal) {
        this.isWithdrawal = withdrawal;
    }

    public void expireSessionId() {
        this.sessionId = null;
    }

    public void makeSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void changeStopStartDate(LocalDate date) {
        this.stopStartDate = date;
    }

    public void changeStopEndDate(LocalDate date) {
        this.stopEndDate = date;
    }

    public void changeStatus(int status) {
        this.status = status;
    }

    public void addReportCount() {
        this.reportCount++;
    }
}
