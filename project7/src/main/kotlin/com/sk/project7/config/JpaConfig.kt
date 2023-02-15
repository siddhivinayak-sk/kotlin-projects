package com.sk.project7.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.sk"])
@EntityScan(basePackages = ["com.sk"])
class JpaConfig {
}