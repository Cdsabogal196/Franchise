package com.list.Franchise.services;

import com.list.Franchise.entities.Branch;
import com.list.Franchise.entities.Franchise;
import com.list.Franchise.entities.Product;
import com.list.Franchise.entities.Request.ProductRequest;
import com.list.Franchise.entities.Response.GetFranchiseResponse;
import com.list.Franchise.entities.Response.GetProductResponse;
import com.list.Franchise.repositories.BranchRepository;
import com.list.Franchise.repositories.ProductRepository;
import com.list.Franchise.services.impl.IProductServices;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;


@Service
public class ProductService implements IProductServices {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Override
    public Mono<GetProductResponse> save(ProductRequest productRequest) {
        return Mono.fromCallable(() -> {
                    Branch branch = branchRepository.findById(productRequest.getBranchId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "The provided branch with Id: " + productRequest.getBranchId() + " doesn't exist"
                            ));

                    Product product = new Product();
                    product.setName(productRequest.getName());
                    product.setStock(productRequest.getStock());
                    product.setBranch(branch);

                    return productRepository.save(product);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(GetProductResponse::fromProduct);
    }
    @Override
    public Mono<GetProductResponse> deleteProductInBranch(Integer productId) {

        return Mono.fromCallable(() -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found with id: " + productId));

            if (product.getBranch() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Product has no branch assigned, cannot delete non-existent relation");
            }

            Integer branchId = product.getBranch().getId();
            if (branchId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Product's branch has null id");
            }
            branchRepository.findById(branchId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Branch not found with id: " + branchId));

            product.setBranch(null);

            return productRepository.save(product);
        }).subscribeOn(Schedulers.boundedElastic())
                .map(GetProductResponse::fromProduct);
    }

    @Override
    public Mono<GetProductResponse> updateName(Integer productId,String newName) {

        return Mono.fromCallable(() -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found with id: " + productId));

            product.setName(newName);

            return productRepository.save(product);
                }).subscribeOn(Schedulers.boundedElastic())
                .map(GetProductResponse::fromProduct);
    }

    @Override
    public Mono<GetProductResponse> updateProductStock(Integer productId, Integer newStock) {
        return Mono.fromCallable(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Product not found with id: " + productId));
                    product.setStock(newStock);
                    return productRepository.save(product);
                }).subscribeOn(Schedulers.boundedElastic())
                .map(GetProductResponse::fromProduct);
    }

}
