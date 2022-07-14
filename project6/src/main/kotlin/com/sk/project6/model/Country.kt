package com.sk.project6.model

class Country() {
    var id: Long = 0
    var name: String = ""
    var code: String = ""

    constructor(id: Long, name: String, code: String): this() {
        this.id = id
        this.name = name
        this.code = code
    }
}