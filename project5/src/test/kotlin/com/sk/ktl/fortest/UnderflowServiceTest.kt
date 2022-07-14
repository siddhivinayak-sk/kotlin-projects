package com.sk.ktl.fortest

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class UnderflowServiceTest {

    @Nested
    inner class VoteUpTest {
        private val questionId = 20
        private val voterId = 10

        private val mockQuestionRepository = mockk<IQuestionRepository>()
        private val mockUserRepository = mockk<IUserRepository>()

        private val underflowService = UnderflowService(mockQuestionRepository, mockUserRepository)

        @Test
        fun `should be able to initialize service`() {
            underflowService.shouldNotBeNull()
        }

        @Test
        fun `should be able to vote up question` () {
            val user = User(1, "Alice")
            val question = Question(questionId, user, "title", "question")

            user.changeReputation(3000)
            question.voteUp()
            question.voteUp()

            //Mocking of methods in repositories
            every { mockQuestionRepository.findQuestion(questionId) } returns question
            every { mockUserRepository.findUser(question.userId) } returns user
            every { mockUserRepository.findUser(voterId) } returns user
            every { mockQuestionRepository.update(question) } just  Runs
            every { mockUserRepository.update(user) } just Runs

            val votes = underflowService.voteUpQuestion(questionId, voterId)

            votes `should be` 3
        }

        @Test
        fun `should throw an exception if the question id is invalid` () {
            val user = User(1, "Alice")
            val question = Question(2, user, "title", "question")

            every { mockQuestionRepository.findQuestion(questionId) } throws Exception()
            invoking { underflowService.voteUpQuestion(questionId, voterId) } `should throw` ServiceException::class
        }
    }

    @Nested
    inner class VoteUpTestRelaxed {
        private val questionId = 20
        private val voterId = 10

        private val mockQuestionRepository = mockk<IQuestionRepository>(relaxUnitFun = true)
        private val mockUserRepository = mockk<IUserRepository>(relaxUnitFun = true)

        private val underflowService = UnderflowService(mockQuestionRepository, mockUserRepository)

        @Test
        fun `should be able to initialize service`() {
            underflowService.shouldNotBeNull()
        }

        @Test
        fun `should be able to vote up question` () {
            val user = User(1, "Alice")
            val question = Question(questionId, user, "title", "question")

            user.changeReputation(3000)
            question.voteUp()
            question.voteUp()

            //Mocking of methods in repositories
            every { mockQuestionRepository.findQuestion(questionId) } returns question
            every { mockUserRepository.findUser(question.userId) } returns user
            every { mockUserRepository.findUser(voterId) } returns user

            val votes = underflowService.voteUpQuestion(questionId, voterId)

            votes `should be` 3
        }

        @Test
        fun `should throw an exception if the question id is invalid` () {
            val user = User(1, "Alice")
            val question = Question(2, user, "title", "question")

            every { mockQuestionRepository.findQuestion(questionId) } throws Exception()
            invoking { underflowService.voteUpQuestion(questionId, voterId) } `should throw` ServiceException::class
        }
    }

    @Nested
    inner class WithAnnotations {
        private val questionId = 20
        private val voterId = 10

        @MockK
        private lateinit var mockQuestionRepository: IQuestionRepository
        @MockK
        private lateinit var mockUserRepository: IUserRepository

        init {
            MockKAnnotations.init(this)
        }

        private val underflowService = UnderflowService(mockQuestionRepository, mockUserRepository)

        @Test
        fun `should be able to vote up question` () {
            val user = User(1, "Alice")
            val question = Question(questionId, user, "title", "question")

            user.changeReputation(3000)
            question.voteUp()
            question.voteUp()

            //Mocking of methods in repositories
            every { mockQuestionRepository.findQuestion(questionId) } returns question
            every { mockUserRepository.findUser(question.userId) } returns user
            every { mockUserRepository.findUser(voterId) } returns user
            every { mockQuestionRepository.update(question) } just  Runs
            every { mockUserRepository.update(user) } just Runs

            val votes = underflowService.voteUpQuestion(questionId, voterId)

            votes `should be` 3
        }

        @Test
        fun `should throw an exception if the question id is invalid` () {
            val user = User(1, "Alice")
            val question = Question(2, user, "title", "question")

            every { mockQuestionRepository.findQuestion(questionId) } throws Exception()
            invoking { underflowService.voteUpQuestion(questionId, voterId) } `should throw` ServiceException::class
        }

    }

    @Nested
    inner class WithRelaxedAnnotations {
        private val questionId = 20
        private val voterId = 10

        @RelaxedMockK
        private lateinit var mockQuestionRepository: IQuestionRepository
        @RelaxedMockK
        private lateinit var mockUserRepository: IUserRepository

        init {
            MockKAnnotations.init(this)
        }

        private val underflowService = UnderflowService(mockQuestionRepository, mockUserRepository)

        @Test
        fun `should be able to vote up question` () {
            val user = User(1, "Alice")
            val question = Question(questionId, user, "title", "question")

            user.changeReputation(3000)
            question.voteUp()
            question.voteUp()

            //Mocking of methods in repositories
            every { mockQuestionRepository.findQuestion(questionId) } returns question
            every { mockUserRepository.findUser(question.userId) } returns user
            every { mockUserRepository.findUser(voterId) } returns user

            val votes = underflowService.voteUpQuestion(questionId, voterId)

            votes `should be` 3
        }

        @Test
        fun `should throw an exception if the question id is invalid` () {
            val user = User(1, "Alice")
            val question = Question(2, user, "title", "question")

            every { mockQuestionRepository.findQuestion(questionId) } throws Exception()
            invoking { underflowService.voteUpQuestion(questionId, voterId) } `should throw` ServiceException::class
        }

    }

    @Nested
    @ExtendWith(MockKExtension::class)
    inner class WithRelaxedAnnotationsJUnitExtensions {
        private val questionId = 20
        private val voterId = 10

        @MockK
        private lateinit var mockQuestionRepository: IQuestionRepository
        @RelaxedMockK
        private lateinit var mockUserRepository: IUserRepository


        @Test
        fun `should be able to vote up question` () {
            val underflowService = UnderflowService(mockQuestionRepository, mockUserRepository)
            val user = User(1, "Alice")
            val question = Question(questionId, user, "title", "question")

            user.changeReputation(3000)
            question.voteUp()
            question.voteUp()

            //Mocking of methods in repositories
            every { mockQuestionRepository.findQuestion(questionId) } returns question
            every { mockUserRepository.findUser(question.userId) } returns user
            every { mockUserRepository.findUser(voterId) } returns user
            every { mockQuestionRepository.update(question) } just  Runs

            val votes = underflowService.voteUpQuestion(questionId, voterId)

            votes `should be` 3
        }

        @Test
        fun `should throw an exception if the question id is invalid` () {
            val underflowService = UnderflowService(mockQuestionRepository, mockUserRepository)
            val user = User(1, "Alice")
            val question = Question(2, user, "title", "question")

            every { mockQuestionRepository.findQuestion(questionId) } throws Exception()
            invoking { underflowService.voteUpQuestion(questionId, voterId) } `should throw` ServiceException::class
        }

    }
}