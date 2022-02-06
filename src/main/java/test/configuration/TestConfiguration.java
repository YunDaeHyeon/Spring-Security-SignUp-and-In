package test.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties") // (1)
public class TestConfiguration {
    /* (1)
     * application.properties를 사용할 수 있도록 설정 파일의 위치 지정.
     * @PropertySource는 기본 제공이며 @PropertySource을 사용하여 다른 설정 파일도 사용할 수 있음.
     */

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari") //(2)
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }
    /* (2)
     * application.properties 설정한 DB 관련 정보를 사용할 수 있도록 지정.
     * @ConfigurationProperties 어노테이션에 prefix가 spring.datasource.hikari로 설정되어 있기에 spring.datasoure.hikari로 시작하는
     * 설정을 이용하여 히카리CP의 설정 파일을 제작함.
     */

    @Bean
    public DataSource dataSource() throws Exception{ // (3)
        DataSource dataSource = new HikariDataSource(hikariConfig());
        System.out.println(dataSource.toString());
        return dataSource;
    }
    /* (3)
     * 히카리CP의 설정 파일을 이용하여 DB와 연결하는 데이터 소스 생성
     */

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();// (4)
        sqlSessionFactoryBean.setDataSource(dataSource);// (5)
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/sqlquery/**/*.xml"));// (6)

        sqlSessionFactoryBean.setConfiguration(mybatisConfig());

        return sqlSessionFactoryBean.getObject();
    }
    /* (4)
     * Spring-MyBatis에서는 SqlSessionFactory를 생성하기 위해 SqlSessionFactoryBean을 사용.
     * 만약, Spring이 아닌 MyBatis를 사용한다면 SqlSessionFactoryBuilder를 사용.
     *
     * (5)
     * 앞에서 만든 데이터 소스 설정
     *
     * (6)
     * MyBatis 매퍼(Mapper) 파일의 위치 설정.
     * Mapper : Application에서 사용할 SQL을 담고있는 XML 파일
     * 매퍼를 등록할 때에는 매퍼 파일을 하나씩 따로 등록할 수도 있지만 하나의 애플리케이션에는 일반적으로 많은 수의 매퍼 파일 존재. (하나씩 등록하기 어려움)
     * 패턴을 기반으로 한 번에 등록하는 것이 좋음.
     * classpath : resource 폴더를 의미
     * mapper/**\/ mapper 폴더 밑의 모든 폴더를 의미
     * sql-*.xml : 이름이 sql-로 시작하고 확장자가 xml인 모든 파일을 의미.
     */

    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @ConfigurationProperties(prefix="mybatis.configuration") // (7)
    public org.apache.ibatis.session.Configuration mybatisConfig(){
        return new org.apache.ibatis.session.Configuration(); // (8)
    }
    /* (7)
     * application.properties의 설정 중 마이바티스에 관련된 설정을 불러옴.
     *
     * (8)
     * 가져온 마이바티스 설정을 자바 클래스로 만들어서 반환.
     */

}

