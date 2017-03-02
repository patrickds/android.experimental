package com.github.patrickds.androidexperimental.home.injection

import com.github.patrickds.androidexperimental.home.HomeContract
import com.github.patrickds.androidexperimental.home.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class HomeModule(private val view: HomeContract.View) {

    @Provides
    @HomeScope
    fun view(): HomeContract.View{
        return view
    }

    @Provides
    @HomeScope
    fun presenter(presenter: HomePresenter): HomeContract.Presenter{
        return presenter
    }
}