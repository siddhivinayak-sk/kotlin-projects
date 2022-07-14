package com.sk.ktl.fortest

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should be true`
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserTest {

    var user: User = User(1, "Alice")

    @Test
    fun `should be able to increase reputation`() {
        user.changeReputation(10)
        //assertEquals(10, user.reputation)
        //user.reputation.`should be`(10)
        user.reputation `should be` 10
    }

    @Test
    fun `should be able to decrease reputation`() {
        user.changeReputation(10)
        user.changeReputation(-5)
        assertEquals(5, user.reputation)
    }

    @Nested
    inner class KluentUserTest {

        val editReputationLimit = 2000

        @Test
        fun `edit if reputation is greater than 2000`() {
            user.changeReputation(editReputationLimit + 1)
            user.canEditPost().shouldBeTrue()
        }

        @Test
        fun `edit if reputation is equal to 2000`() {
            user.changeReputation(editReputationLimit)
            Assertions.assertFalse(user.canEditPost())
        }

        @Test
        fun `edit if reputation is less than 2000`() {
            user.changeReputation(editReputationLimit - 1)
            Assertions.assertFalse(user.canEditPost())
        }
    }
}