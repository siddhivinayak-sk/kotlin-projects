package com.sk.project4.model.entity

import com.fasterxml.jackson.annotation.JsonView
import com.sk.project4.resource.ResourceConstraints
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Document("person")
class Person {
    @Id
    @JsonView(ResourceConstraints.WithoutPasswordView::class)
    var id: Long = 0
    @JsonView(ResourceConstraints.WithoutPasswordView::class)
    var name: String = ""
    @JsonView(ResourceConstraints.WithPasswordView::class)
    var password: String = ""

    constructor(): super() {}
    constructor(id: Long, name: String, password: String): this() {
        this.id = id
        this.name = name
        this.password = password
    }

}