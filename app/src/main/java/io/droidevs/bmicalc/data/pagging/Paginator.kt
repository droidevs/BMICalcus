package io.droidevs.bmicalc.data.pagging

interface Paginator<Key,Item> {

    suspend fun loadNextPage()

    fun reset()
}