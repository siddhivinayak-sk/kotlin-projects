package com.sk.task.runner.base

interface BaseReader {
    fun read(config: Map<String, String>): BaseInput
}
