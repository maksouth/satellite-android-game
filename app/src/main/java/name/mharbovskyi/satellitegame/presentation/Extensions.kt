package name.mharbovskyi.satellitegame.presentation

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<ViewState<T>>.observeBy(
    owner: LifecycleOwner,
    success: (T) -> Unit,
    failure: (Int) -> Unit,
    loading: () -> Unit
) {
    observe(owner, Observer {
        when(it) {
            is ViewState.Success -> success(it.data)
            is ViewState.Failure -> failure(it.resId)
            is ViewState.Loading -> loading()
        }
    })
}