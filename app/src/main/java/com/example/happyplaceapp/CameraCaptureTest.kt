package com.example.happyplaceapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun CameraCaptureTestScreen() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        // Wenn das Foto erfolgreich aufgenommen wurde, zeigt es sich automatisch durch das State
    }

    val imageFile = remember {
        File.createTempFile("happy_place_", ".jpg", context.cacheDir).apply {
            deleteOnExit()
        }
    }

    val fileUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            imageUri = fileUri
            cameraLauncher.launch(fileUri)
        }) {
            Text("📸 Foto aufnehmen")
        }

        Spacer(modifier = Modifier.height(24.dp))

        imageUri?.let {
            AsyncImage(
                model = it,
                contentDescription = "Aufgenommenes Bild",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}
