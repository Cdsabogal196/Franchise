package com.list.Franchise.services;

import com.list.Franchise.entities.Branch;
import com.list.Franchise.entities.Request.BranchRequest;
import com.list.Franchise.entities.Response.GetBranchResponse;
import com.list.Franchise.repositories.BranchRepository;
import com.list.Franchise.repositories.FranchiseRepository;
import com.list.Franchise.services.impl.IBranchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class BranchService implements IBranchServices {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Override
    public Mono<GetBranchResponse> save(BranchRequest branchRequest) {
        Branch branch = branchRequest.toEntity();
        Integer franchiseId = branch.getFranchise().getId();

        return Mono.fromCallable(() -> franchiseRepository.findById(franchiseId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optional -> optional
                        .map(franchise -> Mono.fromCallable(() -> branchRepository.save(branch))
                                .subscribeOn(Schedulers.boundedElastic())
                                .map(GetBranchResponse::fromBranch))
                        .orElseGet(() -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "The provided franchise with Id: " + franchiseId + " doesn't exist")))
                );
    }

    @Override
    public Mono<GetBranchResponse> updateName(Integer branchId, String newName) {
        return Mono.fromCallable(() -> {
                    Branch branch = branchRepository.findById(branchId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Branch not found with id: " + branchId));
                    branch.setName(newName);
                    return branchRepository.save(branch);
                }).subscribeOn(Schedulers.boundedElastic())
                .map(GetBranchResponse::fromBranch);
    }




}
