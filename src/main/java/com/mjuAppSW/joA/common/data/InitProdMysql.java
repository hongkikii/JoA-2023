package com.mjuAppSW.joA.data;

import com.mjuAppSW.joA.domain.college.MCollege;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.vote.voteCategory.VoteCategory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class InitProdMysql {

    private final InitService initService;

    @PostConstruct
    public void init() {
//        initService.initCollege();
//        initService.initVoteCategory();
//        initService.initReportCategory();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        MCollege c1;
        VoteCategory vc1, vc2, vc3, vc4, vc5, vc6, vc7, vc8, vc9;
        ReportCategory rc1, rc2, rc3;

        public void initCollege() {
            c1 = new MCollege(1L, "명지대학교", "@mju.ac.kr");
            em.persist(c1);
        }

        public void initVoteCategory() {
            vc1 = new VoteCategory(1L, "선배님 밥 사주세요!");
            vc2 = new VoteCategory(2L, "혹시 3대 500?");
            vc3 = new VoteCategory(3L, "이 강의실의 패피는 바로 너");
            vc4 = new VoteCategory(4L, "페이커 뺨 칠 거 같음");
            vc5 = new VoteCategory(5L, "친해지고 싶어요");
            vc6 = new VoteCategory(6L, "과탑일 거 같아요");
            vc7 = new VoteCategory(7L, "팀플 같이 하고 싶어요");
            vc8 = new VoteCategory(8L, "끝나고 뭐 하는지 궁금해요");
            vc9 = new VoteCategory(9L, "존잘/존예이십미다");

            em.persist(vc1);
            em.persist(vc2);
            em.persist(vc3);
            em.persist(vc4);
            em.persist(vc5);
            em.persist(vc6);
            em.persist(vc7);
            em.persist(vc8);
            em.persist(vc9);
        }

        public void initReportCategory() {
            rc1 = new ReportCategory(1L, "욕설/비방");
            rc2 = new ReportCategory(2L, "성희롱");
            rc3 = new ReportCategory(3L, "기타");

            em.persist(rc1);
            em.persist(rc2);
            em.persist(rc3);
        }
    }
}
