package com.bizmiz.moynaktour.core.di

import com.bizmiz.moynaktour.core.api.ApiClient
import com.bizmiz.moynaktour.core.helper.PlacesHelper
import com.bizmiz.moynaktour.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseStorage.getInstance().reference }
    single { ApiClient.getClient() }
    single { PlacesHelper(get(),get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}