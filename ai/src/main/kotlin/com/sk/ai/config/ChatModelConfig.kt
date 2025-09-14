package com.sk.ai.config

import com.azure.ai.openai.OpenAIClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.sk.ai.advisor.CustomAdvisor
import com.sk.ai.config.properties.ChatMemoryType
import com.sk.ai.config.properties.ChatModelConfig
import com.sk.ai.config.properties.ChatModelType.OAI_GPT_4_1
import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.ModelProperties
import com.sk.ai.tool.DateTimeTools
import org.springframework.ai.azure.openai.AzureOpenAiChatModel
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.ChatMemoryRepository
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.util.function.Supplier

@Configuration(proxyBeanMethods = false)
@Order(2)
@EnableConfigurationProperties(ModelProperties::class, ContentProperties::class)
@ConditionalOnProperty(prefix = "app.chat", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ChatModelConfig {

    @Bean
    fun chatMemoryRepository(): ChatMemoryRepository {
        return InMemoryChatMemoryRepository()
    }

    @Bean
    fun chatMemory(modelProperties: ModelProperties, repository: ChatMemoryRepository): ChatMemory {
        return MessageWindowChatMemory.builder()
                .maxMessages(modelProperties.chatMemoryCount)
                .chatMemoryRepository(repository)
                .build()
    }

    @Bean
    fun chatMemoryAdvisor(
            modelProperties: ModelProperties,
            chatMemory: ChatMemory,
    ): BaseChatMemoryAdvisor {
        return when (modelProperties.chatMemoryType) {
            ChatMemoryType.MESSAGE -> MessageChatMemoryAdvisor.builder(chatMemory).build()
            ChatMemoryType.PROMPT -> PromptChatMemoryAdvisor.builder(chatMemory).build()
        }
    }

    @Bean
    fun chatModelBeansRegistrar(
            modelProperties: ModelProperties,
            chatMemoryAdvisor: BaseChatMemoryAdvisor,
    ): BeanFactoryPostProcessor = BeanFactoryPostProcessor { beanFactory ->
        if (beanFactory is BeanDefinitionRegistry) {
            modelProperties.models.filter { it.value.enabled }.forEach { (key, model) ->
                val options = model.chatModelOptions()
                val chatModel = model.toChatModel(options)
                val beanDefinitionChatModel = GenericBeanDefinition()
                beanDefinitionChatModel.setBeanClass(ChatModel::class.java)
                beanDefinitionChatModel.instanceSupplier = Supplier<ChatModel> { chatModel }
                beanFactory.registerBeanDefinition(key, beanDefinitionChatModel)
                val beanDefinitionChatClient = GenericBeanDefinition()
                beanDefinitionChatClient.setBeanClass(ChatClient::class.java)
                beanDefinitionChatClient.instanceSupplier =
                        Supplier<ChatClient> { model.chatClient(chatModel, options, chatMemoryAdvisor) }
                beanFactory.registerBeanDefinition(key + "Client", beanDefinitionChatClient)
            }
        }
    }

    private fun ChatModelConfig.chatModelOptions(): ChatOptions {
        return when (modelType) {
            OAI_GPT_4_1 -> azureChatModelOptions()
        }
    }

    private fun ChatModelConfig.toChatModel(chatOptions: ChatOptions): ChatModel {
        return when (modelType) {
            OAI_GPT_4_1 -> createAzureOpenAiModel(chatOptions)
        }
    }

    private fun ChatModelConfig.azureChatModelOptions(): ChatOptions {
        return AzureOpenAiChatOptions().also {
            it.user = userPrompt
            it.topP = topP
            it.temperature = temperature
            it.maxTokens = maxTokens
            it.frequencyPenalty = frequencyPenalty
            it.presencePenalty = presencePenalty
            it.n = n
            it.deploymentName = modelName
        }
    }

    private fun ChatModelConfig.createAzureOpenAiModel(chatOptions: ChatOptions): ChatModel {
        val oaiClientBuilder = OpenAIClientBuilder()
                .credential(AzureKeyCredential(apiKey))
                .endpoint(baseUrl)
        return AzureOpenAiChatModel.builder()
                .defaultOptions(chatOptions as AzureOpenAiChatOptions)
                .openAIClientBuilder(oaiClientBuilder)
                .build()
    }

    private fun ChatModelConfig.chatClient(
            chatModel: ChatModel,
            chatOptions: ChatOptions,
            chatMemoryAdvisor: BaseChatMemoryAdvisor,
    ): ChatClient {
        return ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultOptions(chatOptions)
                .defaultAdvisors(advisors(chatMemoryAdvisor))
                .defaultTools(DateTimeTools())
                .defaultToolCallbacks(DateTimeTools().getMyBirthdayToolCallback())
                .build()
    }

    private fun advisors(
            chatMemoryAdvisor: BaseChatMemoryAdvisor,
    ): List<Advisor> {
        return mutableListOf(SimpleLoggerAdvisor(), CustomAdvisor(), chatMemoryAdvisor)
    }
}
