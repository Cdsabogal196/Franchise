package com.list.Franchise.entities.Response;

import com.list.Franchise.entities.Branch;
import com.list.Franchise.entities.Request.ProductRequest;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BranchResponse {
    private Integer id;
    private String name;
    private List<ProductResponse> products;


    public static BranchResponse fromBranch(Branch branch) {
        List<ProductResponse> products = branch.getProducts().stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
        return new BranchResponse(branch.getId(), branch.getName(), products);
    }

}
