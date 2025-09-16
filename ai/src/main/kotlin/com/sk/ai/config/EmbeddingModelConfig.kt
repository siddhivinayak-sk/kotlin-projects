package com.sk.ai.config

import com.azure.ai.openai.OpenAIClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.sk.ai.config.properties.EmbeddingModelConfig
import com.sk.ai.config.properties.EmbeddingModelType.OAI_TEXT_EMBEDDING_3_LARGE
import com.sk.ai.config.properties.EmbeddingModelType.POSTGRES_ML_DISTILBERT_BASE_UNCASED
import com.sk.ai.config.properties.ModelProperties
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingModel
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingOptions
import org.springframework.ai.document.MetadataMode
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.postgresml.PostgresMlEmbeddingModel
import org.springframework.ai.postgresml.PostgresMlEmbeddingOptions
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import java.util.function.Supplier

@Configuration(proxyBeanMethods = false)
@Order(0)
@EnableConfigurationProperties(ModelProperties::class)
@ConditionalOnProperty(prefix = "app.chat", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class EmbeddingModelConfig {

    @Bean
    fun embeddingModelBeansRegistrar(
            modelProperties: ModelProperties,
            jdbcTemplate: JdbcTemplate?,
    ): BeanFactoryPostProcessor = BeanFactoryPostProcessor { beanFactory ->
        if (beanFactory is BeanDefinitionRegistry) {
            modelProperties.embeddings.filter{ it.value.enabled }.forEach { (key, model) ->
                val beanDefinition = GenericBeanDefinition()
                beanDefinition.setBeanClass(EmbeddingModel::class.java)
                beanDefinition.instanceSupplier = Supplier<EmbeddingModel> { model.toEmbedding(jdbcTemplate) }
                beanFactory.registerBeanDefinition(key, beanDefinition)
            }
        }
    }

    private fun EmbeddingModelConfig.toEmbedding(jdbcTemplate: JdbcTemplate?): EmbeddingModel {
        return when(modelType) {
            OAI_TEXT_EMBEDDING_3_LARGE -> createAzureOpenAiEmbedding()
            POSTGRES_ML_DISTILBERT_BASE_UNCASED -> createPostgresSqlEmbedding(jdbcTemplate)
        }
    }

    private fun EmbeddingModelConfig.createAzureOpenAiEmbedding(): EmbeddingModel {
        val openAIClient = OpenAIClientBuilder()
                .credential(AzureKeyCredential(apiKey))
                .endpoint(baseUrl)
                .buildClient()
        val options = AzureOpenAiEmbeddingOptions.builder()
                .deploymentName(modelName)
                .user(user)
                //.inputType("")
                //.dimensions(0)
                .build()
        return AzureOpenAiEmbeddingModel(openAIClient, MetadataMode.EMBED, options)
    }

    private fun EmbeddingModelConfig.createPostgresSqlEmbedding(jdbcTemplate: JdbcTemplate?):  EmbeddingModel {
        val options = PostgresMlEmbeddingOptions.builder()
                .transformer(transformer)
                .vectorType(PostgresMlEmbeddingModel.VectorType.PG_VECTOR)
                .kwargs(kwargs)
                .metadataMode(MetadataMode.EMBED)
                .build()
        val embeddingModel = PostgresMlEmbeddingModel(
                jdbcTemplate,
                options,
        )
        embeddingModel.afterPropertiesSet()
        return embeddingModel
    }
}
