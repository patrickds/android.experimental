package com.github.patrickds.androidexperimental.home.injection

import com.github.patrickds.androidexperimental.application.ApplicationComponent
import com.github.patrickds.androidexperimental.home.HomeActivity
import dagger.Component

@HomeScope
@Component(modules = arrayOf(HomeModule::class),
        dependencies = arrayOf(ApplicationComponent::class))
interface HomeComponent {
    fun inject(homeActivity: HomeActivity)
}