package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.usecases.bmi.DeleteBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.GetBmiRecordByIdUseCase

class BmiUseCases(
    val add : AddBmiRecordUseCase,
    val delete : DeleteBmiRecordUseCase,
    val getById : GetBmiRecordByIdUseCase,
    val get : GetBmiRecordsUseCase
)
