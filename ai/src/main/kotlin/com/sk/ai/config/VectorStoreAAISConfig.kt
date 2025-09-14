package com.sk.ai.config

import com.azure.core.credential.AzureKeyCredential
import com.azure.search.documents.indexes.SearchIndexClient
import com.azure.search.documents.indexes.SearchIndexClientBuilder
import com.sk.ai.config.properties.AzureAiVectorStoreProperties
import com.sk.ai.config.properties.VectorStoreProperties
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.azure.AzureVectorStore
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.util.stream.Collectors

@Configuration(proxyBeanMethods = false)
@Order(1)
@EnableConfigurationProperties(VectorStoreProperties::class)
@Conditional(VectorStoreCondition::class)
class VectorStoreAAISConfig {

    @Bean
    fun searchIndexClient(vectorStoreProperties: VectorStoreProperties): SearchIndexClient {
        val properties = vectorStoreProperties.azureAiSearch
        return SearchIndexClientBuilder()
                .endpoint(properties.endpoint)
                .credential(AzureKeyCredential(properties.apiKey))
                .buildClient()
    }

    @Bean
    fun vectorStore(
            vectorStoreProperties: VectorStoreProperties,
            searchIndexClient: SearchIndexClient,
            embeddingModel: EmbeddingModel,
    ): VectorStore {
        val properties = vectorStoreProperties.azureAiSearch
        val builder = AzureVectorStore.builder(searchIndexClient, embeddingModel)
                .initializeSchema(properties.initializationSchema)
                .defaultTopK(properties.topK)
                .defaultSimilarityThreshold(properties.similarityIndexThreshold)
                .indexName(properties.indexName)
        takeIf { properties.metadataFields.isNotEmpty() }?.let { builder.filterMetadataFields(properties.metadataFields()) }
        return builder.build()
    }

    private fun AzureAiVectorStoreProperties.metadataFields(): List<AzureVectorStore.MetadataField> {
        return metadataFields.entries.stream().map { toMetadataField(it.key, it.value) }.collect(Collectors.toList())
    }

    private fun toMetadataField(name: String, type: String): AzureVectorStore.MetadataField {
        return when(type) {
            "bool" -> AzureVectorStore.MetadataField.bool(name)
            "int32" -> AzureVectorStore.MetadataField.int32(name)
            "int64" -> AzureVectorStore.MetadataField.int64(name)
            "date" -> AzureVectorStore.MetadataField.date(name)
            "decimal" -> AzureVectorStore.MetadataField.decimal(name)
            else -> AzureVectorStore.MetadataField.text(name)
        }
    }
}