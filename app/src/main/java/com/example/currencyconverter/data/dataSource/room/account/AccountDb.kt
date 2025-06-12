package com.example.currencyconverter.data.dataSource.room.account

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [AccountDbo::class], version = 1)
abstract class AccountDb: RoomDatabase(){
    abstract val accountDao: AccountDao


    companion object AccountDbObject {
        private var _db: AccountDb? = null
        val db get() = _db!!

        fun create(context: Context){
            if (_db == null){
                    _db = Room.databaseBuilder(
                    context,
                    AccountDb::class.java, "accounts"
                ).build()
                CoroutineScope(Dispatchers.IO).launch {
                    db.accountDao.insertAll(
                        AccountDbo("RUB", 75000.0)
                    )
                }
            }
        }
    }
}
