package com.example.demoeffectivekotlin.item39.annotationWithParameter

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExceptionTest(val value: KClass<out Throwable>)
