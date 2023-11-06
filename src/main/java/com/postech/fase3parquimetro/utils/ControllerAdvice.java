package com.postech.fase3parquimetro.utils;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.parking.exceptions.ParkingException;
import com.postech.fase3parquimetro.payments.exceptions.PaymentException;
import com.postech.fase3parquimetro.receipt.ReceiptException;
import com.postech.fase3parquimetro.vehicle.exceptions.VehicleException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ReceiptException.class)
    public ResponseEntity<ApiErrorResponse> handleReceiptException(final ReceiptException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return new ResponseEntity(apiResponse, HttpStatus.valueOf(apiResponse.status()));
    }

    @ExceptionHandler(ConductorException.class)
    public ResponseEntity<ApiErrorResponse> handleConductorException(final ConductorException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return new ResponseEntity(apiResponse, HttpStatus.valueOf(apiResponse.status()));
    }

    @ExceptionHandler(VehicleException.class)
    public ResponseEntity<ApiErrorResponse> handleVehicleException(final VehicleException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return new ResponseEntity(apiResponse, HttpStatus.valueOf(apiResponse.status()));
    }

    @ExceptionHandler(ParkingException.class)
    public ResponseEntity<ApiErrorResponse> handleParkingException(final ParkingException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return new ResponseEntity(apiResponse, HttpStatus.valueOf(apiResponse.status()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentException(final PaymentException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return new ResponseEntity(apiResponse, HttpStatus.valueOf(apiResponse.status()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
