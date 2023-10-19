package com.org.os.persistance.repository;

import com.org.os.persistance.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Token, Integer> {

    @Query(
            value = "SELECT * FROM TOKEN t INNER JOIN USERS u " +
                    "using (USER_ID) " +
                    "where t.IS_ACTIVE = 'Y'",
            nativeQuery = true
    )
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByTokenValue(String token);
}
