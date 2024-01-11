package com.mjuAppSW.joA.domain.member;

import static com.mjuAppSW.joA.common.constant.Constants.Cache.AFTER_CERTIFY_TIME;
import static com.mjuAppSW.joA.common.constant.Constants.Cache.AFTER_EMAIL;
import static com.mjuAppSW.joA.common.constant.Constants.Cache.AFTER_SAVE_LOGIN_ID_TIME;
import static com.mjuAppSW.joA.common.constant.Constants.Cache.BEFORE_CERTIFY_TIME;
import static com.mjuAppSW.joA.common.constant.Constants.Cache.BEFORE_EMAIL;
import static com.mjuAppSW.joA.common.constant.Constants.Cache.CERTIFY_NUMBER;
import static com.mjuAppSW.joA.common.constant.Constants.Cache.ID;
import static com.mjuAppSW.joA.common.constant.Constants.EMAIL_SPLIT;
import static com.mjuAppSW.joA.common.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.common.constant.Constants.MAIL.CERTIFY_NUMBER_IS;
import static com.mjuAppSW.joA.common.constant.Constants.MAIL.TEMPORARY_PASSWORD_IS;
import static com.mjuAppSW.joA.common.constant.Constants.MAIL.USER_ID_IS;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.common.auth.MemberChecker;
import com.mjuAppSW.joA.domain.college.MCollege;
import com.mjuAppSW.joA.domain.college.MCollegeRepository;
import com.mjuAppSW.joA.domain.member.dto.request.FindIdRequest;
import com.mjuAppSW.joA.domain.member.dto.request.FindPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.request.JoinRequest;
import com.mjuAppSW.joA.domain.member.dto.request.LoginRequest;
import com.mjuAppSW.joA.domain.member.dto.request.TransPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.request.SendCertifyNumRequest;
import com.mjuAppSW.joA.domain.member.dto.response.SessionIdResponse;
import com.mjuAppSW.joA.domain.member.dto.request.VerifyCertifyNumRequest;
import com.mjuAppSW.joA.domain.member.exception.InvalidCertifyNumberException;
import com.mjuAppSW.joA.domain.member.exception.InvalidLoginIdException;
import com.mjuAppSW.joA.domain.member.exception.InvalidPasswordException;
import com.mjuAppSW.joA.domain.member.exception.JoiningMailException;
import com.mjuAppSW.joA.domain.member.exception.LoginIdAlreadyExistedException;
import com.mjuAppSW.joA.domain.member.exception.LoginIdNotAuthorizedException;
import com.mjuAppSW.joA.domain.member.exception.MailForbiddenException;
import com.mjuAppSW.joA.domain.member.exception.MailNotVerifyException;
import com.mjuAppSW.joA.domain.member.exception.MemberAlreadyExistedException;
import com.mjuAppSW.joA.domain.member.exception.PasswordNotFoundException;
import com.mjuAppSW.joA.domain.member.exception.SessionNotFoundException;
import com.mjuAppSW.joA.domain.memberProfile.exception.MemberNotFoundException;
import com.mjuAppSW.joA.domain.memberProfile.exception.S3InvalidException;
import com.mjuAppSW.joA.geography.college.PCollege;
import com.mjuAppSW.joA.geography.college.PCollegeRepository;
import com.mjuAppSW.joA.geography.location.Location;
import com.mjuAppSW.joA.geography.location.LocationRepository;
import com.mjuAppSW.joA.common.session.SessionManager;
import com.mjuAppSW.joA.common.storage.CacheManager;
import com.mjuAppSW.joA.common.storage.S3Uploader;
import com.mjuAppSW.joA.geography.location.exception.CollegeNotFoundException;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MCollegeRepository mCollegeRepository;
    private final PCollegeRepository pCollegeRepository;
    private final LocationRepository locationRepository;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;
    private final CacheManager cacheManager;
    private final S3Uploader s3Uploader;
    private final MemberChecker memberChecker;

    public SessionIdResponse sendCertifyNum(SendCertifyNumRequest request) {
        MCollege college = findByMCollegeId(request.getCollegeId());
        checkExistedMember(request.getUEmail(), college);

        String eMail = request.getUEmail() + college.getDomain();
        checkJoiningMail(eMail);

        long sessionId = sessionManager.makeSessionId();
        String certifyNum = cacheCertifyNumAndEmail(sessionId, eMail);
        sendCertifyNumMail(request.getUEmail(), college.getDomain(), certifyNum);
        return SessionIdResponse.of(sessionId);
    }

    private MCollege findByMCollegeId(Long collegeId) {
        return mCollegeRepository.findById(collegeId).orElseThrow(CollegeNotFoundException::new);
    }

    private void checkExistedMember(String uEmail, MCollege college) {
        if (memberRepository.findByuEmailAndcollege(uEmail, college).isPresent()) {
            throw new MemberAlreadyExistedException();
        }
    }

    private void checkJoiningMail(String eMail) {
        if (cacheManager.isExistedValue(BEFORE_EMAIL, eMail) || cacheManager.isExistedValue(AFTER_EMAIL, eMail)) {
            throw new JoiningMailException();
        }
    }

    private String cacheCertifyNumAndEmail(Long sessionId, String totalEmail) {
        String certifyNum = cacheManager.addRandomValue(CERTIFY_NUMBER + sessionId, BEFORE_CERTIFY_TIME);
        cacheManager.add(BEFORE_EMAIL + sessionId, totalEmail, BEFORE_CERTIFY_TIME);
        return certifyNum;
    }

    private void sendCertifyNumMail(String uEmail, String domain, String certifyNum) {
        mail(CERTIFY_NUMBER_IS, null, uEmail, domain, certifyNum);
    }

    private void mail(String header, String memberName, String uEmail, String collegeDomain, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(uEmail + collegeDomain);

        if (isNull(memberName)) {
            message.setSubject("[JoA] " + header + "를 확인하세요.");
            message.setText(header + "는 " + content + " 입니다.");
        } else {
            message.setSubject("[JoA] " + memberName + "님의 " + header + "를 확인하세요.");
            message.setText(memberName + "님의 " + header + "는 " + content + " 입니다.");
        }
        javaMailSender.send(message);
    }

    public void verifyCertifyNum(VerifyCertifyNumRequest request) {
        MCollege college = findByMCollegeId(request.getCollegeId());
        Long sessionId = request.getId();
        if(cacheManager.isNotExistedKey(CERTIFY_NUMBER + sessionId)) {
            throw new SessionNotFoundException();
        }
        String mail = request.getUEmail() + college.getDomain();
        if (!cacheManager.compare(BEFORE_EMAIL + sessionId, mail)) {
            throw new MailNotVerifyException();
        }
        if (!cacheManager.compare(CERTIFY_NUMBER + sessionId, request.getCertifyNum())) {
            throw new InvalidCertifyNumberException();
        }
        cacheEmailOnly(sessionId);
    }

    private void cacheEmailOnly(Long sessionId) {
        cacheManager.delete(CERTIFY_NUMBER + sessionId);
        String Email = cacheManager.delete(BEFORE_EMAIL + sessionId);
        cacheManager.add(AFTER_EMAIL + sessionId, Email, AFTER_CERTIFY_TIME);
    }

    public void verifyId(Long sessionId, String loginId) {
        if (isNotValidLoginId(loginId)) {
            throw new InvalidLoginIdException();
        }
        if (isNotExistedCacheKey(AFTER_EMAIL + sessionId)) {
            throw new MailNotVerifyException();
        }
        checkExistedLoginId(loginId);

        if (isCachedLoginId(loginId) && !isMyJoiningId(sessionId, loginId)) {
            throw new LoginIdAlreadyExistedException();
        }
        cacheLoginId(sessionId, loginId);
    }

    private boolean isNotValidLoginId(String id) {
        if (id.length() < 5 || id.length() > 20) {
            return true;
        }
        String regex = "^[a-z0-9-_]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return !matcher.matches();
    }

    private boolean isNotExistedCacheKey(String key) {
        return cacheManager.isNotExistedKey(key);
    }

    private void checkExistedLoginId(String loginId) {
        memberRepository.findByloginId(loginId).ifPresent(existingMember -> {
            throw new LoginIdAlreadyExistedException();});
    }

    private Boolean isCachedLoginId(String loginId) {
        return cacheManager.isExistedValue(ID, loginId);
    }

    private Boolean isMyJoiningId(Long sessionId, String loginId) {
        return cacheManager.compare(ID + sessionId, loginId);
    }

    private void cacheLoginId(Long sessionId, String loginId) {
        cacheManager.add(ID + sessionId, loginId, AFTER_SAVE_LOGIN_ID_TIME);
        cacheManager.changeTime(AFTER_EMAIL + sessionId, AFTER_SAVE_LOGIN_ID_TIME);
    }

    @Transactional
    public void join(JoinRequest request) {
        if (isNotValidPassword(request.getPassword())) {
            throw new InvalidPasswordException();
        }
        Long sessionId = request.getId();
        if (isNotExistedCacheKey(AFTER_EMAIL + sessionId)) {
            throw new SessionNotFoundException();
        }
        String loginId = request.getLoginId();
        if (isNotCachedLoginId(sessionId, request.getLoginId())) {
            throw new LoginIdNotAuthorizedException();
        }
        String eMail = cacheManager.getData(AFTER_EMAIL + sessionId);
        String[] splitEMail = eMail.split(EMAIL_SPLIT);
        String uEmail = splitEMail[0];
        MCollege mCollege = findByDomain(splitEMail[1]);
        PCollege pCollege = findByPCollegeId(mCollege.getId());

        if (isForbiddenMail(uEmail, mCollege)) {
            throw new MailForbiddenException();
        }
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(request.getPassword(), salt);

        Member joinMember = Member.builder()
                                    .name(request.getName())
                                    .loginId(loginId)
                                    .password(hashedPassword)
                                    .salt(salt)
                                    .uEmail(splitEMail[0])
                                    .college(mCollege)
                                    .sessionId(sessionId)
                                    .build();
        memberRepository.save(joinMember);

        Location joinLocation = new Location(joinMember.getId(), pCollege);
        locationRepository.save(joinLocation);
        emptyCache(sessionId);
    }

    private static boolean isNotValidPassword(String password) {
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=]).{8,16}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(password);
        return !matcher.matches();
    }

    private Boolean isNotCachedLoginId(Long sessionId, String loginId) {
        return !cacheManager.compare(ID + sessionId, loginId);
    }

    private MCollege findByDomain(String domain) {
        return mCollegeRepository.findBydomain(EMAIL_SPLIT + domain).orElse(null);
    }

    private PCollege findByPCollegeId(Long collegeId) {
        return pCollegeRepository.findById(collegeId).orElseThrow(CollegeNotFoundException::new);
    }

    private boolean isForbiddenMail(String uEmail, MCollege mCollege) {
        return memberRepository.findForbiddenMember(uEmail, mCollege).isPresent();
    }

    private void emptyCache(Long sessionId) {
        cacheManager.delete(ID + sessionId);
        cacheManager.delete(AFTER_EMAIL + sessionId);
    }

    @Transactional
    public SessionIdResponse login(LoginRequest request) {
        Member findMember = memberChecker.findByLoginId(request.getLoginId());
        findMember.makeSessionId(sessionManager.makeSessionId());
        String hashedPassword = BCrypt.hashpw(request.getPassword(), findMember.getSalt());

        if (!findMember.getPassword().equals(hashedPassword)) {
            throw new PasswordNotFoundException();
        }
        return SessionIdResponse.of(findMember.getSessionId());
    }

    @Transactional
    public void logout(Long sessionId) {
        Member findMember = memberChecker.findBySessionId(sessionId);
        locationRepository.findById(findMember.getId())
                .ifPresent(location -> {
                    locationRepository.save(new Location(location.getId(), location.getCollege(),
                            location.getPoint(), false, location.getUpdateDate()));
                });
        findMember.expireSessionId();
    }

    public void findId(FindIdRequest request) {
        MCollege college = findByMCollegeId(request.getCollegeId());

        Member member = memberRepository.findByuEmailAndcollege(request.getUEmail(), college)
                .orElseThrow(MemberNotFoundException::new);

        mail(USER_ID_IS, member.getName(), member.getUEmail(), college.getDomain(), member.getLoginId());
    }

    @Transactional
    public void findPassword(FindPasswordRequest request) {
        Member member = memberChecker.findByLoginId(request.getLoginId());

        String randomPassword = randomPassword();
        String hashedRandomPassword = BCrypt.hashpw(randomPassword, member.getSalt());

        mail(TEMPORARY_PASSWORD_IS, member.getName(), member.getUEmail(),
                member.getCollege().getDomain(), randomPassword);
        member.changePassword(hashedRandomPassword);
    }

    private String randomPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";

        String allCharacters = upper + lower + digits + specialChars;
        int maxLength = 16;
        Random random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        while (builder.length() < maxLength) {
            int index = random.nextInt(allCharacters.length());
            char randomChar = allCharacters.charAt(index);
            builder.append(randomChar);
        }
        return builder.toString();
    }

    @Transactional
    public void transPassword(TransPasswordRequest request) {
        Member findMember = memberChecker.findBySessionId(request.getId());

        String hashedCurrentPassword = BCrypt.hashpw(request.getCurrentPassword(), findMember.getSalt());
        if (findMember.getPassword().equals(hashedCurrentPassword)) {
            if (isNotValidPassword(request.getNewPassword())) {
                throw new InvalidPasswordException();
            }
            String hashedNewPassword = BCrypt.hashpw(request.getNewPassword(), findMember.getSalt());
            findMember.changePassword(hashedNewPassword);
            return;
        }
        throw new PasswordNotFoundException();
    }

    @Transactional
    public void withdrawal(Long sessionId) {
        Member member = memberChecker.findBySessionId(sessionId);

        if (s3Uploader.deletePicture(member.getUrlCode())) {
            locationRepository.deleteById(member.getId());
            member.expireSessionId();
            member.changeWithdrawal(true);
            member.changeUrlCode(EMPTY_STRING);
            return;
        }
        throw new S3InvalidException();
    }
}
