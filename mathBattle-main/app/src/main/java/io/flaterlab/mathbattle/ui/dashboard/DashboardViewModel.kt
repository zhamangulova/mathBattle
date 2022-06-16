package io.flaterlab.mathbattle.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.flaterlab.mathbattle.models.User

class DashboardViewModel : ViewModel() {
    var users: ArrayList<User> = arrayListOf()
}