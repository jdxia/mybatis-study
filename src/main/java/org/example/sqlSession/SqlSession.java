package org.example.sqlSession;

import java.util.List;

//定义增删改查和生成动态代理类
public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementid, Object... params) throws Exception;

    //查询单个
    public <T> T selectOne(String statementid, Object... params) throws Exception;

    //为Dao接口生成代理类
    public <T> T getMapper(Class<?> mapperClass);
}
