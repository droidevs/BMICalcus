package io.droidevs.bmicalc.data.pagging

import io.droidevs.bmicalc.domain.model.FavoredBmiRecord
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import io.droidevs.wallpaper.domain.result.errors.Error

class FavoredRecordPaginator(initialKey: Int,
                         onLoadUpdated: (Boolean) -> Unit,
                         onRequest: suspend (Int) -> Result<List<FavoredBmiRecord>, DatabaseError>,
                         getNextKey: suspend (List<FavoredBmiRecord>) -> Int,
                         onError: suspend (Error) -> Unit,
                         onSuccess: (List<FavoredBmiRecord>, Int) -> Unit
) : DefaultPaginator<Int, FavoredBmiRecord>(initialKey, onLoadUpdated, onRequest, getNextKey, onError,
    onSuccess
)