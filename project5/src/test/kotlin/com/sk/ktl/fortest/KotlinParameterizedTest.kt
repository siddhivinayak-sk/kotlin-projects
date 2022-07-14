package com.sk.ktl.fortest

import org.junit.jupiter.api.TestInstance
import java.lang.annotation.RetentionPolicy

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KotlinParameterizedTest()
