package com.accenture.franchise.infrastructure.mapper;

import com.accenture.franchise.domain.model.*;
import com.accenture.franchise.infrastructure.entity.FranchiseDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FranchiseMapper {

    public Franchise toDomain(FranchiseDocument doc) {
        return Franchise.builder()
                .id(doc.getId())
                .name(doc.getName())
                .branches(doc.getBranches().stream().map(this::toDomain).collect(Collectors.toList()))
                .build();
    }

    public Branch toDomain(FranchiseDocument.BranchDocument doc) {
        return Branch.builder()
                .id(doc.getId())
                .name(doc.getName())
                .products(doc.getProducts().stream().map(this::toDomain).collect(Collectors.toList()))
                .build();
    }

    public Product toDomain(FranchiseDocument.ProductDocument doc) {
        return Product.builder()
                .id(doc.getId())
                .name(doc.getName())
                .stock(doc.getStock())
                .build();
    }

    public FranchiseDocument toDocument(Franchise franchise) {
        return FranchiseDocument.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(franchise.getBranches().stream().map(this::toDocument).collect(Collectors.toList()))
                .build();
    }

    public FranchiseDocument.BranchDocument toDocument(Branch branch) {
        return FranchiseDocument.BranchDocument.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(branch.getProducts().stream().map(this::toDocument).collect(Collectors.toList()))
                .build();
    }

    public FranchiseDocument.ProductDocument toDocument(Product product) {
        return FranchiseDocument.ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}
