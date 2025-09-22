package com.sk.ai.util

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever
import org.springframework.ai.template.st.StTemplateRenderer
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore

fun VectorStore.ragAdvisor(): Advisor {
    //return createQnAdvisor()
    //return createQnAAdvisorWithTemplate()
    //return naiveRag()
    return allowEmptyContextRag()
}

private fun VectorStore.createQnAdvisor(): QuestionAnswerAdvisor {
    return QuestionAnswerAdvisor.builder(this)
            .searchRequest(SearchRequest.builder().build())
            .build()
}

private fun VectorStore.createQnAAdvisorWithTemplate(): Advisor {
    val customPromptTemplate = PromptTemplate.builder()
            .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
            .template(
                    """
            <query>

            Context information is below.

			---------------------
			<question_answer_context>
			---------------------

			Given the context information and no prior knowledge, answer the query.

			Follow these rules:

			1. If the answer is not in the context, just say that you don't know.
			2. Avoid statements like "Based on the context..." or "The provided information...".
            
            """.trimIndent()
            )
            .build()
    return QuestionAnswerAdvisor.builder(this)
            .promptTemplate(customPromptTemplate)
            .build()
}

private fun VectorStore.naiveRag(): Advisor {
    return RetrievalAugmentationAdvisor.builder()
            .documentRetriever(
                    VectorStoreDocumentRetriever.builder()
                            .similarityThreshold(0.50)
                            .vectorStore(this)
                            .build()
            )
            .build()
}

private fun VectorStore.allowEmptyContextRag(): Advisor {
    return RetrievalAugmentationAdvisor.builder()
            .documentRetriever(
                    VectorStoreDocumentRetriever.builder()
                            .similarityThreshold(0.40)
                            .vectorStore(this)
                            .topK(10)
                            .build()
            )
            .queryAugmenter(
                    ContextualQueryAugmenter.builder()
                            .allowEmptyContext(true)
                            .build()
            )
            .build()
}

private fun VectorStore.advancedRag(chatClientBuilder: ChatClient.Builder): Advisor {
    return RetrievalAugmentationAdvisor.builder()
            .queryTransformers(
                    RewriteQueryTransformer.builder()
                            .chatClientBuilder(chatClientBuilder.build().mutate())
                            .build()
            )
            .documentRetriever(
                    VectorStoreDocumentRetriever.builder()
                            .similarityThreshold(0.50)
                            .vectorStore(this)
                            .build()
            )
            .build()
}
