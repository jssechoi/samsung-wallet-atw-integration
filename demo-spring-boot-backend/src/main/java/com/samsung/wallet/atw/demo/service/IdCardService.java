package com.samsung.wallet.atw.demo.service;

import com.samsung.wallet.atw.demo.entity.IdCard;
import com.samsung.wallet.atw.demo.repository.IdCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdCardService {

    private final IdCardRepository repository;

    public IdCardService(IdCardRepository repository) {
        this.repository = repository;
    }

    public List<IdCard> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public IdCard findByRefId(String refId) {
        return repository.findById(refId).orElse(null);
    }
}
