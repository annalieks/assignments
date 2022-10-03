package com.university.db.repository;

import com.university.db.entity.Row;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RowRepository extends MongoRepository<Row, String> {

}
