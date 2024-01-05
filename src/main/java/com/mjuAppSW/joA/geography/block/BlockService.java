package com.mjuAppSW.joA.geography.block;

import com.mjuAppSW.joA.common.session.SessionManager;
import com.mjuAppSW.joA.geography.block.exception.BlockAccessForbiddenException;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.geography.block.dto.BlockRequest;
import com.mjuAppSW.joA.geography.block.exception.LocationNotFoundException;
import com.mjuAppSW.joA.geography.location.Location;
import com.mjuAppSW.joA.geography.location.LocationRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final LocationRepository locationRepository;
    private final SessionManager sessionManager;

    @Transactional
    public void block(BlockRequest request) {
        Member blockerMember = sessionManager.findBySessionId(request.getBlockerId());

        Location blockerLocation = locationRepository.findById(blockerMember.getId())
                .orElseThrow(LocationNotFoundException::new);
        Location blockedLocation = locationRepository.findById(request.getBlockedId())
                .orElseThrow(LocationNotFoundException::new);

        checkEqualBlock(blockerLocation.getId(), blockedLocation.getId());

        Block saveBlock = new Block(blockerLocation, blockedLocation);
        blockRepository.save(saveBlock);
    }

    private void checkEqualBlock(Long blockerId, Long blockedId) {
        Optional<Block> equalBlock = blockRepository.findEqualBlock(blockerId, blockedId);
        if (equalBlock.isPresent()) {
            throw new BlockAccessForbiddenException();
        }
    }
}
