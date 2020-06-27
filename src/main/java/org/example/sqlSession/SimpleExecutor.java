package org.example.sqlSession;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.example.config.BoundSql;
import org.example.pojo.Configuration;
import org.example.pojo.MappedStatement;
import org.example.utils.GenericTokenParser;
import org.example.utils.ParameterMapping;
import org.example.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1. 注册驱动, 获取连接
        Connection connection = configuration.getDataSource().getConnection();
        //2. 获取sql语句 : select * from user where id = #{id} and username = #{username}
        //3. 转换sql语句:select * from user where id = #{id} and username = #{username} 转换的过程中, 对你妹的 #{} 进行解析存储
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //3. 预处理对象: preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4. 设置参数
        //获取参数的全路径
        String paramterType = mappedStatement.getParamterType();
        Class<?> paramtertypeClass = getClassType(paramterType);

        //把参数映射进去
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);

            //暴力访问
            declaredField.setAccessible(Boolean.TRUE);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1, o);
        }

        //5. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        //存放结果集
        ArrayList<Object> objects = new ArrayList<>();

        //6. 封装返回结果集
        while (resultSet.next()) {
            Object o =resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            //从1开始, 从0开始这边就报错了
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);


            }
            objects.add(o);
        }

        return (List<E>) objects;
    }


    //完成对#{}的解析工作, 1. 将 #{} 用?代替, 2. 解析里面的 #{}的值并进行存储
    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;
    }

    //给一个参数类型获取参数的类
    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if (paramterType != null) {
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;
    }

}
