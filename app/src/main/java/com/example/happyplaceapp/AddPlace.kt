package com.example.happyplaceapp

import android.net.Uri
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happyplaceapp.data.Place

// ------------------ aktuelle Standortanzeige ------------------

@Composable
fun CurrentLocationView(viewModel: LocationViewModel = viewModel()) {
    // Beobachte den aktuellen Standort vom ViewModel und aktualisiere die UI automatisch
    val location by viewModel.location.collectAsState()
    val context = LocalContext.current

    // Launcher für Berechtigungsanfrage (zur Laufzeit)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchLocation()
        }
    }

    // Starte die Berechtigungsabfrage beim ersten Laden
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // UI für die Standortanzeige
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Aktueller Standort:")
        if (location != null) {
            Text("Lat: ${location?.latitude}, Lng: ${location?.longitude}")
        } else {
            Text("Wird geladen...")
        }
    }
}

// ------------------ Hauptfunktion für Ort hinzufügen ------------------

@Composable
fun AddPlace() {
    // Speichert Name, Beschreibung, Pfad zum Bild
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val placeViewModel: PlaceViewModel = viewModel() // Hol das ViewModel zur Verwendung


    val context = LocalContext.current

    // Galerie öffnen und Bild auswählen
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // Kamera öffnen und Bild aufnehmen
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraImageUri
        }
    }

    // Temporäre Datei erstellen für Kamerabild
    fun createImageFile(): Uri {
        val imageFile = File.createTempFile("happy_place_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }

    // ------------------ UI-Layout ------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Dein neuer Lieblingsort", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Foto aus Galerie auswählen")
        }

        Button(
            onClick = {
                val uri = createImageFile()
                cameraImageUri = uri
                cameraLauncher.launch(uri)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Foto mit Kamera aufnehmen")
        }
        Button(
            onClick = {
                // Neues Place-Objekt mit aktuellen Werten aus den Eingabefeldern
                val place = Place(
                    title = title,                         // Titel aus Textfeld
                    description = description,             // Beschreibung aus Textfeld
                    imageUri = imageUri.toString(),        // Bild-URI als String
                    latitude = location?.latitude,
                    longitude = location?.longitude
                )

                // Speichern über ViewModel
                placeViewModel.addPlace(place)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ort speichern")
        }



        imageUri?.let { uri ->
            AsyncImage(
                model = ImageRequest.Builder(context).data(uri).crossfade(true).build(),
                contentDescription = "Ausgewähltes Bild",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }

        // Zeige den aktuellen Standort
        CurrentLocationView()

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Hier später speichern
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ort speichern")
        }
    }
}


