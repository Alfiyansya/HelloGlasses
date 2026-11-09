package id.alfiansyah.helloglasses

import android.content.Context
import androidx.xr.projected.ProjectedContext
import androidx.xr.projected.experimental.ExperimentalProjectedApi
import androidx.xr.runtime.XrDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalProjectedApi::class, ExperimentalCoroutinesApi::class)
fun glassesStatus(context: Context): Flow<String> {
    var made = 0
    return ProjectedContext
        .isProjectedDeviceConnected(context, Dispatchers.Default)
        .flatMapLatest { connected ->
            if(!connected){
                flowOf("disconnected")
            }else{
                try {
                    val glasses = ProjectedContext
                        .createProjectedDeviceContext(context)
                    made++
                    val label =
                        "context #$made, device ${glasses.deviceId}"
                    XrDevice.getCurrentDevice(glasses)
                        .getLifecycle().currentStateFlow
                        .map { state -> "$state, $label" }
                }catch (e: IllegalStateException){
                    flowOf("no context: ${e.message}")

                }
            }
        }
}