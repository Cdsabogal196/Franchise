package com.list.Franchise.repositories;
import com.list.Franchise.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BranchRepository extends JpaRepository <Branch,Integer>{
    List<Branch> findProductByFranchiseId(Integer franchiseId);

}

