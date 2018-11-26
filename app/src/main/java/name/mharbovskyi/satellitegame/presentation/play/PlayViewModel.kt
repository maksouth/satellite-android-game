package name.mharbovskyi.satellitegame.presentation.play

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.intervalTimer
import name.mharbovskyi.satellitegame.domain.scaledTimer
import name.mharbovskyi.satellitegame.presentation.BaseViewModel
import name.mharbovskyi.satellitegame.presentation.ViewState

class PlayViewModel(
    private var satellite: ObjectState,
    private val planet: Planet,
    private val objectStateTransformer: (ObjectState, Long) -> ObjectState,
    private val locationScaler: (Location) -> Location
): BaseViewModel() {

    val satellitePosition: MutableLiveData<ViewState<Location>> = MutableLiveData()
    private var satelliteLocationJob: Job? = null
    private val timer = scaledTimer(100.0, intervalTimer())

    fun start() = launch {
        satelliteLocationJob = launch {
            for (time in timer) {
                val scaledLocation = locationScaler(satellite.location)
                satellitePosition.postValue(ViewState.success(scaledLocation))
                satellite = objectStateTransformer(satellite, time)
                Log.d("PlayViewModel", "new state: $satellite")
            }
        }
    }

    fun stop() {
        satelliteLocationJob?.cancel()
    }
}

class PlayViewModelFactory(
    private val satellite: ObjectState,
    private val planet: Planet,
//    private val timer: ReceiveChannel<Long>,
    private val objectStateTransformer: (ObjectState, Long) -> ObjectState,
    private val locationScaler: (Location) -> Location
): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PlayViewModel(satellite, planet, objectStateTransformer, locationScaler) as T
}

