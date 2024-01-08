package com.mjuAppSW.joA.geography.location;

import static com.mjuAppSW.joA.common.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.common.constant.Constants.NORMAL_OPERATION;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.common.auth.MemberChecker;
import com.mjuAppSW.joA.domain.heart.Heart;
import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.geography.block.exception.LocationNotFoundException;
import com.mjuAppSW.joA.geography.college.PCollege;
import com.mjuAppSW.joA.geography.college.PCollegeRepository;
import com.mjuAppSW.joA.geography.location.dto.response.NearByInfo;
import com.mjuAppSW.joA.geography.location.dto.response.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.response.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.request.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.response.UpdateResponse;
import com.mjuAppSW.joA.geography.location.exception.CollegeNotFoundException;
import com.mjuAppSW.joA.geography.location.exception.OutOfCollegeException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;
    private final PCollegeRepository pCollegeRepository;
    private final HeartRepository heartRepository;
    private final MemberChecker memberChecker;

    @Transactional
    public UpdateResponse updateLocation(UpdateRequest request) {
        Member member = memberChecker.findBySessionId(request.getId());
        Location location = findLocationById(member.getId());
        PCollege college = findCollegeById(location.getCollege().getCollegeId());

        memberChecker.checkStopped(member);

        boolean isContained = isPointWithinPolygon
                (request.getLatitude(), request.getLongitude(), college.getPolygonField());

        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Location newLocation = new Location(location.getId(), location.getCollege(), point, isContained, LocalDate.now());
        locationRepository.save(newLocation);
        return UpdateResponse.of(isContained);
    }

    public NearByListResponse getNearByList(Long sessionId, Double latitude, Double longitude, Double altitude) {
        Member member = memberChecker.findBySessionId(sessionId);
        memberChecker.checkStopped(member);

        Location location = findLocationById(member.getId());

        checkWithinCollege(location);

        Point point = getPoint(latitude, longitude, altitude);
        List<Long> nearMemberIds = findNearIds(member.getId(), point, member.getCollege().getId());
        List<NearByInfo> nearByList = makeNearByList(member, nearMemberIds);
        return NearByListResponse.of(nearByList);
    }

    public OwnerResponse getOwner(Long sessionId) {
        Member findMember = memberChecker.findBySessionId(sessionId);
        return OwnerResponse.of(findMember);
    }

    private Location findLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow(LocationNotFoundException::new);
    }

    private PCollege findCollegeById(Long id) {
        return pCollegeRepository.findById(id).orElseThrow(CollegeNotFoundException::new);
    }

    private Point getPoint(double latitude, double longitude, double altitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude, altitude);
        return geometryFactory.createPoint(coordinate);
    }

    private Boolean isPointWithinPolygon(double latitude, double longitude, Polygon polygon) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);

        return polygon.contains(point);
    }

    private void checkWithinCollege(Location location) {
        if (!location.getIsContained()) {
            throw new OutOfCollegeException();
        }
    }

    private List<Long> findNearIds(Long id, Point point, Long collegeId) {
        return locationRepository.findNearIds(id, point, collegeId, LocalDate.now());
    }

    private List<NearByInfo> makeNearByList(Member member, List<Long> nearMemberIds) {
        List<NearByInfo> nearByList = nearMemberIds.stream()
                .map(nearId -> {
                    Member findMember = memberChecker.findById(nearId);
                    boolean isLiked = isEqualHeartExisted(member.getId(), nearId);
                    return NearByInfo.builder()
                            .id(findMember.getId())
                            .name(findMember.getName())
                            .urlCode(findMember.getUrlCode())
                            .bio(findMember.getBio())
                            .isLiked(isLiked)
                            .build();
                })
                .collect(Collectors.toList());

        return nearByList;
    }

    private Boolean isEqualHeartExisted(Long giveId, Long takeId) {
        return heartRepository.findEqualHeart(LocalDate.now(), giveId, takeId).isPresent();
    }
}
