package com.university.db.repository;

import com.university.db.entity.Column;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ColumnRepository extends MongoRepository<Column, String> {

    Optional<Column> findByName(String name);

}
