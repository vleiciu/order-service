package com.org.os.persistance.repository;

import com.org.os.persistance.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findById(Integer id);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
}
