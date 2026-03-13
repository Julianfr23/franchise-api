package com.accenture.franchise.infrastructure.adapter.web;

import com.accenture.franchise.application.dto.*;
import com.accenture.franchise.application.usecase.FranchiseUseCase;
import com.accenture.franchise.domain.model.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseUseCase franchiseUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        return franchiseUseCase.createFranchise(request.getName())
                .map(this::toResponse);
    }

    @PatchMapping("/{franchiseId}/name")
    public Mono<FranchiseResponse> updateFranchiseName(
            @PathVariable String franchiseId,
            @Valid @RequestBody UpdateNameRequest request) {
        return franchiseUseCase.updateFranchiseName(franchiseId, request.getName())
                .map(this::toResponse);
    }

    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> addBranch(
            @PathVariable String franchiseId,
            @Valid @RequestBody CreateBranchRequest request) {
        return franchiseUseCase.addBranch(franchiseId, request.getName())
                .map(this::toResponse);
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/name")
    public Mono<FranchiseResponse> updateBranchName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @Valid @RequestBody UpdateNameRequest request) {
        return franchiseUseCase.updateBranchName(franchiseId, branchId, request.getName())
                .map(this::toResponse);
    }

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> addProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @Valid @RequestBody CreateProductRequest request) {
        return franchiseUseCase.addProduct(franchiseId, branchId, request.getName(), request.getStock())
                .map(this::toResponse);
    }

    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId) {
        return franchiseUseCase.removeProduct(franchiseId, branchId, productId)
                .then();
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<FranchiseResponse> updateProductStock(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @Valid @RequestBody UpdateStockRequest request) {
        return franchiseUseCase.updateProductStock(franchiseId, branchId, productId, request.getStock())
                .map(this::toResponse);
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/name")
    public Mono<FranchiseResponse> updateProductName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @Valid @RequestBody UpdateNameRequest request) {
        return franchiseUseCase.updateProductName(franchiseId, branchId, productId, request.getName())
                .map(this::toResponse);
    }

    @GetMapping("/{franchiseId}/top-stock-products")
    public Flux<ProductByBranchResponse> getTopStockProductPerBranch(@PathVariable String franchiseId) {
        return franchiseUseCase.getTopStockProductPerBranch(franchiseId)
                .map(this::toResponse);
    }

    // --- Mappers ---

    private FranchiseResponse toResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(franchise.getBranches().stream().map(this::toResponse).collect(Collectors.toList()))
                .build();
    }

    private BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(branch.getProducts().stream().map(this::toResponse).collect(Collectors.toList()))
                .build();
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }

    private ProductByBranchResponse toResponse(ProductByBranch pbb) {
        return ProductByBranchResponse.builder()
                .branchId(pbb.getBranchId())
                .branchName(pbb.getBranchName())
                .product(toResponse(pbb.getProduct()))
                .build();
    }
}
