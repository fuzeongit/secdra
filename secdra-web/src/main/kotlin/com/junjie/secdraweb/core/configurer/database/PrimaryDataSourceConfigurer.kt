package com.junjie.secdraweb.core.configurer.database

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
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
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPrimary",
        transactionManagerRef = "transactionManagerPrimary",
        basePackages = ["com.junjie.secdradata.database.primary.dao"]) //设置Repository所在位置
class PrimaryDataSourceConfigurer(private val jpaProperties: JpaProperties) {
    @Bean
    @Primary
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    fun primaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Primary
    @Bean
    fun entityManagerPrimary(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): EntityManager {
        return entityManagerFactoryPrimary(builder, hibernateProperties).`object`!!.createEntityManager()
    }

    @Primary
    @Bean
    fun entityManagerFactoryPrimary(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.properties, HibernateSettings())
        return builder
                .dataSource(primaryDataSource())
                .properties(properties)
                .packages("com.junjie.secdradata.database.primary.entity") //设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build()
    }

    @Primary
    @Bean
    fun transactionManagerPrimary(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactoryPrimary(builder, hibernateProperties).`object`!!)
    }
}
