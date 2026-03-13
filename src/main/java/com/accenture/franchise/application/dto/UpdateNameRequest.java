package com.accenture.franchise.application.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class UpdateNameRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
