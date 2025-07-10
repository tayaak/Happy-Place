package com.example.happyplaceapp

import androidx.room.*
import com.example.happyplaceapp.data.Place
import kotlinx.coroutines.flow.Flow

//Dao bedeutet Datat Acces OBjekt. Hier wird definiert wie ich mit meiner Datenbank/datenklase place arbeite
@Dao
interface PlaceDao {
    //Ort wird hinzugefüght, falls dieser mit der ID schon exisiert wird die ersetzt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)
//Liste aller gespeicherten orte
    @Query("SELECT * FROM places")
    fun getAll(): Flow<List<Place>>
    abstract fun insertPlace(place: Any)
    abstract fun getAllPlaces(): Any
}