package com.list.Franchise.entities.Request;

import com.list.Franchise.entities.Franchise;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FranchiseRequest {

    private String name;

    private Integer stock;

    public Franchise toEntity() {
        return new Franchise(null, this.name, this.stock, null);
    }

}
