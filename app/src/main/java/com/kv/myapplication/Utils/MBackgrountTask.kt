package com.kv.myapplication.Utils

import kotlinx.coroutines.*

fun <R> CoroutineScope.executeAsyncTask(
onPreExecute: () -> Unit,
doInBackground: () -> R,
onPostExecute: (R) -> Unit
) = launch {
    onPreExecute()
    val result = withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
        doInBackground()
    }
    onPostExecute(result)
}