package com.mobilitydevai.sce.domain.usecase

import com.mobilitydevai.sce.domain.repository.SimpsonsRepository
import com.mobilitydevai.sce.utils.mock.SimpsonsMocks.aSimpsonsEmptyListEntityMock
import com.mobilitydevai.sce.utils.mock.SimpsonsMocks.aSimpsonsEntityMock
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
        coEvery { repository.doSimpsons("1") } returns aSimpsonsEmptyListEntityMock()

        // WHEN
        val response = simpsonsUsecase(page = "1")

        // THEN
        coVerify(exactly = 1) { repository.doSimpsons(page = "1") }
        assert(response == aSimpsonsEmptyListEntityMock())
    }

    @Test
    fun getSimpsonsList() = runBlocking {
        // GIVEN
        coEvery { repository.doSimpsons("1") } returns aSimpsonsEntityMock()

        // WHEN
        val response = simpsonsUsecase(page = "1")

        // THEN
        coVerify(exactly = 1) { repository.doSimpsons(page = "1") }
        assert(response == aSimpsonsEntityMock())
    }

}