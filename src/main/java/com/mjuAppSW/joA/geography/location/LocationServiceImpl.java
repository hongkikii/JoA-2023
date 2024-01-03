package com.mjuAppSW.joA.geography.location;

import static com.mjuAppSW.joA.constant.Constants.EMPTY_STRING;
import static com.mjuAppSW.joA.constant.Constants.GeographyInfo;
import static com.mjuAppSW.joA.constant.Constants.GeographyList;
import static com.mjuAppSW.joA.constant.Constants.GeographyUpdate.COLLEGE_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.GeographyUpdate.MEMBER_IS_INVALID;
import static com.mjuAppSW.joA.constant.Constants.GeographyUpdate.MEMBER_IS_STOPPED;
import static com.mjuAppSW.joA.constant.Constants.NORMAL_OPERATION;
import static java.util.Objects.isNull;

import com.mjuAppSW.joA.domain.heart.Heart;
import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberAccessor;
import com.mjuAppSW.joA.geography.block.Block;
import com.mjuAppSW.joA.geography.block.BlockRepository;
import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.dto.StatusResponse;
import com.mjuAppSW.joA.geography.college.PCollege;
import com.mjuAppSW.joA.geography.college.PCollegeRepository;
import com.mjuAppSW.joA.geography.location.dto.NearByInfo;
import com.mjuAppSW.joA.geography.location.dto.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final PCollegeRepository pCollegeRepository;
    private final BlockRepository blockRepository;
    private final MemberAccessor memberAccessor;
    private final HeartRepository heartRepository;

    @Transactional
    public UpdateResponse updateLocation(UpdateRequest request) {
        Member member = memberAccessor.findBySessionId(request.getId());
        if(isNull(member))
            return new UpdateResponse(MEMBER_IS_INVALID, null);

        Location location = findLocationById(member.getId());
        if(isNull(location))
            return new UpdateResponse(MEMBER_IS_INVALID, null);

        PCollege college = findCollegeById(location.getCollege().getCollegeId());
        if(isNull(college))
            return new UpdateResponse(COLLEGE_IS_INVALID, null);

        if (memberAccessor.isStopped(member.getSessionId()))
            return new UpdateResponse(MEMBER_IS_STOPPED, null);

        boolean isContained = isPointWithinPolygon
                (request.getLatitude(), request.getLongitude(), college.getPolygonField());

        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Location newLocation = new Location(location.getId(), location.getCollege(), point, isContained, LocalDate.now());
        locationRepository.save(newLocation);
        return new UpdateResponse(NORMAL_OPERATION, isContained);
    }

    public NearByListResponse getNearByList(UpdateRequest request) {
        Member member = memberAccessor.findBySessionId(request.getId());
        if(isNull(member))
            return new NearByListResponse(GeographyList.MEMBER_IS_INVALID);

        if (memberAccessor.isStopped(member.getSessionId()))
            return new NearByListResponse(GeographyList.MEMBER_IS_STOPPED);

        Location location = findLocationById(member.getId());
        if(isNull(location))
            return new NearByListResponse(GeographyList.MEMBER_GEOGRAPHY_IS_INVALID);

        if(isNotWithinCollege(location))
            return new NearByListResponse(GeographyList.MEMBER_IS_OUT_OF_COLLEGE);

        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        List<Long> nearMemberIds = findNearIds(member.getId(), point, member.getCollege().getId());
        List<NearByInfo> nearByList = makeNearByList(member, nearMemberIds);
        return new NearByListResponse(NORMAL_OPERATION, nearByList);
    }

    public OwnerResponse getOwnerInfo(Long id) {
        Member findMember = memberAccessor.findBySessionId(id);
        if(isNull(findMember))
            return OwnerResponse.builder()
                                .status(GeographyInfo.MEMBER_IS_INVALID)
                                .build();

        return OwnerResponse.builder()
                            .status(NORMAL_OPERATION)
                            .name(findMember.getName())
                            .urlCode(findMember.getUrlCode())
                            .bio(findMember.getBio())
                            .build();
    }

    @Transactional
    public StatusResponse execute(BlockRequest request) {
        Member blockerMember = memberAccessor.findBySessionId(request.getBlockerId());
        if (isNull(blockerMember)) {
            return new StatusResponse(1);
        }
        Location blockerLocation = locationRepository.findById(blockerMember.getId()).orElse(null);
        Location blockedLocation = locationRepository.findById(request.getBlockedId()).orElse(null);

        if (isNull(blockerLocation) || isNull(blockedLocation)) {
            return new StatusResponse(1);
        }

        Optional<Block> equalBlock = blockRepository.findEqualBlock(blockerLocation.getId(), blockedLocation.getId());
        if (equalBlock.isPresent()) {
            return new StatusResponse(2);
        }

        Block saveBlock = new Block(blockerLocation, blockedLocation);
        blockRepository.save(saveBlock);
        return new StatusResponse(0);
    }

    @Transactional
    public void setPolygon(PolygonRequest request) {
        Polygon polygon = makePolygon(request);

        PCollege college = new PCollege(request.getCollegeId(), polygon);
        pCollegeRepository.save(college);
    }

//    @Transactional
//    public void deleteLocation(IdRequest request) {
//        locationRepository.deleteById(request.getId());
//    }

    private Location findLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    private PCollege findCollegeById(Long id) {
        return pCollegeRepository.findById(id).orElse(null);
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

    private Boolean isNotWithinCollege(Location location) {
        return !location.getIsContained();
    }

    private List<Long> findNearIds(Long id, Point point, Long collegeId) {
        return locationRepository.findNearIds(id, point, collegeId, LocalDate.now());
    }

    private List<NearByInfo> makeNearByList(Member member, List<Long> nearMemberIds) {
        List<NearByInfo> nearByList = new ArrayList<>();
        for (Long nearId : nearMemberIds) {
            Member findMember = memberAccessor.findById(nearId);
            String urlCode = EMPTY_STRING;
            boolean isLiked = false;

            if(isNotBasicProfile(findMember))
                urlCode = findMember.getUrlCode();
            if(isEqualHeartExisted(member.getId(), nearId))
                isLiked = true;

            nearByList.add(NearByInfo.builder()
                    .id(findMember.getId())
                    .name(findMember.getName())
                    .urlCode(urlCode)
                    .bio(findMember.getBio())
                    .isLiked(isLiked)
                    .build());
        }
        return nearByList;
    }

    private Boolean isEqualHeartExisted(Long giveId, Long takeId) {
        Heart equalHeart = heartRepository.findEqualHeart(LocalDate.now(), giveId, takeId).orElse(null);
        return !isNull(equalHeart);
    }

    private Boolean isNotBasicProfile(Member member) {
        return !member.getBasicProfile();
    }

    private Polygon makePolygon(PolygonRequest request) {
        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] coordinates = new Coordinate[] {
                new Coordinate(request.getTopLeftLng(), request.getTopLeftLat()),
                new Coordinate(request.getTopRightLng(), request.getTopRightLat()),
                new Coordinate(request.getBottomRightLng(), request.getBottomRightLat()),
                new Coordinate(request.getBottomLeftLng(), request.getBottomLeftLat()),
                new Coordinate(request.getTopLeftLng(), request.getTopLeftLat())
        };
        return geometryFactory.createPolygon(coordinates);
    }

}
