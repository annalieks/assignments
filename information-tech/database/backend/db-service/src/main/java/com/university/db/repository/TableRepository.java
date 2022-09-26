package com.university.db.repository;

import com.university.db.entity.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TableRepository extends MongoRepository<Table, String> {
    Optional<Table> findByName(String name);
}
