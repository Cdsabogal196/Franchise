package com.list.Franchise.services;

import com.list.Franchise.entities.Branch;
import com.list.Franchise.entities.Franchise;
import com.list.Franchise.entities.Product;
import com.list.Franchise.entities.Request.FranchiseRequest;
import com.list.Franchise.entities.Response.*;
import com.list.Franchise.repositories.BranchRepository;
import com.list.Franchise.repositories.FranchiseRepository;
import com.list.Franchise.services.impl.IFranchiseServices;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Service
public class FranchiseService implements IFranchiseServices {

    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Override
    public Mono<GetFranchiseResponse> save(FranchiseRequest franchiseRequest) {
        return Mono.fromCallable(() -> franchiseRepository.save(franchiseRequest.toEntity()))
                .subscribeOn(Schedulers.boundedElastic())
                .map(GetFranchiseResponse::fromFranchise);
    }

    @Override
    public Mono<GetFranchiseResponse> updateName(Integer franchiseId, String newName) {
        return Mono.fromCallable(() -> {
            Franchise franchise = franchiseRepository.findById(franchiseId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Franchise not found with id: " + franchiseId));
            franchise.setName(newName);
            return franchiseRepository.save(franchise);
        }).subscribeOn(Schedulers.boundedElastic())
                .map(GetFranchiseResponse::fromFranchise);
    }
    @Override
    public Mono<ProductsFromFranchiseResponse> getProductsStockByFranchise(Integer franchiseId) {
        return Mono.fromCallable(() -> {
            Franchise franchise = franchiseRepository.findById(franchiseId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Franchise not found with id: " + franchiseId));

            List<Branch> branches = branchRepository.findProductByFranchiseId(franchiseId);
            return ProductsFromFranchiseResponse.fromFranchise(franchise, branches);

        }).subscribeOn(Schedulers.boundedElastic());
    }

}
