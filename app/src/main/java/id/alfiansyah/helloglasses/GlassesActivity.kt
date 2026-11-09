package id.alfiansyah.helloglasses

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.xr.glimmer.Button
import androidx.xr.glimmer.GlimmerTheme
import androidx.xr.glimmer.Text
import androidx.xr.glimmer.googlefonts.createGoogleSansFlexTypography
import androidx.xr.projected.ProjectedActivityCompat
import androidx.xr.projected.experimental.ExperimentalProjectedApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val CAMERA_REQUEST = 1

class GlassesActivity : ComponentActivity() {

    private var cameraStatus by mutableStateOf("not asked")
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComposeUiFlags.isInitialFocusOnFocusableAvailable = true
        setContent {
            GlimmerTheme(
                typography = createGoogleSansFlexTypography()
            ) {
                CameraScreen(cameraStatus, ::askForCamera)
            }
        }
    }

    @OptIn(ExperimentalProjectedApi::class)
    private fun askForCamera() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                ProjectedActivityCompat.requestPermissions(
                    this@GlassesActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST
                )
            } catch (e: IllegalStateException) {
                cameraStatus = "service unavailable"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode != CAMERA_REQUEST) return
        val index = permissions.indexOf(Manifest.permission.CAMERA)
        val granted = grantResults.getOrNull(index) == PERMISSION_GRANTED
        cameraStatus = if (granted) "allowed" else "denied"
    }
}

@Composable
fun CameraScreen(status: String, onAsk: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Camera: $status"
        )
        Button(
            onClick = onAsk
        ) {
            Text(
                text = "Ask for Camera"
            )
        }
    }

}