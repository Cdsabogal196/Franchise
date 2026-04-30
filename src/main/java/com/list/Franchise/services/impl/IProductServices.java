package com.list.Franchise.services.impl;

import com.list.Franchise.entities.Product;
import com.list.Franchise.entities.Request.ProductRequest;
import com.list.Franchise.entities.Response.GetProductResponse;
import org.hibernate.internal.util.StringHelper;
import reactor.core.publisher.Mono;

public interface IProductServices {
    Mono<GetProductResponse> save(ProductRequest productRequest);
    Mono<GetProductResponse> deleteProductInBranch(Integer productId);
    Mono<GetProductResponse> updateProductStock(Integer productId, Integer newStock);
    Mono<GetProductResponse> updateName(Integer productId, String newName);
}
