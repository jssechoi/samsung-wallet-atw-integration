package com.samsung.wallet.atw.demo.service;

import com.samsung.wallet.atw.demo.entity.BoardingPass;
import com.samsung.wallet.atw.demo.repository.BoardingPassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardingPassService {

    private final BoardingPassRepository repository;

    public BoardingPassService(BoardingPassRepository repository) {
        this.repository = repository;
    }

    public List<BoardingPass> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public BoardingPass findByRefId(String refId) {
        return repository.findById(refId).orElse(null);
    }
}
