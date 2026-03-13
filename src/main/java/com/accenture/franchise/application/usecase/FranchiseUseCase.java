package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.exception.BranchNotFoundException;
import com.accenture.franchise.domain.exception.FranchiseNotFoundException;
import com.accenture.franchise.domain.exception.ProductNotFoundException;
import com.accenture.franchise.domain.model.*;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(String name) {
        Franchise franchise = Franchise.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .build();
        return franchiseRepository.save(franchise);
    }

    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.save(franchise);
                });
    }

    public Mono<Franchise> addBranch(String franchiseId, String branchName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(branchName)
                            .build();
                    franchise.getBranches().add(branch);
                    return franchiseRepository.save(franchise);
                });
    }

    public Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));
                    branch.setName(newName);
                    return franchiseRepository.save(franchise);
                });
    }

    public Mono<Franchise> addProduct(String franchiseId, String branchId, String productName, int stock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));
                    Product product = Product.builder()
                            .id(UUID.randomUUID().toString())
                            .name(productName)
                            .stock(stock)
                            .build();
                    branch.getProducts().add(product);
                    return franchiseRepository.save(franchise);
                });
    }

    public Mono<Franchise> removeProduct(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));
                    boolean removed = branch.getProducts().removeIf(p -> p.getId().equals(productId));
                    if (!removed) throw new ProductNotFoundException(productId);
                    return franchiseRepository.save(franchise);
                });
    }

    public Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));
                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new ProductNotFoundException(productId));
                    product.setStock(newStock);
                    return franchiseRepository.save(franchise);
                });
    }

    public Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));
                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new ProductNotFoundException(productId));
                    product.setName(newName);
                    return franchiseRepository.save(franchise);
                });
    }

    public Flux<ProductByBranch> getTopStockProductPerBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                        .sort(Comparator.comparingInt(Product::getStock).reversed())
                        .next()
                        .map(product -> ProductByBranch.builder()
                                .branchId(branch.getId())
                                .branchName(branch.getName())
                                .product(product)
                                .build())
                );
    }
}
