package com.accenture.franchise.domain.model;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductByBranch {
    private String branchId;
    private String branchName;
    private Product product;
}
