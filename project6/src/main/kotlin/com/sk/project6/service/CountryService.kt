package com.sk.project6.service

import com.sk.project6.dao.CountryDao
import com.sk.project6.model.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CountryService: ICountryService {

    private lateinit var countryDao: CountryDao

    @Autowired
    fun setCountryDao(countryDao: CountryDao) {
        this.countryDao = countryDao
    }

    override fun create(country: Country): Mono<Country> {
        return countryDao.create(country)
    }

    override fun update(country: Country): Mono<Country> {
        return countryDao.update(country)
    }

    override fun delete(country: Long): Mono<Country> {
        return countryDao.delete(country)
    }

    override fun get(country: Long): Mono<Country> {
        return countryDao.get(country)
    }

    override fun getAll(): Flux<Country> {
        return countryDao.getAll()
    }
}