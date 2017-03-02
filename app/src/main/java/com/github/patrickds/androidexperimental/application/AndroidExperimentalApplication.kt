package com.github.patrickds.androidexperimental.application

import android.app.Activity
import android.app.Application
import timber.log.Timber

class AndroidExperimentalApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        component = DaggerApplicationComponent.create()
    }

    companion object{

        fun get(activity: Activity) =
                activity.application as AndroidExperimentalApplication
    }
}