package com.sk.ai.controller

import org.springframework.ai.image.ImageModel
import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.image.ImageResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/images")
@ConditionalOnProperty(prefix = "app.image", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ImageController(val imageModel: ImageModel) {


    @GetMapping("/generate")
    fun generateImage(prompt: String): ImageResponse {
        val imagePrompt = ImagePrompt(prompt)
        return imageModel.call(imagePrompt)
    }
}