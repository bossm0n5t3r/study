package com.example.demospringdatajpa.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = ["com.example.demospringdatajpa"]
)
class JpaConfig {
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
