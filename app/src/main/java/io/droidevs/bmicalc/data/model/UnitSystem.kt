package io.droidevs.bmicalc.data.model

import io.droidevs.bmicalc.domain.Range

enum class UnitSystem {
    METRIC("metric"),
    IMPERIAL("imperial");

    var systemName: String = ""

    constructor(name: String){
        this.systemName = name
    }


    fun convertHeight(
        height: Float,
        unitSystem: UnitSystem
    ) : Float {
        val origineUnit = HeightUnit.getUnit(this)
        val targetUnit = HeightUnit.getUnit(unitSystem)
        if (origineUnit != targetUnit) {
            return when (targetUnit) {
                HeightUnit.CM -> {
                    if (origineUnit == HeightUnit.INCH)
                        height * 2.54f
                    else
                        height
                }

                HeightUnit.INCH -> {
                    if (origineUnit == HeightUnit.CM)
                        height / 2.54f
                    else
                        height
                }
            }
        }
        return height
    }

    fun convertWeight(
        weight: Float,
        unitSystem: UnitSystem
    ) : Float {
        val origineUnit = WeightUnit.getUnit(this)
        val targetUnit = WeightUnit.getUnit(unitSystem)
        if (origineUnit != targetUnit) {
            return when (targetUnit) {
                WeightUnit.KG -> {
                    if (origineUnit == WeightUnit.LB)
                        weight * 0.453592f
                    else
                        weight
                }
                WeightUnit.LB -> {
                    if (origineUnit == WeightUnit.KG)
                        weight / 0.453592f
                    else
                        weight
                }
            }
        }
        return weight
    }

    companion object {
        fun getUnit(unit: String): UnitSystem {
            return when (unit) {
                METRIC.name -> METRIC
                IMPERIAL.name -> IMPERIAL
                else -> throw IllegalArgumentException("Invalid unit system: $unit")
            }
        }

        val DEFAULT = UnitSystem.METRIC
    }
}

enum class WeightUnit(
    val text : String,
    val defaultRange: Range,
    val validRange: ClosedFloatingPointRange<Float>,
) {
    KG(
        text = "kg",
        defaultRange = Range(50f, 100f),
        validRange = 30f..200f,
    ),
    LB(
        text = "lb",
        defaultRange = Range(110f, 220f), // ~50-100kg in lbs
        validRange = 66f..440f, // ~30-200kg in lbs
    );

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

enum class HeightUnit(
    val text: String,
    val defaultRange: Range,
    val validRange: ClosedFloatingPointRange<Float>,
    private val toDefault: (Float) -> Float,
    private val fromDefault: (Float) -> Float
) {
    CM(
        text = "cm",
        defaultRange = Range(150f, 190f),
        validRange = 100f..250f,
        toDefault = { it }, // cm is our default unit
        fromDefault = { it }
    ),
    INCH(
        text = "inch",
        defaultRange = Range(4.92f, 6.23f), // ~150-190cm in feet
        validRange = 3.28f..8.20f, //
        toDefault = { it *  2.54f }, // inch to cm
        fromDefault = { it /  2.54f } // cm to inch
    );


    fun convert(height: Float, targetUnitSystem: UnitSystem) : Float {
        val targetUnit = getUnit(targetUnitSystem)
        if (targetUnit != this){
            return when (targetUnit) {
                CM -> toDefault(height)
                INCH -> fromDefault(height)
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
