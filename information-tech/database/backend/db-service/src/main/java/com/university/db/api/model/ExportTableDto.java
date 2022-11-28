package com.university.db.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.university.db.api.model.ColumnMetadataDto;
import com.university.db.api.model.RowMetadataDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExportTableDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-11-23T05:08:40.079908200+01:00[Europe/Berlin]")
public class ExportTableDto   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("columns")
  @Valid
  private List<ColumnMetadataDto> columns = null;

  @JsonProperty("rows")
  @Valid
  private List<RowMetadataDto> rows = null;

  public ExportTableDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ExportTableDto columns(List<ColumnMetadataDto> columns) {
    this.columns = columns;
    return this;
  }

  public ExportTableDto addColumnsItem(ColumnMetadataDto columnsItem) {
    if (this.columns == null) {
      this.columns = new ArrayList<ColumnMetadataDto>();
    }
    this.columns.add(columnsItem);
    return this;
  }

  /**
   * Get columns
   * @return columns
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ColumnMetadataDto> getColumns() {
    return columns;
  }

  public void setColumns(List<ColumnMetadataDto> columns) {
    this.columns = columns;
  }

  public ExportTableDto rows(List<RowMetadataDto> rows) {
    this.rows = rows;
    return this;
  }

  public ExportTableDto addRowsItem(RowMetadataDto rowsItem) {
    if (this.rows == null) {
      this.rows = new ArrayList<RowMetadataDto>();
    }
    this.rows.add(rowsItem);
    return this;
  }

  /**
   * Get rows
   * @return rows
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<RowMetadataDto> getRows() {
    return rows;
  }

  public void setRows(List<RowMetadataDto> rows) {
    this.rows = rows;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExportTableDto exportTableDto = (ExportTableDto) o;
    return Objects.equals(this.name, exportTableDto.name) &&
        Objects.equals(this.columns, exportTableDto.columns) &&
        Objects.equals(this.rows, exportTableDto.rows);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, columns, rows);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExportTableDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
    sb.append("    rows: ").append(toIndentedString(rows)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
