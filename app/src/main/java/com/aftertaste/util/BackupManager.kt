package com.aftertaste.util

import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe

data class BackupData(
    val version: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val visits: List<CafeVisitWithDetails>,
    val wishlist: List<WishlistCafe>
)

object BackupManager {

    private fun escapeJson(value: String?): String {
        if (value == null) return "null"
        return "\"" + value.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r") + "\""
    }

    fun createBackupJson(visits: List<CafeVisitWithDetails>, wishlist: List<WishlistCafe>): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"version\": 1,\n")
        sb.append("  \"timestamp\": ").append(System.currentTimeMillis()).append(",\n")

        sb.append("  \"visits\": [\n")
        visits.forEachIndexed { vIdx, details ->
            val v = details.visit
            sb.append("    {\n")
            sb.append("      \"id\": ").append(v.id).append(",\n")
            sb.append("      \"cafeName\": ").append(escapeJson(v.cafeName)).append(",\n")
            sb.append("      \"location\": ").append(escapeJson(v.location)).append(",\n")
            sb.append("      \"latitude\": ").append(v.latitude ?: "null").append(",\n")
            sb.append("      \"longitude\": ").append(v.longitude ?: "null").append(",\n")
            sb.append("      \"visitDate\": ").append(v.visitDate).append(",\n")
            sb.append("      \"pricePaid\": ").append(v.pricePaid ?: "null").append(",\n")
            sb.append("      \"overallRating\": ").append(v.overallRating).append(",\n")
            sb.append("      \"tasteRating\": ").append(v.tasteRating ?: "null").append(",\n")
            sb.append("      \"ambienceRating\": ").append(v.ambienceRating ?: "null").append(",\n")
            sb.append("      \"seatingRating\": ").append(v.seatingRating ?: "null").append(",\n")
            sb.append("      \"wifiRating\": ").append(v.wifiRating ?: "null").append(",\n")
            sb.append("      \"serviceRating\": ").append(v.serviceRating ?: "null").append(",\n")
            sb.append("      \"noiseLevel\": ").append(escapeJson(v.noiseLevel)).append(",\n")
            sb.append("      \"wouldReturn\": ").append(v.wouldReturn).append(",\n")
            sb.append("      \"notes\": ").append(escapeJson(v.notes)).append(",\n")
            sb.append("      \"powerOutlets\": ").append(v.powerOutlets ?: "null").append(",\n")
            sb.append("      \"crowdLevel\": ").append(escapeJson(v.crowdLevel)).append(",\n")
            sb.append("      \"wifiPassword\": ").append(escapeJson(v.wifiPassword)).append(",\n")
            sb.append("      \"seatingType\": ").append(escapeJson(v.seatingType)).append(",\n")

            sb.append("      \"items\": [\n")
            details.items.forEachIndexed { iIdx, item ->
                sb.append("        { ")
                sb.append("\"itemName\": ").append(escapeJson(item.itemName)).append(", ")
                sb.append("\"itemPrice\": ").append(item.itemPrice ?: "null").append(", ")
                sb.append("\"itemRating\": ").append(item.itemRating ?: "null")
                sb.append(" }")
                if (iIdx < details.items.size - 1) sb.append(",")
                sb.append("\n")
            }
            sb.append("      ],\n")

            sb.append("      \"tags\": [\n")
            details.tags.forEachIndexed { tIdx, tag ->
                sb.append("        ").append(escapeJson(tag.tagName))
                if (tIdx < details.tags.size - 1) sb.append(",")
                sb.append("\n")
            }
            sb.append("      ],\n")

            sb.append("      \"photos\": [\n")
            details.photos.forEachIndexed { pIdx, photo ->
                sb.append("        ").append(escapeJson(photo.uri))
                if (pIdx < details.photos.size - 1) sb.append(",")
                sb.append("\n")
            }
            sb.append("      ]\n")

            sb.append("    }")
            if (vIdx < visits.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("  ],\n")

        sb.append("  \"wishlist\": [\n")
        wishlist.forEachIndexed { wIdx, w ->
            sb.append("    {\n")
            sb.append("      \"id\": ").append(w.id).append(",\n")
            sb.append("      \"cafeName\": ").append(escapeJson(w.cafeName)).append(",\n")
            sb.append("      \"location\": ").append(escapeJson(w.location)).append(",\n")
            sb.append("      \"notes\": ").append(escapeJson(w.notes)).append(",\n")
            sb.append("      \"addedDate\": ").append(w.addedDate).append("\n")
            sb.append("    }")
            if (wIdx < wishlist.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("  ]\n")
        sb.append("}")

        return sb.toString()
    }

    fun parseBackupJson(jsonString: String): BackupData? {
        return try {
            val visits = mutableListOf<CafeVisitWithDetails>()
            val wishlist = mutableListOf<WishlistCafe>()

            val visitsBlock = jsonString.substringAfter("\"visits\"", "").substringBefore("\"wishlist\"", "")
            val wishlistBlock = jsonString.substringAfter("\"wishlist\"", "")

            fun extractObjects(block: String): List<String> {
                val objects = mutableListOf<String>()
                var depth = 0
                var startIndex = -1
                for (i in block.indices) {
                    val char = block[i]
                    if (char == '{') {
                        if (depth == 0) startIndex = i
                        depth++
                    } else if (char == '}') {
                        depth--
                        if (depth == 0 && startIndex != -1) {
                            objects.add(block.substring(startIndex, i + 1))
                            startIndex = -1
                        }
                    }
                }
                return objects
            }

            val cafeNameRegex = Regex("\"cafeName\"\\s*:\\s*\"(.*?)\"")
            val locationRegex = Regex("\"location\"\\s*:\\s*\"(.*?)\"")
            val ratingRegex = Regex("\"overallRating\"\\s*:\\s*([0-9.]+)")
            val notesRegex = Regex("\"notes\"\\s*:\\s*\"(.*?)\"")
            val dateRegex = Regex("\"visitDate\"\\s*:\\s*([0-9]+)")
            val addedDateRegex = Regex("\"addedDate\"\\s*:\\s*([0-9]+)")

            val visitBlocks = extractObjects(visitsBlock)
            for (block in visitBlocks) {
                val cafeName = cafeNameRegex.find(block)?.groupValues?.get(1) ?: continue
                val location = locationRegex.find(block)?.groupValues?.get(1) ?: ""
                val rating = ratingRegex.find(block)?.groupValues?.get(1)?.toFloatOrNull() ?: 4.0f
                val notes = notesRegex.find(block)?.groupValues?.get(1)
                val visitDate = dateRegex.find(block)?.groupValues?.get(1)?.toLongOrNull() ?: System.currentTimeMillis()

                val items = mutableListOf<OrderedItem>()
                val itemsBlock = block.substringAfter("\"items\"", "").substringBefore("\"tags\"", "")
                val itemMatches = Regex("\"itemName\"\\s*:\\s*\"(.*?)\"").findAll(itemsBlock)
                for (itemMatch in itemMatches) {
                    val name = itemMatch.groupValues[1]
                    items.add(OrderedItem(visitId = 0, itemName = name))
                }

                val tags = mutableListOf<Tag>()
                val tagBlock = block.substringAfter("\"tags\"", "").substringBefore("\"photos\"", "")
                val tagMatches = Regex("\"(.*?)\"").findAll(tagBlock).map { it.groupValues[1] }
                for (tn in tagMatches) {
                    tags.add(Tag(visitId = 0, tagName = tn))
                }

                visits.add(
                    CafeVisitWithDetails(
                        visit = CafeVisit(
                            cafeName = cafeName,
                            location = location,
                            overallRating = rating,
                            notes = notes,
                            visitDate = visitDate
                        ),
                        items = items,
                        tags = tags,
                        photos = emptyList()
                    )
                )
            }

            val wishlistBlocks = extractObjects(wishlistBlock)
            for (block in wishlistBlocks) {
                val cafeName = cafeNameRegex.find(block)?.groupValues?.get(1) ?: continue
                val location = locationRegex.find(block)?.groupValues?.get(1) ?: ""
                val notes = notesRegex.find(block)?.groupValues?.get(1)
                val addedDate = addedDateRegex.find(block)?.groupValues?.get(1)?.toLongOrNull() ?: System.currentTimeMillis()

                wishlist.add(
                    WishlistCafe(
                        cafeName = cafeName,
                        location = location,
                        notes = notes,
                        addedDate = addedDate
                    )
                )
            }

            BackupData(
                version = 1,
                timestamp = System.currentTimeMillis(),
                visits = visits,
                wishlist = wishlist
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
