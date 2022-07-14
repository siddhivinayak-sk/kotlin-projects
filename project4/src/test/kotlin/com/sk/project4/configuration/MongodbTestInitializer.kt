package com.sk.project4.configuration

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

class MongodbTestInitializer: ContractTestInitializer {

    @Container
    val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
        .withExposedPorts(27017)

    override fun initTest() {
        mongoDBContainer.start()
    }

    override fun cleanUpTest() {
        mongoDBContainer.stop()
    }
}