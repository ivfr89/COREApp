package com.developer.ivan.coreapp.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

//val Fragment.context: Context
//    get() {
//        return activity!!.applicationContext
//    }

fun ViewGroup.inflateView(@LayoutRes layout: Int, attachToRoot: Boolean=false) =
    LayoutInflater.from(this.context).inflate(layout,this,attachToRoot)

@OptIn(ExperimentalCoroutinesApi::class)
fun SearchView.writeTextFlow() = callbackFlow<String?> {
    setOnQueryTextListener(object: SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query == null || query.isEmpty()) return false

            offer(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean  = false

    })
    awaitClose {
        setOnQueryTextListener(null)
    }
}

fun Fragment.registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?=null){
    (activity)?.registerReceiver(receiver,filter)
}