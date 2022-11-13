package com.visualdx.dogbreedapp

import android.app.Application
import com.visualdx.dogbreedapp.di.ApplicationComponent
import com.visualdx.dogbreedapp.di.DaggerApplicationComponent

class AppApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().build()

    }
}