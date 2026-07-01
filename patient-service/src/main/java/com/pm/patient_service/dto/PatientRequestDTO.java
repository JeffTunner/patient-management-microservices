package com.pm.patient_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDTO {

    @NotBlank(message = "Name is Required.")
    @Size(max = 100, message = "Name cannot be bigger than that.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is Required.")
    private String address;

    @NotBlank(message = "DateOfBirth is Required.")
    private String dateOfBirth;

    @NotNull(message = "Registered Date is Required.")
    private String registeredDate;
}
