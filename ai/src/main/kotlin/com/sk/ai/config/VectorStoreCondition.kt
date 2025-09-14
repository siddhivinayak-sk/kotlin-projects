package com.sk.ai.config

import org.springframework.boot.autoconfigure.condition.ConditionOutcome
import org.springframework.boot.autoconfigure.condition.SpringBootCondition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class VectorStoreCondition : SpringBootCondition() {
    override fun getMatchOutcome(context: ConditionContext, metadata: AnnotatedTypeMetadata): ConditionOutcome {
        val env = context.environment
        val first = env.getProperty("vector.azureAiSearch.enabled") == "true"
        val second = env.getProperty("app.chat.enabled") == "true"
        return if (first && second) {
            ConditionOutcome.match()
        } else {
            ConditionOutcome.noMatch("Required properties not set so VectorStoreCondition did not match")
        }
    }
}