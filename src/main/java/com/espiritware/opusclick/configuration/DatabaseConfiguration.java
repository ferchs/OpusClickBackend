package com.espiritware.opusclick.configuration;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//Esta clase es para reemplazar el hibernate.cfg.xml para evitar tener archivos xml 
//y que la session sea manejada como un SINGLETON por Spring 
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
	
	//pendiente...probar si yo cambio el nombre de este metodo; esta clase sigue funcionando... porque no se como la clase AbstractSession llama al objeto sesion
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		LocalSessionFactoryBean sessionFactoryBean= new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan("com.espiritware.opusclick.model");
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		return sessionFactoryBean;
	}
	
	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		//dataSource.setUrl("jdbc:mysql://aa1hq9j06vollj.chosobyrsnb7.sa-east-1.rds.amazonaws.com:3306/OpusClickBD");
		dataSource.setUrl("jdbc:mysql://localhost:3306/OpusClickBD");
		dataSource.setUsername("root");
		dataSource.setPassword("3527Ferchs");
		return dataSource;
	}
	
	public Properties hibernateProperties(){
		Properties properties= new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("show_sql", "true");
		return properties;
	}
	
	//Se usa @Autowired porque estoy trabajando con un objeto que ya ha sido persistido
	//el objeto de que devuelve sessionFactory().getObject()
	//Yo creo que este es el metodo donde se refleja el SINGLETON comentado al principio de esta clase
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(){
		HibernateTransactionManager hibernateTransactionManager= new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
		return hibernateTransactionManager;
	}
}
  