package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.BmiRecord
import io.droidevs.bmicalc.ui.model.BmiRecordUi

class SelectBmiRecordUseCase {


    operator fun  invoke(records: List<BmiRecordUi>, recordId: Long){
        val record = records.find {
            it.id == recordId
        }
        record?.isSelected = true
    }
}