package com.sk.ai.config

import org.springframework.boot.autoconfigure.condition.ConditionOutcome
import org.springframework.boot.autoconfigure.condition.SpringBootCondition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class AzureAIVectorStoreCondition : SpringBootCondition() {
    override fun getMatchOutcome(context: ConditionContext, metadata: AnnotatedTypeMetadata): ConditionOutcome {
        val env = context.environment
        val enabledChat = env.getProperty("app.chat.enabled") == "true"
        val enabledVector = env.getProperty("vector.azureAiSearch.enabled") == "true"
        return if (enabledChat && enabledVector) {
            ConditionOutcome.match()
        } else {
            ConditionOutcome.noMatch("Required properties not set so VectorStoreCondition did not match")
        }
    }
}

class MilvusVectorStoreCondition : SpringBootCondition() {
    override fun getMatchOutcome(context: ConditionContext, metadata: AnnotatedTypeMetadata): ConditionOutcome {
        val env = context.environment
        val enabledChat = env.getProperty("app.chat.enabled") == "true"
        val enabledVector = env.getProperty("vector.milvus.enabled") == "true"
        return if (enabledChat && enabledVector) {
            ConditionOutcome.match()
        } else {
            ConditionOutcome.noMatch("Required properties not set so VectorStoreCondition did not match")
        }
    }
}
