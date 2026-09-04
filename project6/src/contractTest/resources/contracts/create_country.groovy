import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should create country"

    request {
        url "/api/country"
        method POST()
        headers {
            header(contentType(), applicationJson())
        }
        body(
                id: $(anyPositiveInt()),
                name: $(regex('[0-9a-zA-Z]{10}')),
                code: $(regex('[A-Z]{2}'))
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: fromRequest().body('id'),
                name: fromRequest().body('name'),
                code: fromRequest().body('code')
        )
        bodyMatchers {
            jsonPath("id", byRegex("[0-9]+"))
        }
    }
}