package org.example.sqlSession;

import org.example.pojo.Configuration;
import org.example.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {

        //将要完成对simpleExecutor里的query方法调用
        Executor simpleExecutor = new SimpleExecutor();
        //获取statementId 对应的 sql对象
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(this.configuration, mappedStatement, params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {

        List<Object> list = selectList(statementId, params);
        if (list.size() == 1) {
            return (T) list.get(0);
        } else  {
            throw new RuntimeException("返回的结果集过多");
        }
    }

    //用动态代理生成对象
    @Override
    public <T> T getMapper(Class<?> mapperClass) {

        Object proxyInstance = Proxy.newProxyInstance(
                DefaultSqlSession.class.getClassLoader(),
                new Class[]{mapperClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //底层还都是执行JDBC代码, 根据不同的情况, 调用不同的方法
                        String methodName = method.getName();
                        String className = method.getDeclaringClass().getName();

                        String statementId = className + "." + methodName;

                        //准备参数, params:args
                        //获取被调用方法的返回值
                        Type genericReturnType = method.getGenericReturnType();
                        //判断是否进行了 泛型类型参数化
                        if (genericReturnType instanceof ParameterizedType) {
                            List<Object> objects = selectList(statementId, args);
                            return objects;
                        }
                        return selectOne(statementId, args);
                    }
                });

        return (T) proxyInstance;
    }
}
