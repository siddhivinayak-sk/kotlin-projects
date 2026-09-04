import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return person by id=1"

    request {
        url "/api/person/101"
        method GET()
        headers {
            header(contentType(), applicationJson())
            //header(authorization(), $(execute("basicAuth(\"admin:adminpwd\")")))
            header(authorization(), "Basic YWRtaW46YWRtaW5wd2Q=")
        }
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: 101,
                name: "User-101",
                password: "Pass101"
        )
        bodyMatchers {
            jsonPath("id", byRegex("[0-9]+"))
        }
    }
}