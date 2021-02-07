package com.example.demoeffectivekotlin.item39.annotationWithArrayParameter

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExceptionTest(vararg val value: KClass<out Throwable>)
