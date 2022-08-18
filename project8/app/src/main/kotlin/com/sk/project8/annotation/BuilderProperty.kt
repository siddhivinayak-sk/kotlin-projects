package com.sk.project8.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.lang.annotation.Retention

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
annotation class BuilderProperty(val value: String = "", val name: String = "", val priority: Int)
