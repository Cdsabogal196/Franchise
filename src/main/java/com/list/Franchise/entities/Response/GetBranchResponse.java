package com.list.Franchise.entities.Response;

import com.list.Franchise.entities.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBranchResponse {
    private Integer id;

    private String name;
    private Integer franchiseId;

    public static GetBranchResponse fromBranch(Branch branch) {
        return new GetBranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getFranchise() != null ? branch.getFranchise().getId() : null
        );
    }


}
