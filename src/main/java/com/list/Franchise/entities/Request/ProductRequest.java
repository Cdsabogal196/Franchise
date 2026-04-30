package com.list.Franchise.entities.Request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {

    private String name;

    private Integer stock;

    private Integer branchId;
}
