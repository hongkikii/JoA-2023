package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.location.dto.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateResponse;

public interface LocationService {
    UpdateResponse updateLocation(UpdateRequest request); // sessionId, latitude, longitude, altitude

    NearByListResponse getNearByList(UpdateRequest request); // sessionId, latitude, longitude, altitude

    OwnerResponse getOwnerInfo(Long id);

    void setPolygon(PolygonRequest request); // collegeId, 8 direction
}
