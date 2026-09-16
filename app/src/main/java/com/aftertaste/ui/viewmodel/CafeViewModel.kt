package com.aftertaste.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aftertaste.data.local.entity.AccountUser
import com.aftertaste.data.local.entity.Cafe
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe
import com.aftertaste.repository.CafeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CafeViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _userAccount = MutableStateFlow(
        AccountUser(
            id = "google_user_01",
            name = "Coffee Explorer",
            email = "user@aftertaste.app",
            photoUrl = null,
            isSignedIn = false,
            lastSyncedAt = System.currentTimeMillis()
        )
    )
    val userAccount: StateFlow<AccountUser> = _userAccount.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val visits: StateFlow<List<CafeVisitWithDetails>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllVisits()
            } else {
                repository.searchVisits(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val cafes: StateFlow<List<Cafe>> = repository.getAllCafes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val wishlist: StateFlow<List<WishlistCafe>> = repository.getAllWishlistCafes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allTags: StateFlow<List<String>> = repository.getAllTags()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun signInWithGoogle(name: String = "Coffee Enthusiast", email: String = "coffee.lover@gmail.com") {
        _userAccount.value = AccountUser(
            id = "google_1029384",
            name = name,
            email = email,
            photoUrl = null,
            isSignedIn = true,
            lastSyncedAt = System.currentTimeMillis()
        )
    }

    fun signOut() {
        _userAccount.value = AccountUser(
            id = "",
            name = "",
            email = "",
            photoUrl = null,
            isSignedIn = false,
            lastSyncedAt = System.currentTimeMillis()
        )
    }

    fun syncCloudData() {
        _userAccount.value = _userAccount.value.copy(
            lastSyncedAt = System.currentTimeMillis()
        )
    }

    fun getVisitById(visitId: Long): Flow<CafeVisitWithDetails?> {
        return repository.getVisitById(visitId)
    }

    fun getVisitsForCafe(cafeName: String): Flow<List<CafeVisitWithDetails>> {
        return repository.getVisitsForCafe(cafeName)
    }

    fun isWishlistCafe(cafeName: String): Flow<Boolean> {
        return repository.isWishlistCafe(cafeName)
    }

    fun saveVisit(
        visit: CafeVisit,
        items: List<OrderedItem> = emptyList(),
        tags: List<Tag> = emptyList(),
        photos: List<Photo> = emptyList(),
        onComplete: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.saveVisit(visit, items, tags, photos)
            syncCloudData()
            onComplete(id)
        }
    }

    fun deleteVisit(visitId: Long) {
        viewModelScope.launch {
            repository.deleteVisit(visitId)
            syncCloudData()
        }
    }

    fun addToWishlist(cafeName: String, location: String = "", notes: String? = null) {
        viewModelScope.launch {
            repository.addToWishlist(
                WishlistCafe(
                    cafeName = cafeName,
                    location = location,
                    notes = notes
                )
            )
            syncCloudData()
        }
    }

    fun removeFromWishlist(id: Long) {
        viewModelScope.launch {
            repository.removeFromWishlist(id)
            syncCloudData()
        }
    }

    fun removeFromWishlistByName(cafeName: String) {
        viewModelScope.launch {
            repository.removeFromWishlistByName(cafeName)
            syncCloudData()
        }
    }
}
