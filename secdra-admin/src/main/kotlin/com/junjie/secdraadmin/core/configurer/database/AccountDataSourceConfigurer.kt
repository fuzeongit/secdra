package com.junjie.secdraadmin.core.configurer.database

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
        entityManagerFactoryRef = "entityManagerFactoryAccount",
        transactionManagerRef = "transactionManagerAccount",
        basePackages = ["com.junjie.secdradata.database.account.dao"]) //设置Repository所在位置
class AccountDataSourceConfigurer(private val jpaProperties: JpaProperties) {
    @Bean
    @Qualifier("accountDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.account")
    fun accountDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }


    @Bean
    fun entityManagerAccount(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): EntityManager {
        return entityManagerFactoryAccount(builder, hibernateProperties).`object`!!.createEntityManager()
    }

    @Bean
    fun entityManagerFactoryAccount(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.properties, HibernateSettings())
        return builder
                .dataSource(accountDataSource())
                .properties(properties)
                .packages("com.junjie.secdradata.database.account.entity") //设置实体类所在位置
                .persistenceUnit("accountPersistenceUnit")
                .build()
    }

    @Bean
    fun transactionManagerAccount(builder: EntityManagerFactoryBuilder, hibernateProperties: HibernateProperties): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactoryAccount(builder, hibernateProperties).`object`!!)
    }
}
