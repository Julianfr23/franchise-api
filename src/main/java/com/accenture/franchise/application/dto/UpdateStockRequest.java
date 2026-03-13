package com.accenture.franchise.application.dto;
import jakarta.validation.constraints.Min;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class UpdateStockRequest {
    @Min(value = 0, message = "Stock must be >= 0")
    private int stock;
}
