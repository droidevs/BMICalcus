package io.droidevs.bmicalc.domain.pager

import io.droidevs.bmicalc.domain.result.onFailureSuspend
import io.droidevs.bmicalc.domain.result.onSuccessSuspend
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import io.droidevs.bmicalc.domain.result.errors.Error

abstract class DefaultPaginator<Key,Item>(
    val initialKey : Key,
    private inline val onLoadUpdated : (Boolean) -> Unit,
    private inline val onRequest : suspend (nextKey : Key) -> Result<List<Item>, DatabaseError>,
    private inline val getNextKey : suspend (items : List<Item>) -> Key,
    private inline val onError : suspend (error : Error) -> Unit,
    private inline val onSuccess : (items : List<Item> , newKey : Key) -> Unit
) : Paginator<Key, Item> {


    private var currentKey = initialKey
    private var isMakingRequest = false



    override suspend fun loadNextPage() {
        if (isMakingRequest)
            return

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)
        isMakingRequest = false

        result.onSuccessSuspend { items ->
            currentKey = getNextKey(items)

            onSuccess(items,currentKey)

            onLoadUpdated(false)
        }.onFailureSuspend {
            onError(it)
            onLoadUpdated(false)
            return@onFailureSuspend
        }
    }

    override fun reset() {
        currentKey = initialKey
    }

}