package com.list.Franchise.repositories;

import com.list.Franchise.entities.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise,Integer>{

}
