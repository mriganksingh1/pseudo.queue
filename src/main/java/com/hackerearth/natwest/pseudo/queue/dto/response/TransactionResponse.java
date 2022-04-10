package com.hackerearth.natwest.pseudo.queue.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackerearth.natwest.pseudo.queue.dto.Meta;
import com.hackerearth.natwest.pseudo.queue.dto.request.TransactionRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse {

    private Meta meta;
    private TransactionRequest data;
}
