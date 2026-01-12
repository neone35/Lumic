package com.arturmaslov.lumic

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arturmaslov.lumic.data.LocalDataSource
import com.arturmaslov.lumic.data.LocalDatabase
import com.arturmaslov.lumic.data.MainRepository
import com.arturmaslov.lumic.data.UserSetting
import com.arturmaslov.lumic.utils.FlashMode
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MainRepositoryIntegrationTest {

    private lateinit var database: LocalDatabase
    private lateinit var repository: MainRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            LocalDatabase::class.java
        ).build()

        val localDataSource = LocalDataSource(
            database,
            Dispatchers.IO
        )

        repository = MainRepository(localDataSource)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadUserSetting() = runTest {
        // given
        val user = UserSetting(
            1,
            1,
            colorSetting = 0,
            flashDuration = 0f,
            flashMode = FlashMode.NONE,
            sensitivity = 0f
        )

        // when
        repository.insertUserSetting(user)
        val result = repository.getOneUserSettings(1)

        // then
        // Unit & integration tests → Truth
        // UI tests → Espresso assertions
        assertThat(result).isNotNull()
        assertThat(result!!.size).isEqualTo(1)
        assertThat(result[0]).isEqualTo(user)
    }
}
