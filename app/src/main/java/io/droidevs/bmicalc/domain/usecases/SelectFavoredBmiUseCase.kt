package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.ui.model.BmiRecordUi
import io.droidevs.bmicalc.ui.model.FavoredBmiUi


class SelectFavoredBmiUseCase {

    operator fun invoke(records: List<FavoredBmiUi>, favoredId: Long): List<FavoredBmiUi> {
        return records.map { record ->
            if (record.id == favoredId) record.copy(isSelected = true) else record
        }
    }
}
