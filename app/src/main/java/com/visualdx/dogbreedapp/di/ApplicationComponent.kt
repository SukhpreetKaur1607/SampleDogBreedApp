package com.visualdx.dogbreedapp.di

import com.visualdx.dogbreedapp.ui.view.MainActivity
import com.visualdx.dogbreedapp.ui.view.MainFragment
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
}