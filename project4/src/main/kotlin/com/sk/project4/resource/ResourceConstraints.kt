package com.sk.project4.resource

class ResourceConstraints {

    companion object {
        const val PERSON_API = "/api/person"
    }

    interface WithoutPasswordView
    interface WithPasswordView: WithoutPasswordView


}