package com.sk.task.runner.base

interface BaseProcessor {
    fun process(input: BaseInput, config: Map<String, String>): BaseOutput
}