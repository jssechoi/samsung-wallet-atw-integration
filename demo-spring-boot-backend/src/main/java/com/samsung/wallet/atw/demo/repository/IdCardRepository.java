package com.samsung.wallet.atw.demo.repository;

import com.samsung.wallet.atw.demo.entity.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdCardRepository extends JpaRepository<IdCard, String> {

    List<IdCard> findAllByOrderByCreatedAtDesc();
}
