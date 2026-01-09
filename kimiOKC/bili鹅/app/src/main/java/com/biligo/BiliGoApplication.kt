package com.biligo

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * bili鹅应用入口
 */
@HiltAndroidApp
class BiliGoApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化日志
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // 初始化全局异常处理器
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception in thread ${thread.name}")
        }
        
        Timber.d("BiliGoApplication created")
    }
    
    companion object {
        // DataStore
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        
        // 应用实例
        lateinit var instance: BiliGoApplication
            private set
    }
    
    init {
        instance = this
    }
}