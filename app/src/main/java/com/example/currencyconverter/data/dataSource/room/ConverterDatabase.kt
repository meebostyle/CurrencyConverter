package com.example.currencyconverter.data.dataSource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.converter.Converters
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [AccountDbo::class, TransactionDbo::class], version = 1)
@TypeConverters(Converters::class)
abstract class ConverterDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    companion object  {
        private var _db: ConverterDatabase? = null
        val db get() = _db!!


        fun create(context: Context){
            if (_db == null){
                _db = Room.databaseBuilder(
                    context,
                    ConverterDatabase::class.java, "accounts"
                ).addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = _db?.accountDao()
                            if (dao?.getCount() == 0) {
                                dao.insertAll(
                                    AccountDbo("RUB", 75000.0)
                                )
                            }
                        }
                    }
                }).build()
            }
        }

    }
}