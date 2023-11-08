package com.postech.fase3parquimetro.utils;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.parking.exceptions.ParkingException;
import com.postech.fase3parquimetro.payments.exceptions.PaymentException;
import com.postech.fase3parquimetro.receipt.ReceiptException;
import com.postech.fase3parquimetro.vehicle.exceptions.VehicleException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ReceiptException.class)
    public ResponseEntity<ApiErrorResponse> handleReceiptException(final ReceiptException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }

    @ExceptionHandler(ConductorException.class)
    public ResponseEntity<ApiErrorResponse> handleConductorException(final ConductorException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }

    @ExceptionHandler(VehicleException.class)
    public ResponseEntity<ApiErrorResponse> handleVehicleException(final VehicleException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }

    @ExceptionHandler(ParkingException.class)
    public ResponseEntity<ApiErrorResponse> handleParkingException(final ParkingException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentException(final PaymentException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatusCode());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException error) {
        Map<String, String> errorResponse = Map.of("error", error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
