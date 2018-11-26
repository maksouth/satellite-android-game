package name.mharbovskyi.satellitegame.presentation

import android.support.annotation.StringRes

sealed class ViewState<T> {

    class Loading<T> : ViewState<T>()
    data class Failure(@StringRes val resId: Int): ViewState<Int>()
    data class Success<T>(val data: T): ViewState<T>()

    companion object {
        fun loading() = Loading<Nothing>()
        fun <T> success(data: T) = Success(data)
        fun failure(@StringRes resId: Int) = Failure(resId)
    }


}
