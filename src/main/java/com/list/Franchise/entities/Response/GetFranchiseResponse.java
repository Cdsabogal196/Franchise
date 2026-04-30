package com.list.Franchise.entities.Response;

import com.list.Franchise.entities.Franchise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFranchiseResponse {
    private Integer id;
    private String name;
    private Integer stock;

    public static GetFranchiseResponse fromFranchise(Franchise franchise) {
        return new GetFranchiseResponse(franchise.getId(), franchise.getName(), franchise.getStock());
    }
}
