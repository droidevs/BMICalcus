package io.droidevs.bmicalc.data.pagging


import io.droidevs.bmicalc.domain.model.BmiGoal
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError
import io.droidevs.wallpaper.domain.result.errors.Error


class GoalRecordPaginator(initialKey: Int,
                          onLoadUpdated: (Boolean) -> Unit,
                          onRequest: suspend (Int) -> Result<List<BmiGoal>, DatabaseError>,
                          getNextKey: suspend (List<BmiGoal>) -> Int,
                          onError: suspend (Error) -> Unit,
                          onSuccess: (List<BmiGoal>, Int) -> Unit
) : DefaultPaginator<Int, BmiGoal>(initialKey, onLoadUpdated, onRequest, getNextKey, onError,
    onSuccess
)