package com.local.kevin.dal.support;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

/**
 *
 * �޸���ο� org.apache.ibatis.builder.xml.XMLConfigBuilder#settingsElement(org.apache.ibatis.parsing.XNode)
 * �����Ŀ���� ԭ���� SqlSessionFactoryBean �ڷ�xml ģʽ�� �������� Configuration
 * �����õ��� setMapUnderscoreToCamelCase �����������
 * ֱ�� Mybatis �ٷ��ṩ�������
 */
public class WrapAndChangeConfigureSqlSessionFactory extends SqlSessionFactoryBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        SqlSessionFactory sqlSessionFactory = super.getObject();

        Configuration configuration = sqlSessionFactory.getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
    }
}
