package com.accenture.franchise.application.usecase;

import com.accenture.franchise.domain.exception.BranchNotFoundException;
import com.accenture.franchise.domain.exception.FranchiseNotFoundException;
import com.accenture.franchise.domain.model.*;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    private Franchise sampleFranchise;
    private Branch sampleBranch;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder().id("p1").name("Product A").stock(100).build();
        sampleBranch = Branch.builder().id("b1").name("Branch One")
                .products(new ArrayList<>(Arrays.asList(sampleProduct))).build();
        sampleFranchise = Franchise.builder().id("f1").name("Franchise One")
                .branches(new ArrayList<>(Arrays.asList(sampleBranch))).build();
    }

    @Test
    @DisplayName("Should create franchise successfully")
    void createFranchise_success() {
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.createFranchise("New Franchise"))
                .expectNextMatches(f -> f.getName().equals("New Franchise") && f.getId() != null)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw FranchiseNotFoundException when franchise not found on update name")
    void updateFranchiseName_notFound() {
        when(franchiseRepository.findById("unknown")).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.updateFranchiseName("unknown", "New Name"))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update franchise name successfully")
    void updateFranchiseName_success() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.updateFranchiseName("f1", "Updated Name"))
                .expectNextMatches(f -> f.getName().equals("Updated Name"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should add branch to existing franchise")
    void addBranch_success() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.addBranch("f1", "Branch Two"))
                .expectNextMatches(f -> f.getBranches().size() == 2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw BranchNotFoundException when branch not found on add product")
    void addProduct_branchNotFound() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));

        StepVerifier.create(franchiseUseCase.addProduct("f1", "invalidBranch", "Product", 10))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should add product to branch successfully")
    void addProduct_success() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.addProduct("f1", "b1", "New Product", 50))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().size() == 2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should remove product successfully")
    void removeProduct_success() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.removeProduct("f1", "b1", "p1"))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update product stock successfully")
    void updateProductStock_success() {
        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));
        when(franchiseRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(franchiseUseCase.updateProductStock("f1", "b1", "p1", 999))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().get(0).getStock() == 999)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return top stock product per branch")
    void getTopStockProductPerBranch_success() {
        Product p2 = Product.builder().id("p2").name("Product B").stock(200).build();
        sampleBranch.getProducts().add(p2);

        when(franchiseRepository.findById("f1")).thenReturn(Mono.just(sampleFranchise));

        StepVerifier.create(franchiseUseCase.getTopStockProductPerBranch("f1"))
                .expectNextMatches(pbb -> pbb.getProduct().getStock() == 200 && pbb.getBranchName().equals("Branch One"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw FranchiseNotFoundException for top stock when franchise not found")
    void getTopStockProductPerBranch_notFound() {
        when(franchiseRepository.findById("unknown")).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.getTopStockProductPerBranch("unknown"))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}
