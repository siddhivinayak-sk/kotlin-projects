import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return all countries"

    request {
        url "/api/country"
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
        bodyMatchers {
            jsonPath('$.id', byRegex("[0-9]+"))
        }
    }
}