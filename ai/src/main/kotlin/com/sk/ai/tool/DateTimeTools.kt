package com.sk.ai.tool

import com.sk.ai.advisor.CustomAdvisor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ToolContext
import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.ai.tool.function.FunctionToolCallback
import org.springframework.ai.tool.method.MethodToolCallback
import org.springframework.ai.tool.support.ToolDefinitions
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Function

class DateTimeTools {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(CustomAdvisor::class.java)
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    fun getCurrentDateTime(): String {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString()
    }

    @Tool(description = "Set a user alarm for the given time, provided in ISO-8601 format")
    fun setAlarm(@ToolParam(description = "Time in ISO-8601 format") time: String) {
        val alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        logger.info("Alarm set for {}", alarmTime)
    }

    fun getMyBirthday(context: ToolContext): String {
        logger.info("ToolContext entries: {}", context.context.entries) // Example of using ToolContext where tool context can be passed into prompt ChatOption
        return "2024-01-01"
    }

    //This ToolCallback object can be used with Prompt as Options or with ChatClient.defaultTools()
    fun getMyBirthdayToolCallback(): ToolCallback {
        val method: Method? = ReflectionUtils.findMethod(DateTimeTools::class.java, "getMyBirthday", ToolContext::class.java)
        val toolCallback: ToolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions.builder(method!!).name("getMyBirthday").description("Get my birthday").build())
                .toolObject(this)
                .toolMethod(method)
                .build()
        return toolCallback
    }

    //Function (Function, Supplier, Consumer, or BiFunction) as tool
    enum class Unit {C, F}
    data class WeatherRequest(val location: String, val unit: Unit)
    data class WeatherResponse(val temp: Double, val unit: Unit)
    class WeatherService: Function<WeatherRequest, WeatherResponse> {
        override fun apply(t: WeatherRequest): WeatherResponse {
            return WeatherResponse(22.5, t.unit)
        }
    }
    fun createFunctionToolCallback(): ToolCallback {
        return FunctionToolCallback
                .builder<WeatherRequest, WeatherResponse>("currentWeather", WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherRequest::class.java)
                .build()
    }
}