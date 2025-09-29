package app

import android.app.Application
import android.util.Log
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
        startKoin {
            androidContext(this@MyApplication)
            val moduleList = listOf(
                appModule, dataModule, domainModule, networkModule,
                dataSourceModule, mapperModule
            )
            Log.d("MyApplication", "Koin modules: $moduleList")
            moduleList.forEachIndexed { index, module ->
                if (module == null) {
                    Log.e("MyApplication", "Koin module at index $index is NULL!")
                }
            }
            modules(moduleList)
        }
    }
}