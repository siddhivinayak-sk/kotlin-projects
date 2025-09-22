package com.sk.ai.config

import com.sk.ai.config.properties.VectorStoreProperties
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate

@Configuration(proxyBeanMethods = false)
@Order(0)
@EnableConfigurationProperties(VectorStoreProperties::class)
class JdbcConfig {

    @Bean
    @ConditionalOnProperty(prefix = "vector.pg", name = ["enabled"], havingValue = "true", matchIfMissing = false)
    fun jdbcTemplate(vectorStoreProperties: VectorStoreProperties): JdbcTemplate {
        //System.setProperty("user.timezone", "Asia/Kolkata")
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = vectorStoreProperties.pg.url
        hikariConfig.username = vectorStoreProperties.pg.username
        hikariConfig.password = vectorStoreProperties.pg.password
        hikariConfig.driverClassName = "org.postgresql.Driver"
        hikariConfig.schema = vectorStoreProperties.pg.schemaName
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true")
        hikariConfig.addDataSourceProperty("TimeZone", "Asia/Kolkata")
        hikariConfig.maximumPoolSize = 10
        hikariConfig.minimumIdle = 5
        hikariConfig.idleTimeout = 30000
        hikariConfig.connectionTimeout = 20000
        hikariConfig.maxLifetime = 1800000
        val dataSource = HikariDataSource(hikariConfig)
        return JdbcTemplate(dataSource)
    }
}