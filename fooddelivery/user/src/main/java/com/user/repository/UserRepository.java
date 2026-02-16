package com.user.repository;

import com.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String userId);

    Optional<User> findByEmailAndActive(String email,Boolean active);

    Optional<User> findById(String userId);

    Optional<User> findByIdAndActive(String s, Boolean active);
}
