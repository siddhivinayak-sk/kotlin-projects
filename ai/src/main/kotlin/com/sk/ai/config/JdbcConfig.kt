package com.sk.ai.config

import com.sk.ai.config.properties.VectorStoreProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.util.Properties

@Configuration(proxyBeanMethods = false)
@Order(0)
@EnableConfigurationProperties(VectorStoreProperties::class)
class JdbcConfig {

    @Bean
    @ConditionalOnProperty(prefix = "vector.pg", name = ["enabled"], havingValue = "true", matchIfMissing = false)
    fun jdbcTemplate(vectorStoreProperties: VectorStoreProperties): JdbcTemplate {
        val dataSource = DriverManagerDataSource(
                vectorStoreProperties.pg.url,
                vectorStoreProperties.pg.username,
                vectorStoreProperties.pg.password,
        )
        dataSource.setDriverClassName("org.postgresql.Driver")
        dataSource.schema = vectorStoreProperties.pg.schemaName
        dataSource.connectionProperties = Properties().also {
            it.setProperty("TimeZone", "Asia/Kolkata")
        }
        return JdbcTemplate(dataSource)
    }
}