package com.university.db.dto.intersect;

import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.dto.metadata.RowMetadataDto;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class IntersectionTableDto extends RepresentationModel<IntersectionTableDto> {

    private List<ColumnMetadataDto> columns = new ArrayList<>();
    private List<RowMetadataDto> rows = new ArrayList<>();

}
