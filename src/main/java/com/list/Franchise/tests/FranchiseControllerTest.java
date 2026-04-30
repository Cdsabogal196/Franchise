package com.list.Franchise.tests;

import com.list.Franchise.controllers.BranchController;
import com.list.Franchise.controllers.FranchiseController;
import com.list.Franchise.entities.Request.BranchRequest;
import com.list.Franchise.entities.Request.FranchiseRequest;
import com.list.Franchise.entities.Request.ProductRequest;
import com.list.Franchise.entities.Request.UpdateNameRequest;
import com.list.Franchise.entities.Response.GetBranchResponse;
import com.list.Franchise.entities.Response.GetFranchiseResponse;
import com.list.Franchise.entities.Response.ProductsFromFranchiseResponse;
import com.list.Franchise.services.BranchService;
import com.list.Franchise.services.FranchiseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(FranchiseController.class)
public class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseService franchiseService;

    @Test
    public void addFranchiseTest_IsSuccessful() {
        GetFranchiseResponse franchiseResponse = new GetFranchiseResponse(1, "Bogota",10 );

        when(franchiseService.save(any(FranchiseRequest.class)))
                .thenReturn(Mono.just(franchiseResponse));
        webTestClient.post().uri("/api/franchise")
                .body(Mono.just(new FranchiseRequest("Bogota", 10)), FranchiseRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void addFranchiseTest_NotFound() {
        when(franchiseService.save(any(FranchiseRequest.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The provided franchise with Id: 10 doesn't exist")));

        webTestClient.post().uri("/api/franchise")
                .body(Mono.just(new FranchiseRequest("1231", 10)), FranchiseRequest.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateNameTest_IsSuccessful() {
        GetFranchiseResponse franchise = new GetFranchiseResponse(1, "Bogota",10 );
        when(franchiseService.updateName(1, "Bogota"))
                .thenReturn(Mono.just(franchise));
        webTestClient.put().uri("/api/franchise/1/name")
                .body(Mono.just(new UpdateNameRequest("Bogota")), UpdateNameRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateNameTest_InternalServerError() {
        when(franchiseService.updateName(any(Integer.class), any(String.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR)));

        webTestClient.put().uri("/api/franchise/1/name")
                .bodyValue(new UpdateNameRequest("Bogota"))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void getProductsStockByFranchise_IsSuccessful() {

        Integer franchiseId = 999;
        ProductsFromFranchiseResponse productsFromFranchiseResponse = new ProductsFromFranchiseResponse("Bogota",null);

        when(franchiseService.getProductsStockByFranchise(any(Integer.class)))
                .thenReturn(Mono.just(productsFromFranchiseResponse));

        webTestClient.get().uri("/api/franchise/999/products-stock", franchiseId)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    public void getProductsStockByFranchise_NotFound() {
        Integer franchiseId = 999;

        when(franchiseService.getProductsStockByFranchise(eq(franchiseId)))
                .thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Franchise not found with id: " + franchiseId)));

        webTestClient.get().uri("/api/franchise/999/products-stock", franchiseId)
                .exchange()
                .expectStatus().isNotFound();
    }
}
