package com.samples.config;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 *@author : wh
 *@date : 2022/7/20 11:54
 *@description:
 */
@Configuration
@MapperScan("com.samples.mapper")
public class MybatisPlusConfig {

	@ConfigurationProperties(prefix = "db.pg")
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}


	@Bean
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		factoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
		MybatisConfiguration configuration = new MybatisConfiguration();
		//开启下划线转驼峰
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setBanner(false);
		factoryBean.setGlobalConfig(globalConfig);
		factoryBean.setConfiguration(configuration);
		return factoryBean;
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}
