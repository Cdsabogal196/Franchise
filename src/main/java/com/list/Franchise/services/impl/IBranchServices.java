package com.list.Franchise.services.impl;

import com.list.Franchise.entities.Branch;
import com.list.Franchise.entities.Franchise;
import com.list.Franchise.entities.Request.BranchRequest;
import com.list.Franchise.entities.Response.GetBranchResponse;
import reactor.core.publisher.Mono;

public interface IBranchServices {
    Mono<GetBranchResponse> save(BranchRequest branchRequest);
    Mono<GetBranchResponse> updateName(Integer productId,String newName);
}
