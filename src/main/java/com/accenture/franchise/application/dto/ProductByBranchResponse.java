package com.accenture.franchise.application.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductByBranchResponse {
    private String branchId;
    private String branchName;
    private ProductResponse product;
}
