package name.mharbovskyi.satellitegame.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface BaseView {
    fun showLoading()
    fun hideLoading()
}

interface BasePresenter {
    fun destroy()
}

abstract class CoroutinePresenter
    : CoroutineScope, BasePresenter
{

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun destroy() {
        job.cancel()
    }
}