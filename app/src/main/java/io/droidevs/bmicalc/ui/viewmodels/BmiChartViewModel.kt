package io.droidevs.bmicalc.ui.viewmodels

import android.app.Application
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import io.droidevs.bmicalc.domain.model.BmiRecord
import java.io.File

class BmiChartViewModel(
    application: Application
) : AndroidViewModel(application) {

    fun exportDataAsCsv(data: List<BmiRecord>) {
        val context = getApplication() as Application
        val csvHeader = "Date,BMI,Weight,Height,Category\n"
        val csvContent = data.joinToString("\n") { record ->
            "${record.date},${record.bmi},${record.weight},${record.height}"
        }

        val file = File(context.getExternalFilesDir(null), "bmi_data_${System.currentTimeMillis()}.csv")
        file.writeText(csvHeader + csvContent)

        // Share the file
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(Intent.createChooser(intent, "Export BMI Data"))
    }
}