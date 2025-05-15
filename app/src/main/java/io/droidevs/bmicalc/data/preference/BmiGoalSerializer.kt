package io.droidevs.bmicalc.data.preference

import androidx.datastore.core.Serializer
import io.droidevs.bmicalc.data.model.ActiveBmiGoal
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

class BmiGoalSerializer : Serializer<ActiveBmiGoal> {
    override val defaultValue: ActiveBmiGoal
        get() = ActiveBmiGoal()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): ActiveBmiGoal {
        val bmiGoal = Json.decodeFromStream(
            deserializer = ActiveBmiGoal.serializer(),
            stream = input
        )
        return bmiGoal
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: ActiveBmiGoal, output: OutputStream) {
        Json.encodeToStream(
            serializer = ActiveBmiGoal.serializer(),
            stream = output,
            value = t
        )
    }
}