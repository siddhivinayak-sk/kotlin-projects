package com.sk.ai.config

import com.sk.ai.config.properties.VectorStoreProperties
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate

@Configuration(proxyBeanMethods = false)
@Order(1)
@EnableConfigurationProperties(VectorStoreProperties::class)
@Conditional(PgVectorStoreCondition::class)
class VectorStorePgConfig {

    @Bean
    fun vectorStore(
            jdbcTemplate: JdbcTemplate,
            vectorStoreProperties: VectorStoreProperties,
            embeddingModel: EmbeddingModel,
    ): VectorStore {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(vectorStoreProperties.pg.dimensions)
                .distanceType(vectorStoreProperties.pg.distanceType)
                .indexType(vectorStoreProperties.pg.indexType)
                .initializeSchema(vectorStoreProperties.pg.initializeSchema)
                .schemaName(vectorStoreProperties.pg.schemaName)
                .vectorTableName(vectorStoreProperties.pg.tableName)
                .maxDocumentBatchSize(vectorStoreProperties.pg.maxDocumentBatchSize)
                .removeExistingVectorStoreTable(vectorStoreProperties.pg.removeExistingVectorStoreTable)
                .build()
    }
}