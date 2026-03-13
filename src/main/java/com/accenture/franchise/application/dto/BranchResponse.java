package com.accenture.franchise.application.dto;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BranchResponse {
    private String id;
    private String name;
    private List<ProductResponse> products;
}
