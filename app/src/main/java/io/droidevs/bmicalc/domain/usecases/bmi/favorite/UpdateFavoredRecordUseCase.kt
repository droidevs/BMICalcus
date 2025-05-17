package io.droidevs.bmicalc.domain.usecases.bmi.favorite

import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import io.droidevs.bmicalc.domain.model.BmiRecord
import io.droidevs.bmicalc.domain.model.FavoredBmiRecord
import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.result.onSuccessSuspendWithResult
import io.droidevs.wallpaper.domain.result.Result
import io.droidevs.wallpaper.domain.result.errors.DatabaseError

class UpdateFavoredRecordUseCase(
    private val repository : FavoriteBmiRepository,
    private val bmiRepository : BmiRepository
) {

    suspend operator fun invoke(favoredRecord : FavoredBmiRecord) : Result<Int , DatabaseError>{
        return repository.updateCustomNote(favoredRecordId = favoredRecord.id, note = favoredRecord.note?: "").onSuccessSuspendWithResult {
            bmiRepository.updateRecord(
                BmiRecord(
                    id = favoredRecord.recordId,
                    bmi = favoredRecord.bmi,
                    weight = favoredRecord.weight,
                    height = favoredRecord.height,
                    date = favoredRecord.date
                )
            )
        }
    }
}