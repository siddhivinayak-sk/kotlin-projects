@file:Suppress("ClassName")

package com.sk.ktl.fortest

import org.amshove.kluent.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

class QuestionTest {

    val user = User(1, "Alice")

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class `Question Constructor` {
        @Test
        fun `should throw an exception if title is empty`() {
            Assertions.assertThrows(QuestionException::class.java) {
                var question = Question(1, user, "", "question")
            }
        }

        @Test
        fun `should throw an exception if body is empty`() {
            Assertions.assertThrows(QuestionException::class.java) {
                var question = Question(1, user, "title", "")
            }
        }

        @Test
        fun `should not throw an exception if question is valid`() {
            Assertions.assertDoesNotThrow() {
                var question = Question(1, user, "title", "question")
            }
        }

        @ParameterizedTest
        @CsvSource("'  ', '  '", "'  ', Question", "Title, '  '")
        fun `should throw an exception if title or question is invalid`(title:String, body: String) {
            Assertions.assertThrows(QuestionException::class.java) {
                Question(1, user, title, body)
            }
        }
    }

    @Nested
    @KotlinParameterizedTest
    inner class `Question Constructor with Kotlin Parameterized Test Annotation` {
        @Test
        fun `should throw an exception if title is empty`() {
            Assertions.assertThrows(QuestionException::class.java) {
                var question = Question(1, user, "", "question")
            }
        }

        @Test
        fun `should throw an exception if body is empty`() {
            Assertions.assertThrows(QuestionException::class.java) {
                var question = Question(1, user, "title", "")
            }
        }

        @Test
        fun `should not throw an exception if question is valid`() {
            Assertions.assertDoesNotThrow() {
                var question = Question(1, user, "title", "question")
            }
        }

        @ParameterizedTest
        @CsvSource("'  ', '  '", "'  ', Question", "Title, '  '")
        fun `should throw an exception if title or question is invalid`(title:String, body: String) {
            Assertions.assertThrows(QuestionException::class.java) {
                Question(1, user, title, body)
            }
        }
    }

    //companion object {}


    @Nested
    @KotlinParameterizedTest
    inner class `constructor should with method and annotation` {

        @Suppress("unused")
        fun titlesAndQuestions() = listOf(
            Arguments.of("", "Question"),
            Arguments.of("  ", "Question"),
            Arguments.of("  ", "   "),
            Arguments.of("", ""),
            Arguments.of("Title", ""),
            Arguments.of("Title", "   ")
        )

        @ParameterizedTest
        @MethodSource("titlesAndQuestions")
        fun `should throw an exception if title and question is invalid`(title: String, body: String) {
            //Normal
            //Assertions.assertThrows(QuestionException::class.java) {
            //    Question(1, user, title, body)
            //}

            //with Kluent
            invoking {
                Question(1, user, title, body)
            } `should throw` QuestionException::class
        }

    }

    @Nested
    inner class Answer {

        val user: User = User(1, "Alice")
        val question: Question = Question(1, user,  "title", "question")

        @Test
        fun `should have no answer` () {
            question.answers.shouldBeEmpty()
        }

        @Test
        fun `should have answer` () {
            val answer = Answer(1, user, "answer")
            question.addAnswer(answer)
            question.answers.shouldNotBeEmpty()
        }

        @Test
        fun `should contain an answer` () {
            val answer = Answer(1, user, "answer")
            question.addAnswer(answer)
            question.answers `should contain` answer
        }

        @Test
        fun `should not contain answer` () {
            val answer1 = Answer(1, user, "answer1")
            val answer2 = Answer(2, user, "answer2")
            question.addAnswer(answer1)
            question.answers `should not contain` answer2
        }

        @Test
        fun `should contain two answers` () {
            val answer1 = Answer(1, user, "answer1")
            val answer2 = Answer(2, user, "answer2")
            question.addAnswer(answer1)
            question.addAnswer(answer2)
            question.answers `should contain` answer1
            question.answers `should contain` answer2
            question.answers.size.shouldBe(2)
        }

    }


}