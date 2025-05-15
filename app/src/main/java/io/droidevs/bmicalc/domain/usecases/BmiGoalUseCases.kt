package io.droidevs.bmicalc.domain.usecases

class BmiGoalUseCases(
    val delete : DeleteGoalUseCase,
    val get : GetBmiGoalRecordsUseCase,
    val add: AddGoalRecordUseCase,
    val getActive : GetActiveBmiGoalUseCase,
    val setActive : SetActiveBmiGoalUseCase,
)