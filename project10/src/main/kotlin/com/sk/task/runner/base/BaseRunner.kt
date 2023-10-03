package com.sk.task.runner.base

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.cloud.task.listener.annotation.AfterTask
import org.springframework.cloud.task.listener.annotation.BeforeTask
import org.springframework.cloud.task.listener.annotation.FailedTask
import org.springframework.cloud.task.repository.TaskExecution

open class BaseRunner: ApplicationRunner {

    override fun run(args: ApplicationArguments?) {}

    @BeforeTask
    open fun beforeTask(execution: TaskExecution) {}

    @AfterTask
    open fun afterTask(execution: TaskExecution) {}

    @FailedTask
    open fun failedTask(execution: TaskExecution, throwable: Throwable) {}
}