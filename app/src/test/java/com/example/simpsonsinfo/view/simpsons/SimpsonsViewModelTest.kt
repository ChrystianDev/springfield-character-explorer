package com.example.simpsonsinfo.view.simpsons

import com.example.simpsonsinfo.domain.usecase.SimpsonsUsecase
import com.example.simpsonsinfo.utils.mock.SimpsonsMocks.aSimpsonsEntityMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class SimpsonsViewModelTest {

    @RelaxedMockK
    private lateinit var simpsonsUsecase: SimpsonsUsecase

    private lateinit var simpsonsViewModel: SimpsonsViewModel

    private lateinit var dispatcher: TestDispatcher

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCharacters() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        simpsonsViewModel = SimpsonsViewModel(simpsonsUsecase, dispatcher)

        // When
        coEvery { simpsonsUsecase(page = "1") } returns
                aSimpsonsEntityMock()

        // Given
        simpsonsViewModel
        advanceUntilIdle()

        // Then
        coVerify { simpsonsUsecase(page = "1") }
        assertEquals(true, simpsonsViewModel.uiState.value.entity == aSimpsonsEntityMock())
    }

    @Test
    fun toggleLoading() {
        setDispatcherAndViewModel()
        // Check initial value
        assertEquals(false, simpsonsViewModel.isLoading.value)

        // Run function -> change to "true"
        simpsonsViewModel.toggleLoading()

        // Verify
        assertEquals(true, simpsonsViewModel.isLoading.value)

        // Run function -> change to "false"
        simpsonsViewModel.toggleLoading()

        // Verify
        assertEquals(false, simpsonsViewModel.isLoading.value)
    }

    @Test
    fun setIndexSelected() = runTest {
        setDispatcherAndViewModel()
        val index = 7

        // Check initial value
        assertEquals(0, simpsonsViewModel.indexSelected.value)

        // Run function
        simpsonsViewModel.setIndexSelected(index)

        // Verify indexSelected, this value should be equal to index
        assertEquals(index, simpsonsViewModel.indexSelected.value)
    }

    @Test
    fun incrementPage() = runTest {
        setDispatcherAndViewModel()
        val page = "2"

        // Check initial value
        assertEquals("1", simpsonsViewModel.page.value)

        // Run function, increase the page by 1 unit
        simpsonsViewModel.incrementPage()

        // Verify page, this value should be equal to 2
        assertEquals(page, simpsonsViewModel.page.value)
    }

    @Test
    fun decrementPage() = runTest {
        setDispatcherAndViewModel()
        val page = "0"

        // Check initial value
        assertEquals("1", simpsonsViewModel.page.value)

        // Run function, decrease the page by 1 unit
        simpsonsViewModel.decrementPage()

        // Verify page, this value should be equal to 0
        assertEquals(page, simpsonsViewModel.page.value)
    }

    fun setDispatcherAndViewModel() {
        dispatcher = StandardTestDispatcher()
        simpsonsViewModel = SimpsonsViewModel(simpsonsUsecase, dispatcher)
    }

    // TODO Pendiente de desarrollar Test para los metodos: speakItemCharacters, speakItemDetail, toggleMute
}


