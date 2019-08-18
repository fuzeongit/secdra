package com.junjie.secdraadmin.base.configurer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManager
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryCollect",
        transactionManagerRef = "transactionManagerCollect",
        basePackages = ["com.junjie.secdracollect.dao"]) //设置Repository所在位置
class CollectDataSourceConfigurer(private val jpaProperties: JpaProperties) {
    @Bean
    @Qualifier("collectDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.collect")
    fun collectDataSource(): DataSource {
        return DataSourceBuilder.create().build();
    }


    @Bean
    fun entityManagerCollect(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): EntityManager {
        return entityManagerFactoryCollect(builder, hibernateProperties).`object`!!.createEntityManager()
    }

    @Bean
    fun entityManagerFactoryCollect(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.properties, HibernateSettings())
        return builder
                .dataSource(collectDataSource())
                .properties(properties)
                .packages("com.junjie.secdracollect.model") //设置实体类所在位置
                .persistenceUnit("collectPersistenceUnit")
                .build()
    }

    @Bean
    fun transactionManagerCollect(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactoryCollect(builder, hibernateProperties).`object`!!)
    }
}
