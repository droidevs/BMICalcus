package io.droidevs.bmicalc.domain.pager

interface Paginator<Key,Item> {

    suspend fun loadNextPage()

    fun reset()
}