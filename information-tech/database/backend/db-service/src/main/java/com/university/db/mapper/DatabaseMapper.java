package com.university.db.mapper;

import com.university.db.dto.DatabaseDto;
import com.university.db.entity.Database;

public class DatabaseMapper {

    public static DatabaseDto databaseToDatabaseDto(Database d) {
        DatabaseDto dto = new DatabaseDto();
        dto.setId(d.getId());
        dto.setName(d.getName());
        return dto;
    }

}
