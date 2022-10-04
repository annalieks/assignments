package com.university.db.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.db.dto.DatabaseDto;
import com.university.db.dto.TableDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.dto.metadata.RowMetadataDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.dto.serialization.ExportDatabaseDto;
import com.university.db.dto.serialization.ExportTableDto;
import com.university.db.entity.Database;
import com.university.db.entity.Table;
import com.university.db.exception.EntityException;
import com.university.db.exception.FileFormatException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.TableMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class SerializationService {

    private final DatabaseService databaseService;
    private final TableService tableService;
    private final RowService rowService;
    private final ColumnService columnService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExportDatabaseDto export(String id) throws NotFoundException {
        ExportDatabaseDto databaseDto = new ExportDatabaseDto();
        Database fullDb = databaseService.findDatabaseById(id);
        databaseDto.setName(fullDb.getName());
        for (Table table : fullDb.getTables()) {
            Table fullTable = tableService.findTableById(table.getId());
            databaseDto.getTables().add(TableMapper.tableToExportedTableDto(fullTable));
        }
        return databaseDto;
    }

    public void importFile(MultipartFile file) throws FileFormatException {
        try {
            ExportDatabaseDto dto = objectMapper.readValue(file.getBytes(), ExportDatabaseDto.class);
            importDb(dto);
        } catch (Exception e) {
            throw new FileFormatException("Provided file could not be processed. Reason: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void importDb(ExportDatabaseDto dto) throws EntityException {
        DatabaseDto db = databaseService.create(dto.getName());
        for (ExportTableDto tableDto : dto.getTables()) {
            TableMetadataDto tableMetadataDto = TableMapper.exportedTableDtoToTableMetadataDto(tableDto);
            TableDto table = tableService.create(db.getId(), tableMetadataDto);

            for (ColumnMetadataDto columnDto : tableDto.getColumns()) {
                columnService.create(table.getId(), columnDto);
            }
            for (RowMetadataDto rowDto : tableDto.getRows()) {
                rowService.create(table.getId(), rowDto);
            }
        }
    }

}
