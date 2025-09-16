package com.sk.ai.config.properties

import org.springframework.ai.model.transformer.SummaryMetadataEnricher
import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Paths

@ConfigurationProperties(prefix = "content")
data class ContentProperties(
        val pdf: ContentDetail = ContentDetail(),
        val txt: ContentDetail = ContentDetail(),
        val md: ContentDetail = ContentDetail(),
        val json: ContentDetail = ContentDetail(),
        val html: ContentDetail = ContentDetail(),
        val tika: ContentDetail = ContentDetail(),
)

data class ContentDetail(
        val enabled: Boolean = false,
        val inputDirectory: String = "",
        val txtField: String = "",
        val extensions: List<String> = emptyList(),
        val transformers: Map<String, TransformerConfig> = emptyMap(),
        val paragraph: Boolean = false,
        val pagesPerDocument: Int = 1,
)

data class TransformerConfig(
        val defaultChunkSize: Int = 800,
        val minChunkSizeChars: Int = 350,
        val minChunkLengthToEmbed: Int = 5,
        val maxNumChunks: Int = 10000,
        val keepSeparator: Boolean = true,
        val keywordCount: Int = 10000,
        val summaryTypes: List<SummaryMetadataEnricher.SummaryType> = listOf(SummaryMetadataEnricher.SummaryType.CURRENT)
)

fun ContentDetail.filePathAsUrl() = Paths
        .get(inputDirectory)
        .toFile()
        .listFiles { _, name -> extensions.any { ext -> name.endsWith(".$ext") } }?.toList()
        ?.map { it.toURI().toString() } ?: emptyList()