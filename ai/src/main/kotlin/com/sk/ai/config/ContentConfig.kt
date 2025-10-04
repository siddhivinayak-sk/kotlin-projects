package com.sk.ai.config

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.content.ContentLoader
import com.sk.ai.content.ContentProcessor
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ContentProperties::class)
@Order
class ContentConfig {

    @Bean
    fun contentLoader(contentProcessors: List<ContentProcessor>, vectorStore: VectorStore?): ContentLoader {
        return ContentLoader(contentProcessors, vectorStore)
    }
}
