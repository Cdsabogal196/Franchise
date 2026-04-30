package com.list.Franchise.controllers;


import com.list.Franchise.entities.Request.ProductRequest;
import com.list.Franchise.entities.Request.UpdateNameRequest;
import com.list.Franchise.entities.Request.UpdateStockRequest;
import com.list.Franchise.entities.Response.GetProductResponse;
import com.list.Franchise.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GetProductResponse> create(@RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @DeleteMapping("/{productId}/branch")
    public Mono<ResponseEntity<Map<String, Object>>> deleteBranchFromProduct(@PathVariable Integer productId) {
        return productService.deleteProductInBranch(productId).map(product -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Product branch relation deleted successfully");
                    response.put("name", product.getName());
                    response.put("stock", product.getStock());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(ResponseStatusException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body(Map.of("message", ex.getReason()))
                        )
                );
    }

    @PutMapping("/{productId}/stock")
    public Mono<GetProductResponse> updateStock(@PathVariable Integer productId, @RequestBody UpdateStockRequest updateStockRequest) {
        return productService.updateProductStock(productId, updateStockRequest.getStock());

    }

    @PutMapping("/{productId}/name")
    public Mono<ResponseEntity<Map<String, Object>>> updateProductName(@PathVariable Integer productId, @RequestBody UpdateNameRequest updateNameRequest) {
        return productService.updateName(productId, updateNameRequest.getName())
                .map(product -> ResponseEntity.ok()
                        .body(Map.of(
                                "message", "Product updated successfully",
                                "product", product
                        ))
                )
                .onErrorResume(ResponseStatusException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body(Map.of("message", ex.getReason()))
                        )
                );
    }

}
