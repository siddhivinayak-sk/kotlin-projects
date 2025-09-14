package com.sk.ai.config.mcp.server

import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.ai.tool.function.FunctionToolCallback
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.util.function.Consumer

@Configuration(proxyBeanMethods = false)
class McpConfig {

    @Bean
    fun weatherTools(weatherService: WeatherService): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build()
    }

    data class TextInput(val input: String)
    @Bean
    fun toUpperCase(): ToolCallback {
        return FunctionToolCallback.builder("toUpperCase", Consumer { input: TextInput -> input.input.uppercase() })
                .inputType(TextInput::class.java)
                .description("Put the text to upper case")
                .build()
    }
}

@Service
class WeatherService {

    companion object {
        const val BASE_URL = "https://api.weather.gov"
        val restTemplate = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build()
    }

    data class Alert(val features: List<Feature> = emptyList())
    data class Feature(val properties: Properties? = null)
    data class Properties(val event: String?, val areaDesc: String? = null, val severity: String? = null, val description: String? = null, val instruction: String? = null)
    @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
    fun getAlerts(@ToolParam(description =  "Two-letter US state code (e.g. CA, NY") state: String): String {
        val alert = restTemplate.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert::class.java)
        return alert?.features?.joinToString(separator = "\n") { """
            Event: ${it.properties?.event}
            Area: ${it.properties?.areaDesc}
            Severity: ${it.properties?.severity}
            Description: ${it.properties?.description}
            Instructions: ${it.properties?.instruction}
            """.trimIndent() } ?: ""
    }

    data class Points(val properties: Props? = null)
    data class Props(val forecast: String? = null)
    data class Forecast(val properties: FProps? = null)
    data class FProps(val periods: List<Period> = emptyList())
    data class Period(
            val number: Int? = 0,
            val name: String? = null,
            val startTime: String? = null,
            val endTime: String? = null,
            val isDaytime: Boolean? = null,
            val temperature: Int? = 0,
            val temperatureUnit: String? = null,
            val temperatureTrend: String? = null,
            val probabilityOfPrecipitation: Map<Any, Any> = emptyMap(),
            val windSpeed: String? = null,
            val windDirection: String? = null,
            val icon: String? = null,
            val shortForecast: String? = null,
            val detailedForecast: String? = null,
    )
    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    fun getWeatherForecastByLocation(latitude: Double, longitude: Double): String {
        val points = restTemplate.get().uri("/points/{latitude},{longitude}", latitude, longitude).retrieve().body(Points::class.java)
        val forecast = restTemplate.get().uri(points?.properties?.forecast ?: "").retrieve().body(Forecast::class.java)
        return forecast?.properties?.periods?.joinToString("\n") { """
            ${it.name}:
            Temperature: ${it.temperature} ${it.temperatureUnit}
            Wind: ${it.windSpeed} ${it.windDirection}
            DetailedForecast: ${it.detailedForecast}
        """.trimIndent() } ?: ""
    }
}

//fun main(args: Array<String>) {
//    val weatherService = WeatherService()
//    println(weatherService.getAlerts("CA"))
//    println(weatherService.getWeatherForecastByLocation(34.0522, -118.2437))
//}
