package com.sk.ai.config

import com.sk.ai.config.properties.VectorStoreProperties
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer
import org.springframework.boot.sql.init.DatabaseInitializationMode
import org.springframework.boot.sql.init.DatabaseInitializationSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration(proxyBeanMethods = false)
@Order(0)
@EnableConfigurationProperties(VectorStoreProperties::class)
class JdbcConfig {

    @Bean
    @ConditionalOnProperty(prefix = "vector.pg", name = ["enabled"], havingValue = "true", matchIfMissing = false)
    fun dataSource(vectorStoreProperties: VectorStoreProperties): DataSource {
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
        return HikariDataSource(hikariConfig)
    }


    @Bean
    @ConditionalOnProperty(prefix = "vector.pg", name = ["enabled"], havingValue = "true", matchIfMissing = false)
    fun jdbcTemplate(dataSource: DataSource) = JdbcTemplate(dataSource)

    @Bean
    @ConditionalOnProperty(prefix = "vector.pg", name = ["enabled"], havingValue = "true", matchIfMissing = false)
    fun initDb(dataSource: DataSource): DataSourceScriptDatabaseInitializer {
        val settings = DatabaseInitializationSettings()
        settings.schemaLocations = listOf("classpath:db/schema.sql")
        settings.mode = DatabaseInitializationMode.ALWAYS
        val initializer = DataSourceScriptDatabaseInitializer(dataSource, settings)
        initializer.afterPropertiesSet()
        return initializer
    }
}