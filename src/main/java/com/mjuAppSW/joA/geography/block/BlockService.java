package com.mjuAppSW.joA.geography.block;

import static java.util.Objects.isNull;

import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.dto.StatusResponse;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.geography.location.Location;
import com.mjuAppSW.joA.geography.location.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public StatusResponse execute(BlockRequest request) {
        Member blockerMember = memberRepository.findBysessionId(request.getBlockerId()).orElse(null);
        if (isNull(blockerMember)) {
            return new StatusResponse(1);
        }
        Location blockerLocation = locationRepository.findById(blockerMember.getId()).orElse(null);
        Location blockedLocation = locationRepository.findById(request.getBlockedId()).orElse(null);

        if (isNull(blockerLocation) || isNull(blockedLocation)) {
            return new StatusResponse(1);
        }

        Block equalBlock = blockRepository.findEqualBlock(blockerLocation.getId(), blockedLocation.getId());
        if (!isNull(equalBlock)) {
            return new StatusResponse(2);
        }

        Block saveBlock = new Block(blockerLocation, blockedLocation);
        blockRepository.save(saveBlock);
        return new StatusResponse(0);
    }
}
