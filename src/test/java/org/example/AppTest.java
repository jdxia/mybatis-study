package org.example;


import org.example.dao.IUserDao;
import org.example.io.Resources;
import org.example.pojo.User;
import org.example.sqlSession.SqlSession;
import org.example.sqlSession.SqlSessionFactory;
import org.example.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class AppTest {

    @Test
    public void testQuery() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("SqlMapConfig.xml");

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //创建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsSteam);
        //创建sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();

        IUserDao dao = sqlSession.getMapper(IUserDao.class);

        //根据dao创建动态代理对象
        System.out.println("================selectAllUser====================");
        List<User> users = dao.selectAllUser();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("================selectAllUser====================");

        //创建user对象
        System.out.println("================selectUser====================");
        User user = new User();
        user.setId(40);
        user.setUsername("xjd");
        User selectUser = dao.selectUser(user);
        System.out.println(selectUser);
        System.out.println("================selectUser====================");
    }



}
