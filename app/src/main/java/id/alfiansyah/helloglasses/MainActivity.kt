@file:OptIn(ExperimentalProjectedApi::class)

package id.alfiansyah.helloglasses

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.xr.projected.ProjectedContext
import androidx.xr.projected.experimental.ExperimentalProjectedApi
import id.alfiansyah.helloglasses.ui.theme.HelloGlassesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloGlassesTheme {
                ConnectionScreen()
            }
        }
    }
}

@Composable
fun ConnectionScreen() {
    val context = LocalContext.current
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.hello_ai_glasses),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            val scope = rememberCoroutineScope()
            val connectedFlow = remember(context) {
                ProjectedContext.isProjectedDeviceConnected(
                    context, scope.coroutineContext
                )
            }
            val isGlassesConnected by connectedFlow.collectAsStateWithLifecycle(initialValue = false)
            OpenOnGlassesButton(enabled = isGlassesConnected)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.status_prefix) + if (isGlassesConnected) stringResource(
                    id = R.string.status_connected
                ) else stringResource(id = R.string.status_disconnected),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun OpenOnGlassesButton(
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Button(
        enabled = enabled,
        modifier = modifier,
        onClick = {
            val options = ProjectedContext
                .createProjectedActivityOptions(context)
            val intent = Intent(context, GlassesActivity::class.java)
            context.startActivity(intent, options.toBundle())
        }
    ) {
        Text("Open on Glasses")
    }

}

@Preview(showBackground = true)
@Composable
fun ConnectionScreenPreview() {
    ConnectionScreen()
}