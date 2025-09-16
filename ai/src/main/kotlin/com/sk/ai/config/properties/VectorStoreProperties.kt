package com.sk.ai.config.properties

import io.milvus.param.IndexType
import io.milvus.param.MetricType
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vector")
data class VectorStoreProperties(
        val azureAiSearch: AzureAiVectorStoreProperties,
        val milvus: MilvusVectorStoreProperties,
        val pg: PGVectorStoreProperties,
)

data class AzureAiVectorStoreProperties(
    val enabled: Boolean,
    val endpoint: String,
    val indexName: String,
    val apiKey: String,
    val initializationSchema: Boolean = false,
    val topK: Int = 5,
    val similarityIndexThreshold: Double = 0.7,
    val metadataFields: Map<String, String> = emptyMap(),
)

data class MilvusVectorStoreProperties(
        val enabled: Boolean,
        val host: String,
        val port: Int = 19530,
        val database: String,
        val collectionName: String,
        val username: String = "",
        val password: String = "",
        val initializationSchema: Boolean = false,
        val embeddingDimension: Int = 1536,
        val indexType: IndexType = IndexType.IVF_FLAT,
        val metricType: MetricType = MetricType.COSINE,
        val idFieldName: String = "doc_id",
        val autoId: Boolean = false,
        val contentFieldName: String = "content",
        val metadataFieldName: String = "metadata",
        val embeddingFieldName: String = "embedding",
)

data class PGVectorStoreProperties(
        val enabled: Boolean,
        val url: String,
        val username: String,
        val password: String,
        val indexType: PgVectorStore.PgIndexType = PgVectorStore.PgIndexType.HNSW,
        val distanceType: PgDistanceType = PgDistanceType.COSINE_DISTANCE,
        val dimensions: Int = 1536,
        val removeExistingVectorStoreTable: Boolean = false,
        val initializeSchema: Boolean = false,
        val schemaName: String = "public",
        val tableName: String = "vector_store",
        val maxDocumentBatchSize: Int = 10000,
)
