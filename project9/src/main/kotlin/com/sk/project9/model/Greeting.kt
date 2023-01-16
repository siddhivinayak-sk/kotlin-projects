package com.sk.project9.model

class Greeting() {
    private var content: String = ""

    constructor(content: String): this() {
        this.content = content
    }

    public final fun setContent(content: String) {
        this.content = content
    }

    public final fun getContent(): String {
        return content
    }

}