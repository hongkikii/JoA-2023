package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.dto.StatusResponse;
import com.mjuAppSW.joA.geography.location.dto.response.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.response.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.request.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.request.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.response.UpdateResponse;

public interface LocationService {
    UpdateResponse updateLocation(UpdateRequest request);

    NearByListResponse getNearByList(UpdateRequest request);

    OwnerResponse getOwner(Long id);

    StatusResponse block(BlockRequest request);

    void setPolygon(PolygonRequest request);
}
