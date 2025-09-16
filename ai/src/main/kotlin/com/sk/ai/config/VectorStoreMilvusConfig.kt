package com.sk.ai.config

import com.sk.ai.config.properties.VectorStoreProperties
import io.milvus.client.MilvusServiceClient
import io.milvus.param.ConnectParam
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.TokenCountBatchingStrategy
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration(proxyBeanMethods = false)
@Order(1)
@EnableConfigurationProperties(VectorStoreProperties::class)
@Conditional(MilvusVectorStoreCondition::class)
class VectorStoreMilvusConfig {

    @Bean
    fun vectorStore(
            milvusClient: MilvusServiceClient,
            embeddingModel: EmbeddingModel,
            vectorStoreProperties: VectorStoreProperties,
    ): VectorStore {
        return MilvusVectorStore.builder(milvusClient, embeddingModel)
                .collectionName(vectorStoreProperties.milvus.collectionName)
                .databaseName(vectorStoreProperties.milvus.database)
                .indexType(vectorStoreProperties.milvus.indexType)
                .metricType(vectorStoreProperties.milvus.metricType)
                .batchingStrategy(TokenCountBatchingStrategy())
                .initializeSchema(vectorStoreProperties.milvus.initializationSchema)
                .embeddingDimension(vectorStoreProperties.milvus.embeddingDimension)
                .iDFieldName(vectorStoreProperties.milvus.idFieldName)
                .autoId(vectorStoreProperties.milvus.autoId)
                .contentFieldName(vectorStoreProperties.milvus.contentFieldName)
                .metadataFieldName(vectorStoreProperties.milvus.metadataFieldName)
                .embeddingFieldName(vectorStoreProperties.milvus.embeddingFieldName)
                .build()
    }

    @Bean
    fun milvusClient(vectorStoreProperties: VectorStoreProperties): MilvusServiceClient {
        return MilvusServiceClient(
                ConnectParam.newBuilder()
                        .withAuthorization(vectorStoreProperties.milvus.username, vectorStoreProperties.milvus.password)
                        .withHost(vectorStoreProperties.milvus.host)
                        .withPort(vectorStoreProperties.milvus.port)
                        .withDatabaseName(vectorStoreProperties.milvus.database)
                        .build()
        )
    }
}