package com.mjuAppSW.joA.domain.roomInMember.dto.response;

import java.util.List;

import com.mjuAppSW.joA.domain.roomInMember.dto.RoomListVO;

import lombok.Builder;

public class RoomListResponse {
	List<RoomListVO> roomListVOs;

	@Builder
	public RoomListResponse(List<RoomListVO> roomListVOs){
		this.roomListVOs = roomListVOs;
	}

	public static RoomListResponse of(List<RoomListVO> roomListVOs) {
		return RoomListResponse.builder()
			.roomListVOs(roomListVOs)
			.build();
	}
}