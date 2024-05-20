package com.example.mq.controller.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;


/**
 * Class responsible for the structure of the error response entity.
 * * Annotations:
 * * @AllArgsConstructor: Generates a constructor with 1 parameter for each field in your class. Fields are initialized in the order they are declared.
 * * @NoArgsConstructor: Generates a no-argument constructor.
 * * @Getter: Generates getters.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
