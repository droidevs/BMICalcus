package io.droidevs.bmicalc.model

enum class UnitSystem {
    METRIC("metric"),
    IMPERIAL("imperial");

    var systemName: String = ""

    constructor(name: String){
        this.systemName = name
    }


    companion object {
        fun getUnit(unit: String): UnitSystem {
            return when (unit) {
                METRIC.name -> METRIC
                IMPERIAL.name -> IMPERIAL
                else -> throw IllegalArgumentException("Invalid unit system: $unit")
            }
        }
    }
}

enum class WeightUnit {
    KG("kg"),
    LB("lb");

    val text: String

    constructor(text: String){
        this.text = text
    }

    fun convert(weight: Float, targetUnitSystem: UnitSystem) : Float {
        val targetUnit = WeightUnit.getUnit(targetUnitSystem)
        if (targetUnit != this){
            return when (targetUnit) {
                KG -> weight * 2.20462f
                LB -> weight * 0.453592f
            }
        }
        return weight
    }
    companion object {
        fun getUnit(unit: String): WeightUnit {
            return when (unit) {
                KG.name -> KG
                LB.name -> LB
                else -> throw IllegalArgumentException("Invalid weight unit: $unit")
            }
        }

        fun getUnit(unitSystem: UnitSystem) : WeightUnit {
            return when (unitSystem) {
                UnitSystem.METRIC -> KG
                UnitSystem.IMPERIAL -> LB
            }
        }
    }
}

enum class HeightUnit {
    CM("cm"),
    INCH("inch");

    val text: String

    constructor(text: String){
        this.text = text
    }

    fun convert(height: Float, targetUnitSystem: UnitSystem) : Float {
        val targetUnit = HeightUnit.getUnit(targetUnitSystem)
        if (targetUnit != this){
            return when (targetUnit) {
                CM -> height * 2.54f
                INCH -> height / 2.54f
            }
        }
        return height
    }

    companion object {
        fun getUnit(unit: String): HeightUnit {
            return when (unit) {
                CM.name -> CM
                INCH.name -> INCH
                else -> throw IllegalArgumentException("Invalid height unit: $unit")
            }
        }

        fun getUnit(unitSystem: UnitSystem) : HeightUnit {
            return when (unitSystem) {
                UnitSystem.METRIC -> CM
                UnitSystem.IMPERIAL -> INCH
            }
        }
    }
}
