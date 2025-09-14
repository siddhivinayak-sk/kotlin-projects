package com.sk.ai.controller

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.sk.ai.util.ragAdvisor
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.ChatMemoryRepository
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.converter.BeanOutputConverter
import org.springframework.ai.converter.ListOutputConverter
import org.springframework.ai.model.tool.ToolCallingChatOptions
import org.springframework.ai.template.st.StTemplateRenderer
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RequestMapping("/chat")
@RestController
@ConditionalOnProperty(prefix = "app.chat", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ChatController(
        private val chatClient: ChatClient,
        private val chatMemoryRepository: ChatMemoryRepository,
        private val chatMemoryAdvisor: BaseChatMemoryAdvisor,
        chatModel: ChatModel,
        private val vectorStore: VectorStore?,
) {

    @GetMapping("/simple")
    fun simpleChat(@RequestParam query: String): Flux<String> {
        return chatClient.prompt().user(query).stream().content()
    }

    @GetMapping("/promptTemplate")
    fun promptTemplate(@RequestParam adjective: String, @RequestParam topic: String): Flux<String> {
        val promptTemplate = PromptTemplate("Tell me a {adjective} joke about {topic}")
        val prompt = promptTemplate.create(mapOf("adjective" to adjective, "topic" to topic))
        return chatClient.prompt(prompt).stream().content()
    }

    @GetMapping("/promptTemplateCustom")
    fun promptTemplateCustom(): Flux<String> {
        val promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("Tell me the names of 5 movies whose soundtrack was composed by <composer>.".trimIndent())
                .build()
        val prompt = promptTemplate.render(mapOf("composer" to "John Williams"))
        return chatClient.prompt().user(prompt).stream().content()
    }

    @GetMapping("/simpleAssistantMessage")
    fun simpleAssistantMessage(@RequestParam query: String): Flux<AssistantMessage> {
        return chatClient
                .prompt()
                .user(query)
                .system("You consider as expert in software development.")
                .stream().chatResponse().map { it.result.output }
    }

    @GetMapping("/simplePrompt")
    fun simplePrompt(@RequestParam query: String): Flux<String> {
        val prompt = Prompt.builder()
                .messages(listOf(UserMessage(query)))
                .build()
        return chatClient
                .prompt(prompt)
                .stream().content()
    }

    @GetMapping("/chatMemory")
    fun chatMemory(@RequestParam id: String, @RequestParam query: String): Flux<String> {
        return chatClient.prompt()
                .user(query)
                .advisors { a -> a.param(ChatMemory.CONVERSATION_ID, id) }
                .stream()
                .content()
    }

    @GetMapping("/chatMemoryRepository")
    fun chatMemory(): List<String> {
        return chatMemoryRepository.findConversationIds()
    }

    @GetMapping("/toolWithContext")
    fun toolWithContext(@RequestParam query: String): Flux<String> {
        val chatOptions = ToolCallingChatOptions.builder()
                .toolNames("getMyBirthday")
                .toolContext(mapOf("tenantId" to "acme"))
                .build()
        val prompt = Prompt(query, chatOptions)
        return chatClient.prompt(prompt).stream().content()
    }

    @JsonPropertyOrder("actor", "movies")
    data class ActorFilms(val name: String, val films: List<String>)

    @GetMapping("/outputConverter")
    fun outputConverter(): ActorFilms? {
        return chatClient
                .prompt()
                .user { it.text("Generate the filmography of 5 movies for {actor}.").param("actor", "Tom Hanks") }
                .call()
                .entity(ActorFilms::class.java)
    }

    @GetMapping("/outputConverterDeep")
    fun outputConverterDeep(): ActorFilms? {
        val beanOutputConverter = BeanOutputConverter(ActorFilms::class.java)
        val format = beanOutputConverter.getFormat()
        val actor = "Tom Hanks"
        val template = """
            Generate the filmography of 5 movies for {actor}.
            {format}
        """
        val prompt = PromptTemplate.builder().template(template).variables(mapOf("actor" to actor, "format" to format)).build().create()
        val generation = chatClient.prompt(prompt).call().chatClientResponse().chatResponse!!.result
        return beanOutputConverter.convert(generation.output.text!!)
    }

    @GetMapping("/outputConverter2")
    fun outputConverter2(): List<ActorFilms?>? {
        return chatClient
                .prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(object : ParameterizedTypeReference<List<ActorFilms>>() {})
    }

    @GetMapping("/outputConverterDeep2")
    fun outputConverterDeep2(): List<ActorFilms?>? {
        val beanOutputConverter = BeanOutputConverter(object : ParameterizedTypeReference<List<ActorFilms>>() {})
        val format = beanOutputConverter.getFormat()
        val template = """
            Generate the filmography of 5 movies for Tom Hanks and Bill Murray.
            {format}
        """
        val prompt = PromptTemplate.builder().template(template).variables(mapOf("format" to format)).build().create()
        val generation = chatClient.prompt(prompt).call().chatClientResponse().chatResponse!!.result
        return beanOutputConverter.convert(generation.output.text!!)
    }

    @GetMapping("/mapOutputConverter")
    fun mapOutputConverter(): Map<String, Any>? {
        return chatClient
                .prompt()
                .user {
                    it
                            .text("Provide me a List of {subject}")
                            .param("subject", "an array of numbers from 1 to 9 under they key name 'numbers'")
                }
                .call()
                .entity(object : ParameterizedTypeReference<Map<String, Any>>() {})
    }

    @GetMapping("/listOutputConverter")
    fun listOutputConverter(): List<String>? {
        return chatClient
                .prompt()
                .user { it.text("List five {subject}").param("subject", "ice cream flavors") }
                .call()
                .entity(ListOutputConverter(DefaultConversionService()))
    }

    @GetMapping("/rag")
    fun rag(@RequestParam query: String): String? {
        return vectorStore?.let {
            chatClient
                    .prompt()
                    .user(query)
                    .advisors(it.ragAdvisor(), chatMemoryAdvisor)
                    .call()
                    .content()
        } ?: chatClient.prompt().user(query).call().content()
    }

    data class QueryMessage(val query: String)
    data class QueryResponse(val response: String?)

    @PostMapping("/rag")
    fun ragPost(@RequestBody body: QueryMessage): QueryResponse {
        val advisors = mutableListOf<Advisor>(chatMemoryAdvisor)
        vectorStore?.let { vs ->
            queryIntent(body.query)?.let { intent ->
                takeIf { intent == QueryIntent.INFORMATIONAL }?.let { advisors.add(vs.ragAdvisor()) }
            }
        }
        return QueryResponse(
                chatClient
                        .prompt()
                        .user(body.query)
                        .advisors(advisors)
                        .call().content()
        )
    }

    enum class QueryIntent { CONVERSATIONAL, INFORMATIONAL }
    val intentChatClient = ChatClient.builder(chatModel).build()
    private fun queryIntent(query: String): QueryIntent? {
        val prompt = PromptTemplate("What is the intent of the following sentence: {sentence}? Classify either conversational or informational.")
                        .create(mapOf("sentence" to query))
        return intentChatClient.prompt(prompt).call().entity(object : ParameterizedTypeReference<QueryIntent>() {})
    }
}