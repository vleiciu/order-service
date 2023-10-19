package com.org.os.persistance.repository;

import com.org.os.persistance.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer> {
}
