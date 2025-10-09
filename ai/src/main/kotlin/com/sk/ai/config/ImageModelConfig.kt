package com.sk.ai.config

import com.sk.ai.config.properties.ModelProperties
import org.springframework.ai.image.ImageModel
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.util.function.Supplier
import com.sk.ai.util.toImageModel

@Configuration(proxyBeanMethods = false)
@Order(2)
@EnableConfigurationProperties(ModelProperties::class)
@ConditionalOnProperty(prefix = "app.image", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ImageModelConfig {

    @Bean
    fun imageModelBeansRegistrar(modelProperties: ModelProperties): BeanFactoryPostProcessor = BeanFactoryPostProcessor { beanFactory ->
        if (beanFactory is BeanDefinitionRegistry) {
            modelProperties.images.filter { it.value.enabled }.forEach { (key, model) ->
                val imageModel = model.toImageModel()
                val beanDefinitionChatModel = GenericBeanDefinition()
                beanDefinitionChatModel.setBeanClass(ImageModel::class.java)
                beanDefinitionChatModel.instanceSupplier = Supplier<ImageModel> { imageModel }
                beanFactory.registerBeanDefinition("image$key", beanDefinitionChatModel)
            }
        }
    }
}