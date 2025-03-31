package io.droidevs.bmicalc.domain.usecases

import io.droidevs.bmicalc.domain.BmiRecord

class SelectBmiRecordUseCase {


    operator fun  invoke(records: List<Long>, record: Long){
        if (records.size < 20)
            (records as ArrayList<Long>).add(record)
    }
}