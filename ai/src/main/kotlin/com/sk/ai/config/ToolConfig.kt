package com.sk.ai.config

import com.sk.ai.tool.DateTimeTools
import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor
import org.springframework.ai.tool.resolution.DelegatingToolCallbackResolver
import org.springframework.ai.tool.resolution.StaticToolCallbackResolver
import org.springframework.ai.tool.resolution.ToolCallbackResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description

@Configuration(proxyBeanMethods = false)
class ToolConfig {
    @Bean
    fun toolExecutionExceptionProcessor(): ToolExecutionExceptionProcessor { //Exception handleing for tools
        return DefaultToolExecutionExceptionProcessor(true)
    }

    @Bean
    fun toolCallbackResolver(toolCallbacks: List<ToolCallback>): ToolCallbackResolver { //Auto register tool callbacks
        val staticToolCallbackResolver = StaticToolCallbackResolver(toolCallbacks)
        return DelegatingToolCallbackResolver(listOf(staticToolCallbackResolver))
    }

    @Bean
    @Description("Get the weather in location") //ToolCallbackResolver uses this description
    fun dynamicFuncallToolRegistration(): DateTimeTools.WeatherService {
        return DateTimeTools.WeatherService()
    }

}