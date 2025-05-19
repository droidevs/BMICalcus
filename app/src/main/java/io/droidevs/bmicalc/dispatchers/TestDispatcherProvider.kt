package io.droidevs.bmicalc.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Test implementation
class TestDispatcherProvider(
    private val testDispatcher: CoroutineDispatcher = Dispatchers.Main
) : CoroutineDispatcherProvider {
    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
    override val unconfined: CoroutineDispatcher = testDispatcher
}