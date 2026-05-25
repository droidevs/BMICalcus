package io.droidevs.bmicalc.data.pagging

import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.pager.DefaultPaginator
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.bmicalc.domain.result.errors.DatabaseError
import io.droidevs.bmicalc.domain.result.errors.Error

class BmiRecordPaginator(initialKey: Int,
                         onLoadUpdated: (Boolean) -> Unit,
                         onRequest: suspend (Int) -> Result<List<BmiRecord>, DatabaseError>,
                         getNextKey: suspend (List<BmiRecord>) -> Int,
                         onError: suspend (Error) -> Unit,
                         onSuccess: (List<BmiRecord>, Int) -> Unit
) : DefaultPaginator<Int, BmiRecord>(initialKey, onLoadUpdated, onRequest, getNextKey, onError,
    onSuccess
)