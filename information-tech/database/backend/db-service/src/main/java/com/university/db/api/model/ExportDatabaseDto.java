package com.university.db.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.university.db.api.model.ExportTableDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExportDatabaseDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-11-23T05:08:40.079908200+01:00[Europe/Berlin]")
public class ExportDatabaseDto   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("tables")
  @Valid
  private List<ExportTableDto> tables = null;

  public ExportDatabaseDto name(String name) {
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

  public ExportDatabaseDto tables(List<ExportTableDto> tables) {
    this.tables = tables;
    return this;
  }

  public ExportDatabaseDto addTablesItem(ExportTableDto tablesItem) {
    if (this.tables == null) {
      this.tables = new ArrayList<ExportTableDto>();
    }
    this.tables.add(tablesItem);
    return this;
  }

  /**
   * Get tables
   * @return tables
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ExportTableDto> getTables() {
    return tables;
  }

  public void setTables(List<ExportTableDto> tables) {
    this.tables = tables;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExportDatabaseDto exportDatabaseDto = (ExportDatabaseDto) o;
    return Objects.equals(this.name, exportDatabaseDto.name) &&
        Objects.equals(this.tables, exportDatabaseDto.tables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, tables);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExportDatabaseDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tables: ").append(toIndentedString(tables)).append("\n");
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
