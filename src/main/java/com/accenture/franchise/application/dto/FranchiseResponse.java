package com.accenture.franchise.application.dto;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FranchiseResponse {
    private String id;
    private String name;
    private List<BranchResponse> branches;
}
