package com.example.simpsonsinfo.domain.usecase

import com.example.simpsonsinfo.domain.entity.CharacterEntity
import com.example.simpsonsinfo.domain.entity.SimpsonsEntity
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class SimpsonsUsecaseTest {

    @RelaxedMockK
    private lateinit var repository: SimpsonsRepository

    lateinit var simpsonsUsecase: SimpsonsUsecase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        simpsonsUsecase = SimpsonsUsecase(repository)
    }

    @Test
    fun getSimpsonsEmptyList() = runBlocking {
        // GIVEN
        val entity =
            SimpsonsEntity(count = 0, next = "", prev = "", pages = 0, results = emptyList())
        coEvery { repository.doSimpsons("1") } returns entity

        // WHEN
        val response = simpsonsUsecase(page = "1")

        // THEN
        coVerify(exactly = 1) { repository.doSimpsons(page = "1") }
        assert(response == entity)
    }

    @Test
    fun getSimpsonsList() = runBlocking {
        // GIVEN
        val entity =
            SimpsonsEntity(
                count = 0, next = "", prev = "", pages = 0, results = listOf(
                    CharacterEntity(
                        id = 1,
                        age = 34,
                        birthdate = "01/01/1991",
                        name = "Homer",
                        gender = "Male",
                        occupation = "secure_inspector",
                        portrait_path = "...homer.png",
                        phrases = listOf("Hi, my name is Homer"),
                        status = "Alive"
                    ),
                    CharacterEntity(
                        id = 2,
                        age = 12,
                        birthdate = "01/01/2010",
                        name = "Bart",
                        gender = "Male",
                        occupation = "student",
                        portrait_path = "...bart.png",
                        phrases = listOf("Hello World"),
                        status = "Alive"
                    )
                )
            )
        coEvery { repository.doSimpsons("1") } returns entity

        // WHEN
        val response = simpsonsUsecase(page = "1")

        // THEN
        coVerify(exactly = 1) { repository.doSimpsons(page = "1") }
        assert(response == entity)
    }

}