package com.university.db.repository;

import com.university.db.entity.Column;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColumnRepository extends MongoRepository<Column, Long> {

}
