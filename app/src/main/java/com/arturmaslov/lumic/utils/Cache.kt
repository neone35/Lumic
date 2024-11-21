package com.arturmaslov.lumic.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.also


abstract class Cache<T>(
    private val dispatcher: CoroutineDispatcher
) {

    @Volatile
    private var variableCache: T? = null

    suspend fun set(value: T) {
        saveInVariable(value)
        withContext(dispatcher) { saveInStorage(value) }
    }

    suspend fun get(): T? {
        return getFromVariable() ?: withContext(dispatcher) { getFromStorage() }
            ?.also { saveInVariable(it) }
    }

    suspend fun clear() {
        clearVariable()
        withContext(dispatcher) { clearStorage() }
    }

    private fun getFromVariable(): T? {
        return variableCache
    }

    private fun saveInVariable(value: T) {
        this.variableCache = value
    }

    private fun clearVariable() {
        variableCache = null
    }

    protected abstract suspend fun getFromStorage(): T?

    protected abstract suspend fun saveInStorage(value: T)

    protected abstract suspend fun clearStorage()

}