package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private DataSource dataSource;
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

}
