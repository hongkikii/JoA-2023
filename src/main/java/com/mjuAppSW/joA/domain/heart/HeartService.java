package com.mjuAppSW.joA.domain.heart;


import com.mjuAppSW.joA.domain.heart.dto.HeartRequest;
import com.mjuAppSW.joA.domain.heart.dto.HeartResponse;

public interface HeartService {

    HeartResponse sendHeart(HeartRequest request); // giveId, takeId, named
}
