import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should delete country by id=1"

    request {
        url "/api/country/1"
        method DELETE()
        headers {
            header(contentType(), applicationJson())
        }
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        bodyMatchers {
            jsonPath("id", byRegex("[0-9]+"))
        }
    }
}