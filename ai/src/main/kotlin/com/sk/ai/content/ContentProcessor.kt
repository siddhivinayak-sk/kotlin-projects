package com.sk.ai.content

import org.springframework.ai.document.DocumentReader
import org.springframework.ai.document.DocumentTransformer

interface ContentProcessor: DocumentReader, DocumentTransformer