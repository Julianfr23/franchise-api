package com.accenture.franchise.application.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class CreateBranchRequest {
    @NotBlank(message = "Branch name is required")
    private String name;
}
