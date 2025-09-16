package com.sk.ai.content.pdf

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.filePathAsUrl
import com.sk.ai.content.ContentProcessor
import com.sk.ai.util.applyTransformation
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.reader.ExtractedTextFormatter
import org.springframework.ai.reader.pdf.PagePdfDocumentReader
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "content.pdf", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class PdfProcessor(
        private val contentProperties: ContentProperties,
        private val chatModel: ChatModel,
) : ContentProcessor {

    fun pdfDocument(filePath: String, pagesPerDocument: Int, paragraph: Boolean): MutableList<Document> {
        val extractor = ExtractedTextFormatter.builder()
                .withNumberOfTopTextLinesToDelete(0)
                .build()
        val readerConfig = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(extractor)
                .withPagesPerDocument(pagesPerDocument)
                .build()
        if (paragraph) {
            return ParagraphPdfDocumentReader(filePath, readerConfig).get()
        } else {
            return PagePdfDocumentReader(filePath, readerConfig).get()
        }
    }

    override fun get(): List<Document> {
        return contentProperties.pdf
                .filePathAsUrl()
                .flatMap { apply(pdfDocument(it, contentProperties.pdf.pagesPerDocument, contentProperties.pdf.paragraph)) }
    }

    override fun apply(t: List<Document>): List<Document> {
        return contentProperties.pdf.applyTransformation(chatModel, t)
    }
}