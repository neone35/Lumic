package com.arturmaslov.lumic

import com.arturmaslov.lumic.data.LocalDataSource
import com.arturmaslov.lumic.data.MainRepository
import com.arturmaslov.lumic.data.UserSetting
import com.arturmaslov.lumic.data.UserSettingDao
import com.arturmaslov.lumic.data.toEntity
import com.arturmaslov.lumic.utils.FlashMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryTest {

    private val localDataSource = mockk<LocalDataSource>()
    private lateinit var repository: MainRepository

    @Before
    fun setup() {
        repository = MainRepository(localDataSource)
    }

    @Test
    fun `getAllUsersSettings returns data from local data source`() = runTest {
        val users = listOf(UserSetting(
            1,
            1,
            colorSetting = 0,
            flashDuration = 0f,
            flashMode = FlashMode.NONE,
            sensitivity = 0f
        ))
        coEvery { localDataSource.getAllUsersSettings() } returns users

        val result = repository.getAllUsersSettings()

        assertEquals(users, result)
        coVerify { localDataSource.getAllUsersSettings() }
    }
}

