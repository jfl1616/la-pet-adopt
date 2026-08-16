package com.joseapps.lapetadopt.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

/** Tiny generic factory so ViewModels can take plain-constructor dependencies without Hilt. */
class ViewModelFactory<T : ViewModel>(private val create: () -> T) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <U : ViewModel> create(modelClass: Class<U>, extras: CreationExtras): U =
        create() as U
}
