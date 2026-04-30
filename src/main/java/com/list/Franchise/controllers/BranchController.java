package com.list.Franchise.controllers;


import com.list.Franchise.entities.Request.BranchRequest;
import com.list.Franchise.entities.Request.UpdateNameRequest;
import com.list.Franchise.entities.Response.GetBranchResponse;
import com.list.Franchise.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GetBranchResponse> save(@RequestBody BranchRequest request) throws Exception {

        try {
            return branchService.save(request);

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/{branchId}/name")
    public Mono<ResponseEntity<Map<String, Object>>> updateBranchName(@PathVariable Integer branchId, @RequestBody UpdateNameRequest updateNameRequest) {
        Map<String, Object> response = new HashMap<>();
        return branchService.updateName(branchId, updateNameRequest.getName())
                .map(product -> {
                    response.put("message", "Product name updated successfully");
                    response.put("name", product.getName());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(ResponseStatusException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body(Map.of("message", ex.getReason()))
                        )
                );
    }


}
