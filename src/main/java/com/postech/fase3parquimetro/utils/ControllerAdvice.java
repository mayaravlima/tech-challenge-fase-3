package com.postech.fase3parquimetro.utils;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.parking.exceptions.ParkingException;
import com.postech.fase3parquimetro.receipt.ReceiptException;
import com.postech.fase3parquimetro.vehicle.exceptions.VehicleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
