package com.example.demogradledependencies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoGradleDependenciesApplication

fun main(args: Array<String>) {
	runApplication<DemoGradleDependenciesApplication>(*args)
}
