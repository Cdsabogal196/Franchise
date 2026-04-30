package com.list.Franchise.entities.Response;

import com.list.Franchise.entities.Branch;
import com.list.Franchise.entities.Franchise;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductsFromFranchiseResponse {
    private String name;
    private List<BranchResponse> branches;

    public static ProductsFromFranchiseResponse fromFranchise(Franchise franchise, List<Branch> branches) {
        List<BranchResponse> branchResponses = branches.stream()
                .map(BranchResponse::fromBranch)
                .collect(Collectors.toList());
        return new ProductsFromFranchiseResponse(franchise.getName(), branchResponses);
    }
}
