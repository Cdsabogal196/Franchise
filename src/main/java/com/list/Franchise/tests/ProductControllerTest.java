package com.list.Franchise.tests;

import com.list.Franchise.controllers.BranchController;
import com.list.Franchise.controllers.ProductController;
import com.list.Franchise.entities.Product;
import com.list.Franchise.entities.Request.BranchRequest;
import com.list.Franchise.entities.Request.ProductRequest;
import com.list.Franchise.entities.Request.UpdateNameRequest;
import com.list.Franchise.entities.Request.UpdateStockRequest;
import com.list.Franchise.entities.Response.GetBranchResponse;
import com.list.Franchise.entities.Response.GetFranchiseResponse;
import com.list.Franchise.entities.Response.GetProductResponse;
import com.list.Franchise.entities.Response.ProductResponse;
import com.list.Franchise.services.BranchService;
import com.list.Franchise.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService productService;

    @Test
    public void addProductTest_IsSuccessful() {
        GetProductResponse product = new GetProductResponse(1,"celular",2,1);

        when(productService.save(any(ProductRequest.class)))
                .thenReturn(Mono.just(product));
        webTestClient.post().uri("/api/product")
                .body(Mono.just(new ProductRequest( "Bogota", 3, 1)), BranchRequest.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void addProductTest_NotFound() {
        when(productService.save(any(ProductRequest.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The provided franchise with Id: 10 doesn't exist")));

        webTestClient.post().uri("/api/prodcut")
                .bodyValue(new ProductRequest("Bogota", 10, 3))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateNameTest_IsSuccessful() {
        GetProductResponse product = new GetProductResponse(1,"celular",2,1);

        when(productService.updateName(any(Integer.class), any(String.class)))
                .thenReturn(Mono.just(product));
        webTestClient.put().uri("/api/product/1/name")
                .body(Mono.just(new UpdateNameRequest("Bogota")), UpdateNameRequest.class)
                .exchange()
                .expectStatus().isOk();
    }



    @Test
    public void updateNameTest_InternalServerError() {
        when(productService.updateName(any(Integer.class), any(String.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR)));

        webTestClient.put().uri("/api/product/1/name")
                .bodyValue(new UpdateNameRequest("Bogota"))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void deleteBranchFromProduct_IsSuccessful() {
        GetProductResponse product = new GetProductResponse(1, "celular", 2, null);

        when(productService.deleteProductInBranch(any(Integer.class)))
                .thenReturn(Mono.just(product));

        webTestClient.delete().uri("/api/product/1/branch")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void deleteBranchFromProduct_NotFound() {
        when(productService.deleteProductInBranch(any(Integer.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with id: 1")));

        webTestClient.delete().uri("/api/product/1/branch")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateStock_IsSuccessful() {
        GetProductResponse product = new GetProductResponse(1, "celular", 2, null);

        when(productService.updateProductStock(any(Integer.class),any(Integer.class)))
                .thenReturn(Mono.just(product));

        webTestClient.put().uri("/api/product/1/stock")
                .body(Mono.just(new UpdateStockRequest( 2)), UpdateStockRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateStock_NotFound() {
        when(productService.updateProductStock(any(Integer.class),any(Integer.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with id: 1")));

        webTestClient.put().uri("/api/product/1/stock")
                .body(Mono.just(new UpdateStockRequest( 2)), UpdateStockRequest.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
