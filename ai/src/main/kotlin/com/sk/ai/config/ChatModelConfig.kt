package com.sk.ai.config

import com.sk.ai.config.properties.ModelProperties
import com.sk.ai.util.chatModelOptions
import com.sk.ai.util.toChatModel
import org.springframework.ai.chat.model.ChatModel
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
@EnableConfigurationProperties(ModelProperties::class)
@ConditionalOnProperty(prefix = "app.chat", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ChatModelConfig {

    @Bean
    fun chatModelBeansRegistrar(modelProperties: ModelProperties): BeanFactoryPostProcessor = BeanFactoryPostProcessor { beanFactory ->
        if (beanFactory is BeanDefinitionRegistry) {
            modelProperties.models.filter { it.value.enabled }.forEach { (key, model) ->
                val options = model.chatModelOptions()
                val chatModel = model.toChatModel(options)
                val beanDefinitionChatModel = GenericBeanDefinition()
                beanDefinitionChatModel.setBeanClass(ChatModel::class.java)
                beanDefinitionChatModel.instanceSupplier = Supplier<ChatModel> { chatModel }
                beanFactory.registerBeanDefinition(key, beanDefinitionChatModel)
            }
        }
    }
}
