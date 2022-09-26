package com.university.db.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.db.entity.Database;
import com.university.db.exception.ConflictException;
import com.university.db.repository.DatabaseRepository;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DatabaseService(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public Database create(String name) throws ConflictException {
        Optional<Database> existingDb = databaseRepository.findByName(name);
        if (existingDb.isPresent()) {
            throw new ConflictException(String.format("Database with name %s already exists", name));
        }
        Database db = new Database();
        db.setName(name);
        return databaseRepository.save(db);
    }

    public Optional<Database> findById(String id) {
        return databaseRepository.findById(id);
    }

    public Optional<Database> findByName(String name) {
        return databaseRepository.findByName(name);
    }

    public List<Database> findAll() {
        return databaseRepository.findAll();
    }

    public byte[] convertToFile(Database database) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(database);
        return json.getBytes(StandardCharsets.UTF_8);
    }

}
