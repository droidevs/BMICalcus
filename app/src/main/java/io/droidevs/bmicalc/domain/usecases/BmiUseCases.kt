package io.droidevs.bmicalc.domain.usecases

class BmiUseCases(
    val add : AddBmiRecordUseCase,
    val delete : DeleteBmiRecordUseCase,
    val getById : GetBmiRecordsUseCase,
    val get : GetBmiRecordsUseCase
)