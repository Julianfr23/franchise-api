package com.accenture.franchise.domain.model;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Branch {
    private String id;
    private String name;
    @Builder.Default
    private List<Product> products = new ArrayList<>();
}
