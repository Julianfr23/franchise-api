package com.accenture.franchise.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "franchises")
public class FranchiseDocument {
    @Id
    private String id;
    private String name;
    @Builder.Default
    private List<BranchDocument> branches = new ArrayList<>();

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class BranchDocument {
        private String id;
        private String name;
        @Builder.Default
        private List<ProductDocument> products = new ArrayList<>();
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ProductDocument {
        private String id;
        private String name;
        private int stock;
    }
}
