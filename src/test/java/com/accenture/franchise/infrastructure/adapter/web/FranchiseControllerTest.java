package com.accenture.franchise.infrastructure.adapter.web;

import com.accenture.franchise.application.dto.*;
import com.accenture.franchise.application.usecase.FranchiseUseCase;
import com.accenture.franchise.domain.exception.FranchiseNotFoundException;
import com.accenture.franchise.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FranchiseUseCase franchiseUseCase;

    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        Product product = Product.builder().id("p1").name("Product A").stock(50).build();
        Branch branch = Branch.builder().id("b1").name("Branch One")
                .products(new ArrayList<>(List.of(product))).build();
        sampleFranchise = Franchise.builder().id("f1").name("Franchise One")
                .branches(new ArrayList<>(List.of(branch))).build();
    }

    @Test
    @DisplayName("POST /api/v1/franchises - should create franchise and return 201")
    void createFranchise_returns201() {
        when(franchiseUseCase.createFranchise("Franchise One")).thenReturn(Mono.just(sampleFranchise));

        webTestClient.post().uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Franchise One\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Franchise One")
                .jsonPath("$.id").isEqualTo("f1");
    }

    @Test
    @DisplayName("POST /api/v1/franchises - should return 400 when name is blank")
    void createFranchise_returns400WhenNameBlank() {
        webTestClient.post().uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("GET /api/v1/franchises/{id}/top-stock-products - should return top products")
    void getTopStockProducts_returnsProducts() {
        ProductByBranch pbb = ProductByBranch.builder()
                .branchId("b1").branchName("Branch One")
                .product(Product.builder().id("p1").name("Product A").stock(50).build())
                .build();
        when(franchiseUseCase.getTopStockProductPerBranch("f1")).thenReturn(Flux.just(pbb));

        webTestClient.get().uri("/api/v1/franchises/f1/top-stock-products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductByBranchResponse.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("GET /api/v1/franchises/{id}/top-stock-products - should return 404 when not found")
    void getTopStockProducts_returns404() {
        when(franchiseUseCase.getTopStockProductPerBranch("unknown"))
                .thenReturn(Flux.error(new FranchiseNotFoundException("unknown")));

        webTestClient.get().uri("/api/v1/franchises/unknown/top-stock-products")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("DELETE product - should return 204 when deleted")
    void removeProduct_returns204() {
        when(franchiseUseCase.removeProduct("f1", "b1", "p1")).thenReturn(Mono.just(sampleFranchise));

        webTestClient.delete().uri("/api/v1/franchises/f1/branches/b1/products/p1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
