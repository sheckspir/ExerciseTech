package ru.fm4m.exercisetechnique.trainingview.ui.di

import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.CoroutineScope
import ru.fm4m.exercisetechnique.trainingdomain.usecases.GetAllTrainingsUseCase
import ru.fm4m.exercisetechnique.trainingdomain.usecases.GetAllTrainingsUseCaseImpl
import ru.fm4m.exercisetechnique.trainingdomain.usecases.GetUserIdUseCase
import ru.fm4m.exercisetechnique.trainingdomain.usecases.GetUserIdUseCaseImpl
import ru.fm4m.exercisetechnique.trainingdomain.usecases.RemoveTrainingUseCase
import ru.fm4m.exercisetechnique.trainingdomain.usecases.RemoveTrainingUseCaseImpl
import ru.fm4m.exercisetechnique.trainingview.ui.programs.AvailableProgramsFragment

@Module
class TrainingModule {

    @Provides
    fun provideRemoveTrainingUseCase(useCase: RemoveTrainingUseCaseImpl) : RemoveTrainingUseCase = useCase

    @Provides
    fun provideGetAllTrainingsUseCase(useCase: GetAllTrainingsUseCaseImpl) : GetAllTrainingsUseCase = useCase

    @Provides
    fun provideGetUserIdUseCase(useCase : GetUserIdUseCaseImpl) : GetUserIdUseCase = useCase

    @Provides
    fun provideCoroutineScope(fragment: AvailableProgramsFragment) : CoroutineScope = fragment.lifecycleScope

}

@Module
abstract class TrainingProvider {

    @ContributesAndroidInjector(modules = [TrainingModule::class])
    abstract fun provideProgramFragment() : AvailableProgramsFragment


}