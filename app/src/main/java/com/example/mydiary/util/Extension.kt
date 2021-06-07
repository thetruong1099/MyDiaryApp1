package com.example.mydiary.util

import android.annotation.SuppressLint
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.mydiary.R

fun NavController.popBackStackAllInstances(destination: Int, inclusive: Boolean): Boolean {
    var popped: Boolean
    while (true) {
        popped = popBackStack(destination, inclusive)
        if (!popped) {
            break
        }
    }
    return popped
}

@SuppressLint("RestrictedApi")
fun NavController.removeElementOverlap(navController: NavController) {
    var arr = ArrayList<NavBackStackEntry>()
    var backstack = navController.backStack

    for (i in backstack) arr.add(i)

    var count = 2
    var checkDuplicate = false

    if (arr.size > 3 && arr[arr.size - 2].destination.id == R.id.calendarFragment) {
        navController.popBackStack(R.id.calendarFragment, true)
        navController.navigate(arr[arr.size - 1].destination.id)
    }

    for (i in 2 until arr.size - 1) {
        count++
        if (arr[i].destination.id == arr[arr.size - 1].destination.id) {
            navController.popBackStackAllInstances(arr[i].destination.id, true)
            checkDuplicate = true
            break
        }
    }

    if (checkDuplicate) {
        for (i in count until arr.size) {
            navController.navigate(arr[i].destination.id)
        }
    }
}
