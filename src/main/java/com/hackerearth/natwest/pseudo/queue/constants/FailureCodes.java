package com.hackerearth.natwest.pseudo.queue.constants;

import org.springframework.http.HttpStatus;

public enum FailureCodes implements ResponseCode{
    ENCRYPT_FAILURE(HttpStatus.OK),
    DECRYPT_FAILURE(HttpStatus.OK),
    DECRYPT_API_FAILURE(HttpStatus.OK),
    DATABASE_SAVING_FAILURE(HttpStatus.OK);

    private HttpStatus httpStatus;

    FailureCodes(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String value() {
        return name();
    }
}
