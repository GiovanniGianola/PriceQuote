package com.example.pricequote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [InvoiceEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter.DateConverter::class, Converter.SizeConverter::class, Converter.CustomOptionConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun invoiceDao(): InvoiceDao?

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "quotehome.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}