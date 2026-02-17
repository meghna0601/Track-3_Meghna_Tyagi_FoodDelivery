package com.restraunt.repository;

import com.restraunt.model.SuspendRestraunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspendRestrauntRepository extends JpaRepository<SuspendRestraunt,String> {
}
