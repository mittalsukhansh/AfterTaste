package com.aftertaste.data.local.entity

/**
 * Data model for Google User Account & Cloud Sync State.
 */
data class AccountUser(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val isSignedIn: Boolean = false,
    val lastSyncedAt: Long = System.currentTimeMillis()
)
