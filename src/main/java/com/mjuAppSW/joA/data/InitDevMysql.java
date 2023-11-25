package com.mjuAppSW.joA.data;

import com.mjuAppSW.joA.domain.college.MCollege;
import com.mjuAppSW.joA.domain.heart.Heart;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.vote.Vote;
import com.mjuAppSW.joA.domain.voteCategory.VoteCategory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class InitDevMysql {

    private final InitService initService;

    @PostConstruct
    public void init() {
//        initService.initCollege();
//        initService.initVoteCategory();
//        initService.initReportCategory();
//        initService.initMember();
//        initService.initHeart();
//        initService.initVote();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        MCollege c1, c2, c3;
        VoteCategory vc1, vc2, vc3, vc4, vc5, vc6, vc7, vc8, vc9;
        ReportCategory rc1, rc2, rc3;
        Member member1, member2, member3, member4, member5, member6, member7, member8, member9, member10;
        Heart heart1, heart2, heart3, heart4, heart5;
        Vote v1, v2, v3, v4, v5, v6, v7, v8, v9, v10;

        public void initCollege() {
            c1 = new MCollege(1L, "명지대학교", "@mju.ac.kr");
//            c2 = new MCollege(2L, "애플대학교", "@icloud.com");
//            c3 = new MCollege(3L, "네이버대학교", "@naver.com");
            em.persist(c1);
//            em.persist(c2);
//            em.persist(c3);
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

        public void initMember() {
            member1 = new Member( "최가의", "god11",
                    "$2a$10$0wosqw60V4BqMC55UDBtjOcAapLBqsRVhXYC5TV29Iy8AZxynbIrC",
                    "$2a$10$0wosqw60V4BqMC55UDBtjO",
                    "god", c1, null);
            member2 = new Member("한요한", "han22",
                    "$2a$10$yW8qjeUJVrJDouyT2yLAUe7eP3RiXNoTHgDJGY0XX58LHfXX9nHya",
                    "$2a$10$yW8qjeUJVrJDouyT2yLAUe",
                    "hyh", c1, null);
            member3 = new Member("한태산", "san33",
                    "$2a$10$S4Q9Wy1AZ6vQDGt.2vyQ9uzQYH6tsf8agBXMhck1EAKYy.ajvqrVW",
                    "$2a$10$S4Q9Wy1AZ6vQDGt.2vyQ9u",
                    "hts", c1,null);
            member4 = new Member("홍향미", "hong44",
                    "$2a$10$BvwjtS.cm3Zmm3JIyjHDd.Xe29nOCkBThflGO0zQa0yMMoaL/HUmW",
                    "$2a$10$BvwjtS.cm3Zmm3JIyjHDd.",
                    "hong", c1, null);
            member5 = new Member("최종현", "choi55",
                    "$2a$10$E/Ixrfm2UC1ltbkhzfgFPOFC5I0kDVrbMpyUrX3.iowUcKCPTJ9Ue",
                    "$2a$10$E/Ixrfm2UC1ltbkhzfgFPO",
                    "cjh", c1, null);
            member6 = new Member("강남순", "soon66",
                    "$2a$10$MCY7f8Ri73H85Y951DjxP.LPgbcpoW2ZnjaOU..2eL/XJ3xEbXrEi",
                    "$2a$10$MCY7f8Ri73H85Y951DjxP.",
                    "kns", c1, null);
            member7 = new Member("백승호", "hoo77",
                    "$2a$10$OdpYryOjnWysN3/YGrSUJ.iiG1aVHtavmdj2a9Y0sqYWoMJkeaN1e",
                    "$2a$10$OdpYryOjnWysN3/YGrSUJ.",
                    "psh", c1, null);
            member8 = new Member("이강인", "lee88",
                    "$2a$10$RTkmfOspvEU/EB73idrL9ePo5UtX4AYufFCz6xDeuDY5rkOlbXqd6",
                    "$2a$10$RTkmfOspvEU/EB73idrL9e",
                    "lki", c1, null);
            member9 = new Member("전정국", "kook99",
                    "$2a$10$Nvfd21BNo15MapBXmaFCAelmKf6Qk4eZhBCIVF8uxBvDvMVCBTmj6",
                    "$2a$10$Nvfd21BNo15MapBXmaFCAe",
                    "jjk", c1, null);
            member10 = new Member("안효섭", "ann1010",
                    "$2a$10$kTJ0m6ot/voRe5JzR.9GZeaHd32AOnyhY4Sgcltl3WXrNkBxi7vd.",
                    "$2a$10$kTJ0m6ot/voRe5JzR.9GZe",
                    "ahs", c1, null);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);
            em.persist(member6);
            em.persist(member7);
            em.persist(member8);
            em.persist(member9);
            em.persist(member10);
        }

        public void initHeart() {
            heart1 = new Heart(2L, member1, LocalDate.of(2023, 7, 25), false);
            heart2 = new Heart(2L, member1, LocalDate.now(), false);
            heart3 = new Heart(3L, member1, LocalDate.now(), false);
            heart4 = new Heart(4L, member1, LocalDate.now(), false);
            heart5 = new Heart(5L, member1, LocalDate.of(2023, 7, 25), false);

            em.persist(heart1);
            em.persist(heart2);
            em.persist(heart3);
            em.persist(heart4);
            em.persist(heart5);
        }

        public void initVote() {
            v1 = new Vote(2L, member1, vc1, LocalDate.of(2023, 7, 27), "나 누구게");
            v2 = new Vote(3L, member1, vc2, LocalDate.of(2023, 7, 27), "누나 안녕하세여");
            v3 = new Vote(4L, member1, vc3, LocalDate.of(2023, 7, 27), "캡스톤 같이 들으시죠");
            v4 = new Vote(5L, member1, vc4, LocalDate.of(2023, 7, 27), "가 의 님");
            v5 = new Vote(2L, member1, vc5, LocalDate.of(2023, 7, 27), "ㅎ2");
            v6 = new Vote(3L, member1, vc1, LocalDate.now(), "킥킷킥깃");
            v7 = new Vote(4L, member1, vc1, LocalDate.now(), "저 버리고 경정 전공으로 가지 마여");
            v8 = new Vote(5L, member1, vc2, LocalDate.now(), "웹프 에이스");
            v9 = new Vote(2L, member1, vc2, LocalDate.now(), "나 좋아한다고?");
            v10 = new Vote(3L, member1, vc3, LocalDate.now(), "");

            em.persist(v1);
            em.persist(v2);
            em.persist(v3);
            em.persist(v4);
            em.persist(v5);
            em.persist(v6);
            em.persist(v7);
            em.persist(v8);
            em.persist(v9);
            em.persist(v10);
        }
    }
}
