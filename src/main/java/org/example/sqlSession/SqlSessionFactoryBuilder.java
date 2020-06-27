package org.example.sqlSession;

import lombok.extern.java.Log;
import org.dom4j.DocumentException;
import org.example.config.XMLConfigBuilder;
import org.example.pojo.Configuration;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    /**
     * 构建sqlSessionFactory, 创建sqlSession的工厂
     * 对xml解析, 创建出数据源还有把sql全部解析下来
     *
     * @return SqlSessionFactory
     * @param resourceAsSteam
     */
    public SqlSessionFactory build(InputStream resourceAsSteam) throws DocumentException, PropertyVetoException {
        //解析config文件的工具类
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        //解析完, 返回封装解析的对象
        Configuration configuration = xmlConfigBuilder.parseConfig(resourceAsSteam);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        return sqlSessionFactory;
    }
}
