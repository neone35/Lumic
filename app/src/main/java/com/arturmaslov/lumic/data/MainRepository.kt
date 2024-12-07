package com.arturmaslov.lumic.data

class MainRepository(
    private val mLocalDataSource: LocalDataSource
) : LocalData {

    override suspend fun getAllUsersSettings(): List<UserSetting>? =
        mLocalDataSource.getAllUsersSettings()

    override suspend fun getUserSetting(id: Int) =
        mLocalDataSource.getUserSetting(id)

    override suspend fun getOneUserSettings(userId: Int) =
        mLocalDataSource.getOneUserSettings(userId)

    override suspend fun deleteAllUserSettings() =
        mLocalDataSource.deleteAllUserSettings()

    override suspend fun deleteOneUserSettings(userId: Int) =
        mLocalDataSource.deleteOneUserSettings(userId)

    override suspend fun insertUserSetting(userSetting: UserSetting) =
        mLocalDataSource.insertUserSetting(userSetting)

}