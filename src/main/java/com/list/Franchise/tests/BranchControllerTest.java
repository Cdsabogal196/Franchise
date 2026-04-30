package com.list.Franchise.tests;

import com.list.Franchise.controllers.BranchController;
import com.list.Franchise.entities.Product;
import com.list.Franchise.entities.Request.BranchRequest;
import com.list.Franchise.entities.Request.UpdateNameRequest;
import com.list.Franchise.entities.Response.GetBranchResponse;
import com.list.Franchise.services.BranchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BranchController.class)
public class BranchControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BranchService branchService;

    @Test
    public void addBranchTest_IsSuccessful() {
        GetBranchResponse branch = new GetBranchResponse(1, "Bogota",10 );

        when(branchService.save(any(BranchRequest.class)))
                .thenReturn(Mono.just(branch));
        webTestClient.post().uri("/api/branch")
                .body(Mono.just(new BranchRequest(1, "Bogota", 10, "Bogota")), BranchRequest.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void addBranchTest_NotFound() {
        when(branchService.save(any(BranchRequest.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The provided franchise with Id: 10 doesn't exist")));

        webTestClient.post().uri("/api/branch")
                .bodyValue(new BranchRequest(1, "Bogota", 10, "Bogota"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateNameTest_IsSuccessful() {
        GetBranchResponse branch = new GetBranchResponse(1, "Bogota",10 );

        when(branchService.updateName(1, "Bogota"))
                .thenReturn(Mono.just(branch));
        webTestClient.put().uri("/api/branch/1/name")
                .body(Mono.just(new UpdateNameRequest("Bogota")), UpdateNameRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateNameTest_InternalServerError() {
        when(branchService.updateName(any(Integer.class), any(String.class)))
                .thenReturn(Mono.error(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR)));

        webTestClient.put().uri("/api/branch/1/name")
                .bodyValue(new UpdateNameRequest("Bogota"))
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
