package com.accenture.franchise.application.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    @Min(value = 0, message = "Stock must be >= 0")
    private int stock;
}
