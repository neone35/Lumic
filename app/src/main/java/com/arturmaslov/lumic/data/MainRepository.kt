package com.arturmaslov.lumic.data

class MainRepository(
    private val mLocalDataSource: LocalDataSource
) : LocalData {

    override suspend fun getAllUsersSettings(): List<UserSetting>? =
        mLocalDataSource.getAllUsersSettings()

    override suspend fun getUserSetting(id: Int) =
        mLocalDataSource.getUserSetting(id)

    override suspend fun getOneUserSettings(userName: String) =
        mLocalDataSource.getOneUserSettings(userName)

    override suspend fun deleteAllUserSettings() =
        mLocalDataSource.deleteAllUserSettings()

    override suspend fun deleteOneUserSettings(userName: String) =
        mLocalDataSource.deleteOneUserSettings(userName)

    override suspend fun insertUserSetting(userSetting: UserSetting) =
        mLocalDataSource.insertUserSetting(userSetting)

}