package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.dto.StatusResponse;
import com.mjuAppSW.joA.geography.location.dto.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.OwnerResponse;
import com.mjuAppSW.joA.geography.location.dto.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateResponse;

public interface LocationService {
    UpdateResponse updateLocation(UpdateRequest request);

    NearByListResponse getNearByList(UpdateRequest request);

    OwnerResponse getOwnerInfo(Long id);

    StatusResponse execute(BlockRequest request);

    void setPolygon(PolygonRequest request);
}
