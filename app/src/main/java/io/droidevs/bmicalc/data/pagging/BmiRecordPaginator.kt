package io.droidevs.bmicalc.data.pagging

import io.droidevs.bmicalc.domain.BmiRecord

class BmiRecordPaginator(initialKey: Int,
                         onLoadUpdated: (Boolean) -> Unit,
                         onRequest: suspend (Int) -> Result<List<BmiRecord>>,
                         getNextKey: suspend (List<BmiRecord>) -> Int,
                         onError: suspend (Throwable) -> Unit,
                         onSuccess: (List<BmiRecord>, Int) -> Unit
) : DefaultPaginator<Int, BmiRecord>(initialKey, onLoadUpdated, onRequest, getNextKey, onError,
    onSuccess
)