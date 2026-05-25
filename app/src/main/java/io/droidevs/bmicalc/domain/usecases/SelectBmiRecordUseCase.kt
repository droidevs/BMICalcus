package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.ui.model.BmiRecordUi

class SelectBmiRecordUseCase {

    operator fun invoke(records: List<BmiRecordUi>, recordId: Long): List<BmiRecordUi> {
        return records.map { record ->
            if (record.id == recordId) record.copy(isSelected = true) else record
        }
    }
}
