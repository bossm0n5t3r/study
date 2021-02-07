package com.example.demoeffectivekotlin.item39.repeatableAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExceptionTestContainer(vararg val value: ExceptionTest)
