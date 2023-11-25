package com.mjuAppSW.joA.geography.block.dto;

import lombok.Getter;

@Getter
public class BlockRequest {
    private Long blockerId; // session id
    private Long blockedId; // pk
}
