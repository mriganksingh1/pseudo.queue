package com.hackerearth.natwest.pseudo.queue.service;

import com.hackerearth.natwest.pseudo.queue.dto.ResponseDTO;
import com.hackerearth.natwest.pseudo.queue.dto.request.TransactionRequest;
import com.hackerearth.natwest.pseudo.queue.dto.response.EncryptionResponse;

public interface PseudoService {
    ResponseDTO<EncryptionResponse> bringEncryptedResponse(TransactionRequest transactionRequest);

    ResponseDTO<TransactionRequest> decryptDataAndSave(EncryptionResponse encryptionRequest);
}
