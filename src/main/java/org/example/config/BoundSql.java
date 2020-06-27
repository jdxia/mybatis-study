package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

//解析过后的sql
@Data
@AllArgsConstructor
public class BoundSql {

    private String sqlText;

    private List<ParameterMapping> parameterMappingList = new ArrayList<>();

}
