package com.sk.ktl.enums

enum class InMediaType(val type: String) {
    PDF("pdf"),
    XML("xml"),
    JSON("json"),
    TEXT("text"),
    XLSX("xlsx"),
    DOCX("docx"),
    JPG("jpg"),
    PNG("png"),
    TIF("tif"),
    GIF("gif")
}

fun resolve(type: String): InMediaType {
    return InMediaType.values().first { it.type == type.lowercase() }
}

fun main(args: Array<String>) {
    println(resolve("pdf1").type)
}