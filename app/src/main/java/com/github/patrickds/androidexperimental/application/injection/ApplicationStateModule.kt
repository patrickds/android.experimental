package com.github.patrickds.androidexperimental.application.injection

import com.github.patrickds.androidexperimental.application.HomeActivityState
import dagger.Module
import dagger.Provides

@Module
class ApplicationStateModule {

    @Provides
    @ApplicationScope
    fun activityState(): HomeActivityState {
        return HomeActivityState()
    }
}