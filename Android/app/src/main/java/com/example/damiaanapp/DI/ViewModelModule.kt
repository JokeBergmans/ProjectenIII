package com.example.damiaanapp.DI

import com.example.damiaanapp.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//Authors: Tom Van der Weeën
val viewModelModule = module {
    viewModel { MapViewModel(get(), get()) }
    viewModel { RoutesViewModel(get()) }
    viewModel { OptionsViewModel(get()) }
    viewModel { QRScanViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}
