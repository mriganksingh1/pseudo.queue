package com.hackerearth.natwest.pseudo.queue.repository;

import com.hackerearth.natwest.pseudo.queue.dto.request.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PseudoQueueRepository extends JpaRepository<TransactionRequest,String> {
}
