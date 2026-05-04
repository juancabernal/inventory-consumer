package com.co.inventoryconsumer.utils.location.validation;

import java.time.LocalTime;
import java.util.UUID;
import com.co.inventoryconsumer.utils.exceptions.ValidationException;

public class LocationValidator {

    private LocationValidator() {}

    public static UUID validateId(UUID id) {
        if (id == null) {
            throw new ValidationException("El id de la sede es obligatorio");
        }
        return id;
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " must not be null or blank");
        }
        return value;
    }

    public static String validateName(String name) { return requireNonBlank(name, "name"); }
    public static String validateCity(String city) { return requireNonBlank(city, "city"); }
    public static String validateAddress(String address) { return requireNonBlank(address, "address"); }
    public static String validateEmail(String email) {
        requireNonBlank(email, "email");
        if (!email.contains("@")) {
            throw new ValidationException("email is not valid: " + email);
        }
        return email;
    }
    public static String validatePhoneNumber(String phoneNumber) {
        return requireNonBlank(phoneNumber, "phoneNumber");
    }
    public static LocalTime validateStartTime(LocalTime startTime) {
        if (startTime == null) { throw new ValidationException("startTime must not be null"); }
        return startTime;
    }
    public static LocalTime validateEndTime(LocalTime endTime) {
        if (endTime == null) { throw new ValidationException("endTime must not be null"); }
        return endTime;
    }
}