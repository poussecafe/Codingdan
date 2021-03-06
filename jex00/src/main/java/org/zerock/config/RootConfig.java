package org.zerock.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
// root-context.xml에 componentScan 설정하는 것과 동일, 
@ComponentScan(basePackages = { "org.zerock.sample" })
// 지정된 패키지의 모든 MyBatis 관련 어노테이션을 찾아서 처리한다
@MapperScan(basePackages= {"org.zerock.mapper"})
public class RootConfig {

	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		
		// hikariConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// hikariConfig.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		
		// log4jdbc를 이용하는 경우에는 JDBC 드라이버와 URL 정보를 수정해야 한다
		hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		hikariConfig.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@localhost:1521:ORCL");
		
		hikariConfig.setUsername("book_ex");
		hikariConfig.setPassword("1234");

		HikariDataSource dataSource = new HikariDataSource(hikariConfig);

		return dataSource;
	}
	
	// sqlSession을 통해 커넥션을 생성하거나 원하는 SQL을 전달하고 결과를 리턴 받는다
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		return (SqlSessionFactory)sqlSessionFactory.getObject();
	}

}
