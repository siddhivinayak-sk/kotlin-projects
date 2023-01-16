package com.sk.project9.model

class HelloMessage() {
    private var name: String = ""

    constructor(name: String): this() {
        this.name = name
    }

    public final fun getName(): String {
        return name
    }

    public final fun setName(name: String) {
        this.name = name
    }
}