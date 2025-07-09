package com.example.happyplaceapp

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.viewinterop.AndroidView
    import org.osmdroid.config.Configuration
    import org.osmdroid.views.MapView
    import org.osmdroid.tileprovider.tilesource.TileSourceFactory

//Diese KLasse soll die Karte anzeigen
    class MapActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // OSMDroid-Konfiguration (wichtig: Kontext setzen)
            Configuration.getInstance().load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))
//Hier startete die UI
            setContent {
                AndroidView( //
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        MapView(context).apply { //erzeugt und konfiguriert die Karte
                            setTileSource(TileSourceFactory.MAPNIK) // legt das aussehen der Katze fest -MAPNIK ist der Standart OSM Stil
                            setMultiTouchControls(true) // Zoom und Scroll mit zwei fingern werden erlaubt
                            controller.setZoom(10.0) // setzt wie der Anfangszoom eingestellt ist
                            controller.setCenter(org.osmdroid.util.GeoPoint(50.9375, 6.9603)) // setzt den ANfangsstandort auf köln

                        }
                    }
                )
            }
        }
    }
