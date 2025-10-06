package app

import android.app.Application
import android.util.Log
import app.util.ImageLoader
import di.appModule
import di.dataModule
import di.dataSourceModule
import di.domainModule
import di.mapperModule
import di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageLoader.appContext = applicationContext
        startKoin {
            androidContext(this@MyApplication)
            val moduleList = listOf(
                appModule, dataModule, domainModule, networkModule,
                dataSourceModule, mapperModule
            )
            Log.d("MyApplication", "Koin modules: $moduleList")
            modules(moduleList)
        }
    }
}