package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappedStatement {

    //sql语句
    private String sql;
    //结果类型
    private String resultType;
    //id标识
    private String id;
    //参数类型
    private String paramterType;
}
