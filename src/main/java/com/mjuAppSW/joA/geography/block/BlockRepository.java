package com.mjuAppSW.joA.geography.block;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("SELECT b FROM Block b WHERE b.blocker = :blockerId AND b.blocked = :blockedId")
    Block findEqualBlock(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);
}
