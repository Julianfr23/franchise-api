package com.accenture.franchise.application.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class CreateFranchiseRequest {
    @NotBlank(message = "Franchise name is required")
    private String name;
}
