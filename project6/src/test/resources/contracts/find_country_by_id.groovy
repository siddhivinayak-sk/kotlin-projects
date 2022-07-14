import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return country by id=1"

    request {
        url "/api/country/1"
        method GET()
        headers {
            header(contentType(), applicationJson())
        }
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: 1,
                name: "India",
                code: "IN"
        )
        bodyMatchers {
            jsonPath("id", byRegex("[0-9]+"))
        }
    }
}