package com.hackerearth.natwest.pseudo.queue.controller;

import com.hackerearth.natwest.pseudo.queue.dto.request.TransactionRequest;
import com.hackerearth.natwest.pseudo.queue.dto.response.EncryptionResponse;
import com.hackerearth.natwest.pseudo.queue.dto.ResponseDTO;
import com.hackerearth.natwest.pseudo.queue.service.PseudoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("queue/api/v1")
public class PseudoQueue {

    private static final Logger logger= LoggerFactory.getLogger(PseudoQueue.class);

    @Autowired
    private PseudoService pseudoService;


    @PostMapping("/encryption")
    public ResponseEntity<ResponseDTO<EncryptionResponse>> encryptData(@RequestBody TransactionRequest transactionRequest) {
        logger.debug("Transaction Object : {}",transactionRequest.toString());
        ResponseDTO<EncryptionResponse> encryptedResponse = pseudoService.bringEncryptedResponse(transactionRequest);
        logger.debug("Response from service : {}",encryptedResponse);
        return new ResponseEntity<>(encryptedResponse, HttpStatus.OK);
    }

    @PostMapping("/decryption")
    public ResponseEntity<ResponseDTO<TransactionRequest>> decryptData(@RequestBody EncryptionResponse encryptionRequest){
        logger.debug("The request : {}",encryptionRequest);
        ResponseDTO<TransactionRequest> encryptionResponse = pseudoService.decryptDataAndSave(encryptionRequest);
        logger.debug("Response from service : {}",encryptionResponse);
        return new ResponseEntity<>(encryptionResponse,HttpStatus.OK);
    }
}
