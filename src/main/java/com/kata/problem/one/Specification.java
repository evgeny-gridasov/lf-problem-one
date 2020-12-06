package com.kata.problem.one;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Specification {

    @JsonProperty("FixedWidthEncoding")
    private String fixedWidthEncoding;

    @JsonProperty("DelimitedEncoding")
    private String delimitedEncoding;

    @JsonProperty("IncludeHeader")
    private boolean includeHeader;

    @JsonProperty("ColumnNames")
    private List<String> columnNames;

    @JsonProperty("Offsets")
    private List<Integer> offsets;

    public String getFixedWidthEncoding() {
        return fixedWidthEncoding;
    }

    public void setFixedWidthEncoding(String fixedWidthEncoding) {
        this.fixedWidthEncoding = fixedWidthEncoding;
    }

    public String getDelimitedEncoding() {
        return delimitedEncoding;
    }

    public void setDelimitedEncoding(String delimitedEncoding) {
        this.delimitedEncoding = delimitedEncoding;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void setOffsets(List<Integer> offsets) {
        this.offsets = offsets;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "fixedWidthEncoding='" + fixedWidthEncoding + '\'' +
                ", delimitedEncoding='" + delimitedEncoding + '\'' +
                ", includeHeader=" + includeHeader +
                ", columnNames=" + columnNames +
                ", offsets=" + offsets +
                '}';
    }
}
