package com.aurabeat.data.backend

import com.aurabeat.core.Resource
import kotlin.random.Random
import kotlinx.coroutines.delay

/**
 * Shared fake backend utility used by repositories to simulate latency and occasional failures.
 * Swap this layer for Retrofit, Ktor, or another API client later without changing UI code.
 */
object FakeBackendSimulator {
    private const val defaultFailureRate = 0.12f
    private const val defaultMinDelayMs = 180L
    private const val defaultMaxDelayMs = 850L

    suspend fun <T> request(
        label: String,
        minDelayMs: Long = defaultMinDelayMs,
        maxDelayMs: Long = defaultMaxDelayMs,
        failureRate: Float = defaultFailureRate,
        block: () -> T
    ): Resource<T> {
        delay(Random.nextLong(minDelayMs, maxOf(minDelayMs + 1, maxDelayMs + 1)))
        val roll = Random.nextFloat()
        return when {
            roll < failureRate -> Resource.Error("$label is temporarily unavailable")
            else -> Resource.Success(block())
        }
    }
}
