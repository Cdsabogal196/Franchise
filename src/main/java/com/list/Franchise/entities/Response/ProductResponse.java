package com.list.Franchise.entities.Response;

import com.list.Franchise.entities.Product;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String name;
    private Integer stock;

    public static ProductResponse fromProduct(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }
}
