package com.hackerearth.natwest.pseudo.queue.service.impl;

import com.hackerearth.natwest.pseudo.queue.config.AESEncryptDecrypt;
import com.hackerearth.natwest.pseudo.queue.constants.Constants;
import com.hackerearth.natwest.pseudo.queue.constants.FailureCodes;
import com.hackerearth.natwest.pseudo.queue.dto.Meta;
import com.hackerearth.natwest.pseudo.queue.dto.ResponseDTO;
import com.hackerearth.natwest.pseudo.queue.dto.request.TransactionRequest;
import com.hackerearth.natwest.pseudo.queue.dto.response.EncryptionResponse;
import com.hackerearth.natwest.pseudo.queue.dto.response.TransactionResponse;
import com.hackerearth.natwest.pseudo.queue.repository.PseudoQueueRepository;
import com.hackerearth.natwest.pseudo.queue.service.PseudoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PseudoServiceImpl implements PseudoService {


    private final static Logger logger = LoggerFactory.getLogger(PseudoServiceImpl.class);

    @Value("${aes.secret}")
    private String aesSecret;

    @Value("${decrypt.api.url}")
    String decryptUrl;

    @Autowired
    private AESEncryptDecrypt aesEncryptDecrypt;

    @Autowired
    private PseudoQueueRepository pseudoQueueRepository;

    @Override
    public ResponseDTO<EncryptionResponse> bringEncryptedResponse(TransactionRequest transactionRequest) {
        EncryptionResponse encryptionResponse = new EncryptionResponse();
        ResponseDTO<EncryptionResponse> encryptionResponseResponseDTO = new ResponseDTO<>();
        try {
            encryptionResponse = EncryptionResponse.builder()
                    .encryptedAccountNumber(aesEncryptDecrypt.encrypt(transactionRequest.getAccountNumber(),aesSecret))
                    .encryptedType(aesEncryptDecrypt.encrypt(transactionRequest.getType(),aesSecret))
                    .encryptedAmount(aesEncryptDecrypt.encrypt(transactionRequest.getAmount(),aesSecret))
                    .encryptedCurrency(aesEncryptDecrypt.encrypt(transactionRequest.getCurrency(),aesSecret))
                    .encryptedAccountFrom(aesEncryptDecrypt.encrypt(transactionRequest.getAccountFrom(),aesSecret))
                    .build();
            logger.info("Encrypted Response : {}",encryptionResponse.toString());
        } catch (Exception e){
            logger.info("Exception occured while encrypting data : {}",e.getMessage());
            encryptionResponseResponseDTO.setMeta(buildMetaForEncryptFailure());
            return encryptionResponseResponseDTO;
        }
        RestTemplate restTemplate = new RestTemplate();
        TransactionResponse response = new TransactionResponse();
        try {
            response =  restTemplate.postForObject(decryptUrl,encryptionResponse, TransactionResponse.class);
        } catch (Exception e){
            logger.info("There is an error while hitting decryption API : {}",e.getMessage());
            encryptionResponseResponseDTO.setMeta(buildMetaForAPIFailure());
        }
        if( response != null && response.getMeta().getCode().equals(FailureCodes.DECRYPT_API_FAILURE.value())) {
            encryptionResponseResponseDTO.setMeta(response.getMeta());
            return encryptionResponseResponseDTO;
        } else if(response != null && response.getMeta().getCode().equals(FailureCodes.DATABASE_SAVING_FAILURE)) {
            encryptionResponseResponseDTO.setMeta(response.getMeta());
            return encryptionResponseResponseDTO;
        }
        encryptionResponseResponseDTO.setMeta(buildMetaForSuccess());
        encryptionResponseResponseDTO.setData(encryptionResponse);
        return encryptionResponseResponseDTO;
    }


    @Override
    public ResponseDTO<TransactionRequest> decryptDataAndSave(EncryptionResponse encryptionRequest) {
        ResponseDTO<TransactionRequest> encryptionResponseResponseDTO = new ResponseDTO<>();
        TransactionRequest transactionRequest = new TransactionRequest();
        try {
            transactionRequest = TransactionRequest.builder()
                    .accountNumber(aesEncryptDecrypt.decrypt(encryptionRequest.getEncryptedAccountNumber(),aesSecret))
                    .type(aesEncryptDecrypt.decrypt(encryptionRequest.getEncryptedType(),aesSecret))
                    .amount(aesEncryptDecrypt.decrypt(encryptionRequest.getEncryptedAmount(),aesSecret))
                    .currency(aesEncryptDecrypt.decrypt(encryptionRequest.getEncryptedCurrency(),aesSecret))
                    .accountFrom(aesEncryptDecrypt.decrypt(encryptionRequest.getEncryptedAccountFrom(),aesSecret))
                    .build();
        } catch (Exception e){
            logger.info("There is a problem decrypting data : {}",e.getMessage());
            encryptionResponseResponseDTO.setMeta(buildMetaForDecryptFailure());
            return encryptionResponseResponseDTO;
        }
        logger.debug("Transaction Object after decrypting : {}",transactionRequest);
        TransactionRequest dbResponse = new TransactionRequest();
        try {
            dbResponse = pseudoQueueRepository.save(transactionRequest);
        } catch (Exception e){
            logger.info("There is an error in saving data to the database : {}",e.getMessage());
            encryptionResponseResponseDTO.setMeta(buildMetaForDatabaseFailure());
            return encryptionResponseResponseDTO;
        }
        encryptionResponseResponseDTO.setMeta(buildMetaForSuccess());
        encryptionResponseResponseDTO.setData(dbResponse);
        return encryptionResponseResponseDTO;
    }

    private Meta buildMetaForDatabaseFailure() {
        Meta meta = new Meta();
        setMetaResponse(meta,FailureCodes.DATABASE_SAVING_FAILURE.value(), "There is an error while saving data in Database", Constants.FAILURE);
        return meta;
    }

    private Meta buildMetaForAPIFailure() {
        Meta meta = new Meta();
        setMetaResponse(meta,FailureCodes.DECRYPT_API_FAILURE.value(), "There is an error while hitting decryption API", Constants.FAILURE);
        return meta;
    }

    private Meta buildMetaForEncryptFailure(){
        Meta meta = new Meta();
        setMetaResponse(meta,FailureCodes.ENCRYPT_FAILURE.value(), "There is an error encrypting data", Constants.FAILURE);
        return meta;
    }

    private Meta buildMetaForSuccess(){
        Meta meta = new Meta();
        setMetaResponse(meta,"200","Success",Constants.SUCCESS);
        return meta;
    }

    private Meta buildMetaForDecryptFailure() {
        Meta meta = new Meta();
        setMetaResponse(meta,FailureCodes.DECRYPT_FAILURE.value(), "There is an error decrypting data",Constants.FAILURE);
        return meta;
    }

    private void setMetaResponse(Meta meta,String code,String description,Integer status){
        meta.setCode(code);
        meta.setDescription(description);
        meta.setStatus(status);
    }
}
