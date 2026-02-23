package com.samsung.wallet.atw.demo.repository;

import com.samsung.wallet.atw.demo.entity.BoardingPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardingPassRepository extends JpaRepository<BoardingPass, String> {

    List<BoardingPass> findAllByOrderByCreatedAtDesc();
}
