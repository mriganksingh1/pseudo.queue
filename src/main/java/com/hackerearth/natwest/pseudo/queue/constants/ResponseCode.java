package com.hackerearth.natwest.pseudo.queue.constants;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

    HttpStatus getStatus();

    String value();
}
