package com.restraunt.repository;

import com.restraunt.enums.RestrauntStatus;
import com.restraunt.model.Restraunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestrauntRepository extends JpaRepository<Restraunt,String> {

    Optional<Restraunt> findByRestrauntNameAndLatitudeAndLongitude(String s, Double latitude, Double longitude);

    List<Restraunt> findAllByStatus(RestrauntStatus restrauntStatus);
}
