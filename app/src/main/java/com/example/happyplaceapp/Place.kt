package com.example.happyplaceapp.data
//datenklasse zur räprentation der Lieblingsorte
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places") //heißt places
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //zugeschriebene ID
    val title: String,
    val description: String,
    val imageUri: String,
    val latitude: Double?,
    val longitude: Double?
)
