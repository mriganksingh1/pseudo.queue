# NATWEST JAVA DEVELOPER PROJECT

## AIM
The aim of this project is to create 2 API. First API tooks a transaction object and encrypts it using an encryption algorithm and then pass the encrypted object to the second API via API call. The second API decrypts the data and save the data into the database.

## TECHNOLOGIES USED
* SpringBoot
* Java 8
* H2 Database
* Log4j2

## STEPS INVOLVED IN DEVELOPMENT
1. Packages are made in the project structure for structuring the code properly. config --> For saving configuration, controller --> for our REST controller, service --> For writing business layer logic, repository --> For repository layer logic, constants --> For defining constants used in application.
2. For encryption purpose i used the AES Encryption Decryption algorith which uses a String value and using a private key it encrypts and decrypt the String value. The private key is placed in application.properties file.
3. Then I created an endpoint which took Transaction Object in request body and encrypts it in service layer using AES algorithm and calls other api using Rest Template.
4. The call flow goes to other API which decrypts the data using AES and saved it to in memory H2-Database.
5. Then the response from database is returned and a check is there in the first API to see if the data is not saved or if decryption has failed then error codes defines later in the file are retuend.
6. The first API returns the Encrypted data in the response on hitting it.
7. The second API returns the data saved in the database after decrypting.
8. Both are REST API and are POST method.

## LocalHost Curl

### FIRST AND EXPOSED API CURL

curl --location --request POST 'http://localhost:8080/queue/api/v1/encryption' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber": "123456789113",
    "type": "credit",
    "amount": "10000",
    "currency" : "INR",
    "accountFrom":"9756597885"
}'

### SECOND API CURL
 * This API can be hit if we took the encrypted response from the first API and give it to the second API in the request body.

 curl --location --request POST 'http://localhost:8080/queue/api/v1/decryption' \
--header 'Content-Type: application/json' \
--data-raw '{
        "encryptedAccountNumber": "AR7Y7bF5YwpAt/ROSfej8w==",
        "encryptedType": "Dd1Y8Ye2xEbIdyT0firNsw==",
        "encryptedAmount": "4mPd08i9qzGWh2np0w1Q2g==",
        "encryptedCurrency": "JfoQBuU/7QYB595MVmVoBA==",
        "encryptedAccountFrom": "IgFvui5x8h2e5c6v0Xzb+g=="
    }'

## LocalHost Response

{ 

    "meta": {
        "code": "200",
        "description": "Success",
        "status": 1
    },
    "data": {
        "encryptedAccountNumber": "AR7Y7bF5YwpAt/ROSfej8w==",
        "encryptedType": "Dd1Y8Ye2xEbIdyT0firNsw==",
        "encryptedAmount": "4mPd08i9qzGWh2np0w1Q2g==",
        "encryptedCurrency": "JfoQBuU/7QYB595MVmVoBA==",
        "encryptedAccountFrom": "IgFvui5x8h2e5c6v0Xzb+g=="
    }
}

## DataBase Saving Image


![Database](https://drive.google.com/uc?export=view&id=17oEU7TMI8PKk0McRk2m6Mf0Xp94yQr5F)


## Error Codes

* ENCRYPT_FAILURE  --> When there will be failure in encrypting the data in first API
* DECRYPT_FAILURE  --> When there will be failure in decrypting the data in second API
* DECRYPT_API_FAILURE -->  When the second API is down due to some reason or there is error in fetching response from the second API from RestTemplate in First API.
* DATABASE_SAVING_FAILURE --> When there is an error in saving the data to the database.


## HEROKU DEPLOYED PUBLIC CURL

curl --location --request POST 'https://natwest-java.herokuapp.com/queue/api/v1/encryption' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber": "123456789112",
    "type": "credit",
    "amount": "10000",
    "currency" : "INR",
    "accountFrom":"9756597885"
}'


* Verification can be done by pasting the response in the decryption API.


## Github Project Link

https://github.com/mriganksingh1/pseudo.queue