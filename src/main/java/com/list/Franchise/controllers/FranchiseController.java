package com.list.Franchise.controllers;


import com.list.Franchise.entities.Request.FranchiseRequest;
import com.list.Franchise.entities.Request.UpdateNameRequest;
import com.list.Franchise.entities.Response.ProductsFromFranchiseResponse;
import com.list.Franchise.services.BranchService;
import com.list.Franchise.services.FranchiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/franchise")
public class FranchiseController {

    @Autowired
    private FranchiseService franchiseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Object>> save(@RequestBody FranchiseRequest franchiseRequest) {

        return franchiseService.save(franchiseRequest)
                .map(franchiseSaved -> ResponseEntity.ok((Object) franchiseSaved))
                .onErrorResume(ResponseStatusException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body(Map.of("message", ex.getReason()))
                        )
                );
    }

    @PutMapping("/{franchiseId}/name")
    public Mono<ResponseEntity<Map<String, Object>>> updateFranchiseName(@PathVariable Integer franchiseId, @RequestBody UpdateNameRequest updateNameRequest) {
        return franchiseService.updateName(franchiseId, updateNameRequest.getName())
                .map(product -> ResponseEntity.ok()
                        .body(Map.of(
                                "message", "Franchise updated successfully",
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

    @GetMapping("/{franchiseId}/products-stock")
    public Mono<ProductsFromFranchiseResponse> getProductsStockByFranchise(
            @PathVariable Integer franchiseId) {

        return franchiseService.getProductsStockByFranchise(franchiseId);
    }
}
