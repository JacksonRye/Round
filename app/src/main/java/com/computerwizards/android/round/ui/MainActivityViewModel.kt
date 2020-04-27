package com.computerwizards.android.round.ui

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.model.User
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    val user: User
) : ViewModel()
