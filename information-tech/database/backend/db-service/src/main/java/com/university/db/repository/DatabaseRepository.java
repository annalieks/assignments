package com.university.db.repository;

import com.university.db.entity.Database;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DatabaseRepository extends MongoRepository<Database, String> {
    Optional<Database> findByName(String name);
}
