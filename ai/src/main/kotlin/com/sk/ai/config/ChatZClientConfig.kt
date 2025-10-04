package com.sk.ai.config

import com.sk.ai.advisor.CustomAdvisor
import com.sk.ai.config.properties.ChatMemoryType.MESSAGE
import com.sk.ai.config.properties.ChatMemoryType.PROMPT
import com.sk.ai.config.properties.ChatMemoryType.VECTOR
import com.sk.ai.config.properties.ChatModelProperties
import com.sk.ai.config.properties.ModelProperties
import com.sk.ai.tool.DateTimeTools
import com.sk.ai.util.chatModelOptions
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
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.util.function.Supplier

@Configuration(proxyBeanMethods = false)
@Order(3)
@EnableConfigurationProperties(ModelProperties::class)
@ConditionalOnProperty(prefix = "app.chat", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ChatZClientConfig {

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
            //vectorStore: VectorStore? = null,
    ): BaseChatMemoryAdvisor {
        return when (modelProperties.chatMemoryType) {
            MESSAGE -> MessageChatMemoryAdvisor.builder(chatMemory).build()
            PROMPT -> PromptChatMemoryAdvisor.builder(chatMemory).build()
            VECTOR -> throw Exception("Vector store object is not available") //VectorStoreChatMemoryAdvisor.builder(vectorStore ?: throw Exception("Vector store object is not available")).build()
        }
    }

    @Bean
    fun chatClientBeansRegistrar(
            modelProperties: ModelProperties,
            chatMemoryAdvisor: BaseChatMemoryAdvisor,
            applicationContext: ApplicationContext,
    ): BeanFactoryPostProcessor = BeanFactoryPostProcessor { beanFactory ->
        val chatModels = applicationContext.getBeansOfType(ChatModel::class.java)
        if (beanFactory is BeanDefinitionRegistry) {
            modelProperties.models.filter { it.value.enabled }.forEach { (key, model) ->
                val options = model.chatModelOptions()
                val beanDefinitionChatClient = GenericBeanDefinition()
                beanDefinitionChatClient.setBeanClass(ChatClient::class.java)
                beanDefinitionChatClient.instanceSupplier =
                        Supplier<ChatClient> { model.chatClient(chatModels.findModel(key), options, chatMemoryAdvisor) }
                beanFactory.registerBeanDefinition(key + "Client", beanDefinitionChatClient)
            }
        }
    }

    private fun Map<String, ChatModel>.findModel(key: String) = this[key]
            ?: throw Exception("Chat model with key $key is not available in the application context")

    private fun ChatModelProperties.chatClient(
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

    private fun advisors(chatMemoryAdvisor: BaseChatMemoryAdvisor): List<Advisor> {
        return mutableListOf(SimpleLoggerAdvisor(), CustomAdvisor())
    }
}