package info.fekri.dunibazaar.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import info.fekri.dunibazaar.model.data.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}