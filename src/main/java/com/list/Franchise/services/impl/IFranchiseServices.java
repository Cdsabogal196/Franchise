package com.list.Franchise.services.impl;

import com.list.Franchise.entities.Franchise;
import com.list.Franchise.entities.Request.FranchiseRequest;
import com.list.Franchise.entities.Response.GetFranchiseResponse;
import com.list.Franchise.entities.Response.ProductsFromFranchiseResponse;
import reactor.core.publisher.Mono;

public interface IFranchiseServices {
    Mono<GetFranchiseResponse> save(FranchiseRequest franchiseRequest);

    Mono<GetFranchiseResponse> updateName(Integer franchiseId, String newName);
    Mono<ProductsFromFranchiseResponse> getProductsStockByFranchise(Integer franchiseId);
}
