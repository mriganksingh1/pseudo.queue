package com.hackerearth.natwest.pseudo.queue.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptionResponse {

    private String encryptedAccountNumber;
    private String encryptedType;
    private String encryptedAmount;
    private String encryptedCurrency;
    private String encryptedAccountFrom;
}
