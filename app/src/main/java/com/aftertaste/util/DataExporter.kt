package com.aftertaste.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.WishlistCafe
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataExporter {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    fun escapeCsvField(value: String?): String {
        if (value.isNullOrEmpty()) return ""
        val escaped = value.replace("\"", "\"\"")
        return if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"")) {
            "\"$escaped\""
        } else {
            escaped
        }
    }

    fun exportVisitsToCsv(visits: List<CafeVisitWithDetails>): String {
        val sb = StringBuilder()
        sb.append("VisitId,CafeName,Location,VisitDate,OverallRating,TasteRating,AmbienceRating,SeatingRating,WifiRating,ServiceRating,PricePaid,WouldReturn,NoiseLevel,PowerOutlets,OrderedItems,Tags,Notes\n")

        for (details in visits) {
            val v = details.visit
            val itemsStr = details.items.joinToString(";") { "${it.itemName} ($${it.itemPrice ?: 0.0})" }
            val tagsStr = details.tags.joinToString(";") { it.tagName }
            val formattedDate = dateFormatter.format(Date(v.visitDate))

            sb.append(v.id).append(",")
            sb.append(escapeCsvField(v.cafeName)).append(",")
            sb.append(escapeCsvField(v.location)).append(",")
            sb.append(escapeCsvField(formattedDate)).append(",")
            sb.append(v.overallRating).append(",")
            sb.append(v.tasteRating ?: "").append(",")
            sb.append(v.ambienceRating ?: "").append(",")
            sb.append(v.seatingRating ?: "").append(",")
            sb.append(v.wifiRating ?: "").append(",")
            sb.append(v.serviceRating ?: "").append(",")
            sb.append(v.pricePaid ?: "").append(",")
            sb.append(v.wouldReturn).append(",")
            sb.append(escapeCsvField(v.noiseLevel)).append(",")
            sb.append(v.powerOutlets ?: "").append(",")
            sb.append(escapeCsvField(itemsStr)).append(",")
            sb.append(escapeCsvField(tagsStr)).append(",")
            sb.append(escapeCsvField(v.notes))
            sb.append("\n")
        }
        return sb.toString()
    }

    fun exportWishlistToCsv(wishlist: List<WishlistCafe>): String {
        val sb = StringBuilder()
        sb.append("WishlistId,CafeName,Location,Notes,AddedDate\n")

        for (w in wishlist) {
            val formattedDate = dateFormatter.format(Date(w.addedDate))
            sb.append(w.id).append(",")
            sb.append(escapeCsvField(w.cafeName)).append(",")
            sb.append(escapeCsvField(w.location)).append(",")
            sb.append(escapeCsvField(w.notes)).append(",")
            sb.append(escapeCsvField(formattedDate))
            sb.append("\n")
        }
        return sb.toString()
    }

    fun generateFullCsvExport(visits: List<CafeVisitWithDetails>, wishlist: List<WishlistCafe>): String {
        val sb = StringBuilder()
        sb.append("=== AFTERTASTE CAFE VISITS ===\n")
        sb.append(exportVisitsToCsv(visits))
        sb.append("\n=== AFTERTASTE WISHLIST CAFES ===\n")
        sb.append(exportWishlistToCsv(wishlist))
        return sb.toString()
    }

    fun writeStringToUri(context: Context, uri: Uri, content: String): Boolean {
        return try {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
            outputStream?.use { stream ->
                stream.write(content.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun createShareIntent(context: Context, text: String, title: String): Intent {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_TITLE, title)
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, title)
    }
}
