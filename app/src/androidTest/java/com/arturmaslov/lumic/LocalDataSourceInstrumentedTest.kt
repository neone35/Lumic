package com.arturmaslov.lumic

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arturmaslov.lumic.data.LocalDataSource
import com.arturmaslov.lumic.data.LocalDatabase
import com.arturmaslov.lumic.data.UserSetting
import com.arturmaslov.lumic.data.UserSettingDao
import com.arturmaslov.lumic.utils.FlashMode
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LocalDataSourceInstrumentedTest {

    private lateinit var database: LocalDatabase
    private lateinit var dao: UserSettingDao
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.userSettingDao ?: throw IllegalStateException("UserSettingDao is null")
        localDataSource = LocalDataSource(database, Dispatchers.Main)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetUserSetting() = runTest {
        val user = UserSetting(
            1,
            1,
            colorSetting = 0,
            flashDuration = 0f,
            flashMode = FlashMode.NONE,
            sensitivity = 0f
        )
        localDataSource.insertUserSetting(user)

        val result = localDataSource.getUserSetting(1)
        assertEquals(user, result)
    }
}
