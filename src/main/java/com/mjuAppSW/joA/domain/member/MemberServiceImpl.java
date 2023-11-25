package com.mjuAppSW.joA.domain.member;

import static com.mjuAppSW.joA.constant.Constants.Auth;
import static com.mjuAppSW.joA.constant.Constants.Auth.CERTIFY_NUMBER_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.Auth.LOGIN_ID_IS_CACHED;
import static com.mjuAppSW.joA.constant.Constants.Auth.LOGIN_ID_IS_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.Auth.LOGIN_ID_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.Auth.MAIL_IS_NOT_CACHED;
import static com.mjuAppSW.joA.constant.Constants.Auth.SESSION_ID_IS_NOT_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.Cache.AFTER_CERTIFY_TIME;
import static com.mjuAppSW.joA.constant.Constants.Cache.AFTER_EMAIL;
import static com.mjuAppSW.joA.constant.Constants.Cache.AFTER_SAVE_LOGIN_ID_TIME;
import static com.mjuAppSW.joA.constant.Constants.Cache.BEFORE_CERTIFY_TIME;
import static com.mjuAppSW.joA.constant.Constants.Cache.BEFORE_EMAIL;
import static com.mjuAppSW.joA.constant.Constants.Cache.CERTIFY_NUMBER;
import static com.mjuAppSW.joA.constant.Constants.Cache.ID;
import static com.mjuAppSW.joA.constant.Constants.EMAIL_SPLIT;
import static com.mjuAppSW.joA.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.constant.Constants.FindId;
import static com.mjuAppSW.joA.constant.Constants.Join;
import static com.mjuAppSW.joA.constant.Constants.Login.LOGIN_ID_IS_NOT_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.Login.LOGIN_IS_ALREADY;
import static com.mjuAppSW.joA.constant.Constants.Login.PASSWORD_IS_NOT_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.Logout.MEMBER_IS_NOT_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.MAIL.CERTIFY_NUMBER_IS;
import static com.mjuAppSW.joA.constant.Constants.MAIL.TEMPORARY_PASSWORD_IS;
import static com.mjuAppSW.joA.constant.Constants.MAIL.USER_ID_IS;
import static com.mjuAppSW.joA.constant.Constants.Member.COLLEGE_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.Member.MAIL_IS_USING;
import static com.mjuAppSW.joA.constant.Constants.Member.MEMBER_IS_EXISTED;
import static com.mjuAppSW.joA.constant.Constants.NORMAL_OPERATION;
import static com.mjuAppSW.joA.constant.Constants.S3Uploader.S3_DELETE_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.TransPassword;
import static com.mjuAppSW.joA.constant.Constants.TransPassword.PASSWORD_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.TransPassword.PASSWORD_IS_NOT_EQUAL;
import static com.mjuAppSW.joA.constant.Constants.Withdrawal;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.domain.college.MCollege;
import com.mjuAppSW.joA.domain.college.MCollegeRepository;
import com.mjuAppSW.joA.domain.member.dto.FindIdRequest;
import com.mjuAppSW.joA.domain.member.dto.FindPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.IdRequest;
import com.mjuAppSW.joA.domain.member.dto.JoinRequest;
import com.mjuAppSW.joA.domain.member.dto.LoginRequest;
import com.mjuAppSW.joA.domain.member.dto.LoginResponse;
import com.mjuAppSW.joA.domain.member.dto.SessionIdRequest;
import com.mjuAppSW.joA.domain.member.dto.StatusResponse;
import com.mjuAppSW.joA.domain.member.dto.TransPasswordRequest;
import com.mjuAppSW.joA.domain.member.dto.UMailRequest;
import com.mjuAppSW.joA.domain.member.dto.UMailResponse;
import com.mjuAppSW.joA.domain.member.dto.UNumRequest;
import com.mjuAppSW.joA.geography.college.PCollege;
import com.mjuAppSW.joA.geography.college.PCollegeRepository;
import com.mjuAppSW.joA.geography.location.Location;
import com.mjuAppSW.joA.geography.location.LocationRepository;
import com.mjuAppSW.joA.session.SessionManager;
import com.mjuAppSW.joA.storage.CacheManager;
import com.mjuAppSW.joA.storage.S3Uploader;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Optional;
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
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final MCollegeRepository mCollegeRepository;
    private final PCollegeRepository pCollegeRepository;
    private final LocationRepository locationRepository;
    private final SessionManager sessionManager;
    private final JavaMailSender javaMailSender;
    private final CacheManager cacheManager;
    private final S3Uploader s3Uploader;

    public UMailResponse sendCertifyNum(UMailRequest request) {
        MCollege college = findByMCollegeId(request.getCollegeId());
        if (isNull(college))
            return new UMailResponse(COLLEGE_IS_INVALID);

        if(isExistedMember(request.getUEmail(), college))
            return new UMailResponse(MEMBER_IS_EXISTED);

        String eMail = request.getUEmail() + college.getDomain();
        if(isUsingMail(eMail))
            return new UMailResponse(MAIL_IS_USING);

        long sessionId = sessionManager.makeSessionId();
        String certifyNum = cacheCertifyNumAndEmail(sessionId, eMail);
        sendCertifyNumMail(request.getUEmail(), college.getDomain(), certifyNum);
        return new UMailResponse(NORMAL_OPERATION, sessionId);
    }

    public StatusResponse authCertifyNum(UNumRequest request) {
        MCollege college = findByMCollegeId(request.getCollegeId());
        if (isNull(college))
            return new StatusResponse(Auth.COLLEGE_IS_NOT_EXISTED);

        Long sessionId = request.getId();
        if (isNotExistedCacheKey(CERTIFY_NUMBER+sessionId))
            return new StatusResponse(SESSION_ID_IS_NOT_EXISTED);

        String mail = request.getUEmail() + college.getDomain();
        if(!isEqualCacheValue(BEFORE_EMAIL + sessionId, mail))
            return new StatusResponse(MAIL_IS_NOT_CACHED);

        if (isEqualCacheValue(CERTIFY_NUMBER + sessionId, request.getCertifyNum())) {
            cacheEmailOnly(sessionId);
            return new StatusResponse(NORMAL_OPERATION);
        }

        return new StatusResponse(CERTIFY_NUMBER_IS_INVALID);
    }

    public StatusResponse verifyId(Long sessionId, String loginId) {
        if (isNotValidLoginId(loginId))
            return new StatusResponse(LOGIN_ID_IS_INVALID);

        if (isNotExistedCacheKey(AFTER_EMAIL + sessionId))
            return new StatusResponse(SESSION_ID_IS_NOT_EXISTED);

        if (!isNull(findByLoginId(loginId)))
            return new StatusResponse(LOGIN_ID_IS_EXISTED);

        if(isCachedLoginId(loginId)) {
            if (isMyJoiningId(sessionId, loginId))
                return new StatusResponse(NORMAL_OPERATION);
            return new StatusResponse(LOGIN_ID_IS_CACHED);
        }

        cacheLoginId(sessionId, loginId);
        return new StatusResponse(NORMAL_OPERATION);
    }

    @Transactional
    public StatusResponse join(JoinRequest request) {
        if (isNotValidPassword(request.getPassword()))
            return new StatusResponse(Join.PASSWORD_IS_INVALID);

        Long sessionId = request.getId();
        if(isNotExistedCacheKey(AFTER_EMAIL+sessionId))
            return new StatusResponse(Join.SESSION_ID_IS_NOT_EXISTED);

        String loginId = request.getLoginId();
        if(isNotCachedLoginId(sessionId, request.getLoginId()))
            return new StatusResponse(Join.LOGIN_ID_IS_NOT_CACHED);

        String eMail = cacheManager.getData(AFTER_EMAIL + sessionId);
        String[] splitEMail = eMail.split(EMAIL_SPLIT);
        String uEmail = splitEMail[0];
        MCollege mCollege = findByDomain(splitEMail[1]);

        PCollege pCollege = findByPCollegeId(mCollege.getId());
        if(isNull(pCollege))
            return new StatusResponse(Join.COLLEGE_IS_INVALID);

        if (isForbiddenMail(uEmail, mCollege))
            return new StatusResponse(Join.MAIL_IS_FORBIDDEN);

        // 비밀번호 암호화
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
        return new StatusResponse(NORMAL_OPERATION);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member findMember = findByLoginId(request.getLoginId());
        if (isNull(findMember))
            return new LoginResponse(LOGIN_ID_IS_NOT_EXISTED);

        findMember.makeSessionId(sessionManager.makeSessionId());
        // 비밀번호 암호화
        String salt = findMember.getSalt();
        String hashedPassword = BCrypt.hashpw(request.getPassword(), salt);

        if (findMember.getPassword().equals(hashedPassword))
            return new LoginResponse(NORMAL_OPERATION, findMember.getSessionId());

        return new LoginResponse(PASSWORD_IS_NOT_EXISTED);
    }

    @Transactional
    public StatusResponse logout(IdRequest request) {
        Member findMember = sessionManager.findBySessionId(request.getId());
        if(isNull(findMember))
            return new StatusResponse(MEMBER_IS_NOT_EXISTED);

        Location location = locationRepository.findById(findMember.getId()).orElse(null);
        if(!isNull(location)) {
            location.changeIsContained(false);
        }
        findMember.expireSessionId();
        return new StatusResponse(NORMAL_OPERATION);
    }

    @Transactional
    public StatusResponse findId(FindIdRequest request) {
        MCollege college = findByMCollegeId(request.getCollegeId());
        if (isNull(college))
            return new StatusResponse(FindId.COLLEGE_IS_NOT_EXISTED);

        Member member = findByUEmailAndCollege(request.getUEmail(), college);
        if (isNull(member))
            return new StatusResponse(FindId.MEMBER_IS_NOT_EXISTED);

        mail(USER_ID_IS, member.getName(), member.getUEmail(), college.getDomain(), member.getLoginId());
        return new StatusResponse(NORMAL_OPERATION);
    }

    @Transactional
    public StatusResponse findPassword(FindPasswordRequest request) {
        Member member = findByLoginId(request.getLoginId());
        if (isNull(member))
            return new StatusResponse(MEMBER_IS_NOT_EXISTED);

        // 비밀번호 암호화
        String randomPassword = randomPassword();
        String hashedRandomPassword = BCrypt.hashpw(randomPassword, member.getSalt());
        mail(TEMPORARY_PASSWORD_IS, member.getName(), member.getUEmail(),
                member.getCollege().getDomain(), randomPassword);
        member.changePassword(hashedRandomPassword);
        return new StatusResponse(NORMAL_OPERATION);
    }

    @Transactional
    public StatusResponse transPassword(TransPasswordRequest request) {
        Member findMember = sessionManager.findBySessionId(request.getId());
        if (isNull(findMember))
            return new StatusResponse(TransPassword.MEMBER_IS_NOT_EXISTED);

        // 비밀번호 암호화
        String hashedCurrentPassword = BCrypt.hashpw(request.getCurrentPassword(), findMember.getSalt());
        if (findMember.getPassword().equals(hashedCurrentPassword)) {
            if (isNotValidPassword(request.getNewPassword()))
                return new StatusResponse(PASSWORD_IS_INVALID);

            // 비밀번호 암호화
            String hashedNewPassword = BCrypt.hashpw(request.getNewPassword(), findMember.getSalt());
            findMember.changePassword(hashedNewPassword);
            return new StatusResponse(NORMAL_OPERATION);
        }
        return new StatusResponse(PASSWORD_IS_NOT_EQUAL);
    }

    @Transactional
    public StatusResponse withdrawal(SessionIdRequest request) {
        Long sessionId = request.getId();
        Member member = sessionManager.findBySessionId(sessionId);
        if (isNull(member))
            return new StatusResponse(Withdrawal.MEMBER_IS_NOT_EXISTED);

        if (s3Uploader.deletePicture(member.getUrlCode())) {
            locationRepository.deleteById(member.getId());
            member.expireSessionId();
            member.changeWithdrawal(true);
            member.changeBasicProfile(true);
            member.changeUrlCode(EMPTY_STRING);
            return new StatusResponse(NORMAL_OPERATION);
        }
        return new StatusResponse(S3_DELETE_IS_INVALID);
    }

    private MCollege findByMCollegeId(Long collegeId) {
        return mCollegeRepository.findById(collegeId).orElse(null);
    }

    private Boolean isExistedMember(String uEmail, MCollege college) {
        if(isNull(findByUEmailAndCollege(uEmail, college)))
            return false;
        return true;
    }

    private Boolean isUsingMail(String eMail) {
        boolean isCertifyingMail = isExistedInCache(BEFORE_EMAIL, eMail);
        boolean isJoiningMail = isExistedInCache(AFTER_EMAIL, eMail);
        if(isJoiningMail || isCertifyingMail)
            return true;
        return false;
    }

    private String cacheCertifyNumAndEmail(Long sessionId, String totalEmail) {
        String certifyNum = cacheManager.addRandomValue(CERTIFY_NUMBER + sessionId, BEFORE_CERTIFY_TIME);
        cacheManager.add(BEFORE_EMAIL + sessionId, totalEmail, BEFORE_CERTIFY_TIME);
        return certifyNum;
    }

    private void sendCertifyNumMail(String uEmail, String domain, String certifyNum) {
        mail(CERTIFY_NUMBER_IS, null, uEmail, domain, certifyNum);
    }

    private Boolean isEqualCacheValue(String key, String value) {
        return cacheManager.compare(key, value);
    }

    private void cacheEmailOnly(Long sessionId) {
        cacheManager.delete(CERTIFY_NUMBER + sessionId);
        String Email = cacheManager.delete(BEFORE_EMAIL + sessionId);
        cacheManager.add(AFTER_EMAIL + sessionId, Email, AFTER_CERTIFY_TIME);
    }

    private boolean isNotValidLoginId(String id) {
        if (id.length() < 5 || id.length() > 20)
            return true;
        String regex = "^[a-z0-9-_]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return !matcher.matches();
    }

    private boolean isNotExistedCacheKey(String key) {
        return cacheManager.isNotExistedKey(key);
    }

    private static boolean isNotValidPassword(String password) {
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=]).{8,16}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(password);
        return !matcher.matches();
    }

    private Boolean isMyJoiningId(Long sessionId, String loginId) {
        return cacheManager.compare(ID + sessionId, loginId);
    }

    private Boolean isCachedLoginId(String loginId) {
        return cacheManager.isExistedValue(ID, loginId);
    }

    private void cacheLoginId(Long sessionId, String loginId) {
        cacheManager.add(ID + sessionId, loginId, AFTER_SAVE_LOGIN_ID_TIME);
        cacheManager.changeTime(AFTER_EMAIL + sessionId, AFTER_SAVE_LOGIN_ID_TIME);
    }

    private Boolean isNotCachedLoginId(Long sessionId, String loginId) {
        return !cacheManager.compare(ID + sessionId, loginId);
    }

    private MCollege findByDomain(String domain) {
        return mCollegeRepository.findBydomain(EMAIL_SPLIT + domain).orElse(null);
    }

    private PCollege findByPCollegeId(Long collegeId) {
        return pCollegeRepository.findById(collegeId).orElse(null);
    }

    private boolean isForbiddenMail(String uEmail, MCollege mCollege) {
        Optional<Member> forbiddenMember = memberRepository.findForbiddenMember(uEmail, mCollege);
        if (forbiddenMember.isPresent()) {
            return true;
        }
        return false;
    }

    private void emptyCache(Long sessionId) {
        cacheManager.delete(ID + sessionId);
        cacheManager.delete(AFTER_EMAIL + sessionId);
    }

    private void mail(String header, String memberName, String uEmail, String collegeDomain, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(uEmail + collegeDomain);

        if (isNull(memberName)) {
            message.setSubject("[JoA] " + header + "를 확인하세요.");
            message.setText(header + "는 " + content + " 입니다.");
        }
        else {
            message.setSubject("[JoA] " + memberName + "님의 " + header + "를 확인하세요.");
            message.setText(memberName + "님의 " + header + "는 " + content + " 입니다.");
        }
        javaMailSender.send(message);
    }

    private Boolean isExistedInCache(String startwith, String checkValue) {
        return cacheManager.isExistedValue(startwith, checkValue);
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

    private Member findByUEmailAndCollege(String uEmail, MCollege college) {
        Member member = memberRepository.findByuEmailAndcollege(uEmail, college).orElse(null);
        if (isNull(member))
            return null;
        return member;
    }

    private Member findByLoginId(String loginId) {
        Member member = memberRepository.findByloginId(loginId).orElse(null);
        if (isNull(member))
            return null;
        return member;
    }
}
