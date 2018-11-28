package name.mharbovskyi.satellitegame.presentation

import android.support.annotation.StringRes

sealed class ViewState<T> {

    class Loading<T> : ViewState<T>()
    data class Failure<T>(@StringRes val resId: Int): ViewState<T>()
    data class Success<T>(val data: T): ViewState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failure(@StringRes resId: Int) = Failure<T>(resId)
    }


}
