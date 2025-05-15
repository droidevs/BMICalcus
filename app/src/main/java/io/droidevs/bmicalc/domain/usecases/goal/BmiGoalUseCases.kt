package io.droidevs.bmicalc.domain.usecases.goal

import io.droidevs.bmicalc.domain.usecases.AddGoalRecordUseCase
import io.droidevs.bmicalc.domain.usecases.GetBmiGoalRecordsUseCase

class BmiGoalUseCases(
    val delete : DeleteGoalUseCase,
    val get : GetBmiGoalRecordsUseCase,
    val add: AddGoalRecordUseCase,
    val getActive : GetActiveBmiGoalUseCase,
    val setActive : SetActiveBmiGoalUseCase,
)