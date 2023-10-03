package com.sk.task.runner.base

interface BaseWriter {
    fun write(output: BaseOutput, config: Map<String, String>)
}
