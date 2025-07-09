package com.example.happyplaceapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.content.FileProvider
import java.io.File
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun AddPlace() { //states speichern Name,Beschreibung;Pfad zzum Bild, mutableStateOf sorgt dafür, dass alles was sich ändert dirket auch inder UI verändert wird
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) } //der state an dem ein Kamera bild gespeichert wird

    val context = LocalContext.current //Manche Funktionen brauchen immer den aktuellen KOntext z:b. ImageRequest, durch LOcal.Context kann drauf zugegriffen werden

    //der galarie Launcher kann in der Galerie App ein Bild auswählen und dieses dem Code zurückgeben
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()// öffnet die Galerie und wählt aus
    ) { uri: Uri? -> //uri ist der Rückgabewert
        imageUri = uri // hier wird die auswahl gespeichtert
    }

    //Bilder die geschosssen werden werden sie als Uri in dem state imageUri gespeichert
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraImageUri
        }
    }
    // beim Öffnen der kamera wird schon ein konkreter speicherort verlangt,
    fun createImageFile(): Uri {
        val imageFile = File.createTempFile("happy_place_", ".jpg", context.cacheDir)//createTempFile erzeugt diese Datei im cache-Order von meiner App
        return FileProvider.getUriForFile( //erstellt die Uri dazu, das weiter geben geht nur über den file provider
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }


//Layout der UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Dein neuer lieblings Ort", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = title, //was im textfeld steht
            onValueChange = { title = it },//das es sich im Textfeld beim schriebn verändert
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()//nimmt ganze breite ein
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            modifier = Modifier.fillMaxWidth()
        )
//der Galarielauncher der oben schon definiert wurde kann jetzt hier durch den Buttonb aufgerufen werden
        Button(
            onClick = { galleryLauncher.launch("image/*") },//galerie öffnet sich
            modifier = Modifier.align(Alignment.CenterHorizontally) //Button ist zenteriert
        ) {
            Text("Foto aus deiner Galerie auswählen")

        }
        Button( // Button für ein Foto mit der Kamera auswählen
            onClick = {
                val uri = createImageFile()
                cameraImageUri = uri
                cameraLauncher.launch(uri)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Foto mit deiner Kamera aufnehmen")
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

        Spacer(modifier = Modifier.weight(1f)) //Flex abstand

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
