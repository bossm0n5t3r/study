package com.example.demoeffectivekotlin.item39.repeatableAnnotation

import kotlin.reflect.KClass

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@java.lang.annotation.Repeatable(ExceptionTestContainer::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class ExceptionTest(val value: KClass<out Throwable>)
