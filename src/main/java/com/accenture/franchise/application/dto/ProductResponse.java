package com.accenture.franchise.application.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private int stock;
}
