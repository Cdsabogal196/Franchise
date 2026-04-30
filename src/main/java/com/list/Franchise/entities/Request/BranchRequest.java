package com.list.Franchise.entities.Request;

import com.list.Franchise.entities.Branch;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BranchRequest {

    private Integer id;
    private String name;
    private Integer franchiseId;
    private String franchiseName;

    public Branch toEntity() {
        return new Branch(this.name, this.franchiseId);
    }
}
