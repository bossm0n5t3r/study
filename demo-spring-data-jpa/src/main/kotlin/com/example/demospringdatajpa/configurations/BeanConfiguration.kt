package com.example.demospringdatajpa.configurations

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class BeanConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    fun hikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @Primary
    fun dataSource(hikariConfig: HikariConfig): DataSource {
        return HikariDataSource(hikariConfig)
    }
}
