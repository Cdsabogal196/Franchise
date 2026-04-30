package com.list.Franchise.entities.Response;

import com.list.Franchise.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {
    private Integer id;
    private String name;
    private Integer stock;
    private Integer branchId;

    public static GetProductResponse fromProduct(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                product.getBranch() != null ? product.getBranch().getId() : null
        );
    }
}