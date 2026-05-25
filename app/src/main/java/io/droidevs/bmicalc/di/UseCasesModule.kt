package io.droidevs.bmicalc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.bmicalc.domain.repository.BmiRepository
import io.droidevs.bmicalc.domain.repository.BmiGoalRecordRepository
import io.droidevs.bmicalc.domain.repository.FavoriteBmiRepository
import io.droidevs.bmicalc.domain.preference.BmiGoalPreference
import io.droidevs.bmicalc.domain.preference.LastBmiScorePreference
import io.droidevs.bmicalc.domain.preference.ThemePreference
import io.droidevs.bmicalc.domain.preference.UnitSystemPreference
import io.droidevs.bmicalc.domain.usecases.AddBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.BmiUseCases
import io.droidevs.bmicalc.domain.usecases.EvaluateBmiGoalProgressUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.DeleteBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.GetBmiRecordsUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.GetBmiRecordByIdUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.UpdateBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.GetBmiScoreUseCase
import io.droidevs.bmicalc.domain.usecases.GetThemeUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.AddToFavoritesUseCase
import io.droidevs.bmicalc.domain.usecases.bmi.favorite.DeleteFavoredBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.goal.AbandonGoalUseCase
import io.droidevs.bmicalc.domain.usecases.goal.CompleteGoalUseCase
import io.droidevs.bmicalc.domain.usecases.goal.GetActiveBmiGoalUseCase
import io.droidevs.bmicalc.domain.usecases.goal.SetActiveBmiGoalUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.GetUnitSystemUseCase
import io.droidevs.bmicalc.domain.usecases.PreferenceUseCases
import io.droidevs.bmicalc.domain.usecases.UpdateLastSubmittedScoreUseCase
import io.droidevs.bmicalc.domain.usecases.UpdateThemeUseCase
import io.droidevs.bmicalc.domain.usecases.SelectBmiRecordUseCase
import io.droidevs.bmicalc.domain.usecases.goal.SelectBmiGoalRecordUseCase
import io.droidevs.bmicalc.domain.usecases.SelectFavoredBmiUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.UpdateUnitSystemUseCase
import io.droidevs.bmicalc.domain.usecases.unitsystem.UnitSystemUseCases
import io.droidevs.bmicalc.domain.usecases.validators.ValidateBmiInputUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {


    @Provides
    @Singleton
    fun provideBmiUseCases(
        getByIdUseCase: GetBmiRecordByIdUseCase,
        getUseCase : GetBmiRecordsUseCase,
        addUseCase: AddBmiRecordUseCase,
        deleteUseCase: DeleteBmiRecordUseCase
    ) : BmiUseCases {
        return BmiUseCases(
            getById = getByIdUseCase,
            get = getUseCase,
            add = addUseCase,
            delete = deleteUseCase
        )
    }

    @Provides
    @Singleton
    fun provideGetBmiRecordByIdUseCase(repository: BmiRepository) : GetBmiRecordByIdUseCase {
        return GetBmiRecordByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBmiRecordsUseCase(repository: BmiRepository): GetBmiRecordsUseCase {
        return GetBmiRecordsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddBmiRecordUseCase(repository: BmiRepository): AddBmiRecordUseCase {
        return AddBmiRecordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateBmiRecordUseCase(repository: BmiRepository): UpdateBmiRecordUseCase {
        return UpdateBmiRecordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteBmiRecordUseCase(repository: BmiRepository): DeleteBmiRecordUseCase {
        return DeleteBmiRecordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideValidateBmiInputUseCase(): ValidateBmiInputUseCase {
        return ValidateBmiInputUseCase()
    }

    @Provides
    @Singleton
    fun provideUnitSystemUseCases(
        getUnitSystemUseCase: GetUnitSystemUseCase,
        updateUnitSystemUseCase: UpdateUnitSystemUseCase
    ) : UnitSystemUseCases {
        return UnitSystemUseCases(
            get = getUnitSystemUseCase,
            update = updateUnitSystemUseCase
        )
    }

    @Provides
    @Singleton
    fun provideAddToFavoritesUseCase(repository: FavoriteBmiRepository): AddToFavoritesUseCase {
        return AddToFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteFavoredBmiRecordUseCase(repository: FavoriteBmiRepository): DeleteFavoredBmiRecordUseCase {
        return DeleteFavoredBmiRecordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetActiveBmiGoalUseCase(preference: BmiGoalPreference): GetActiveBmiGoalUseCase {
        return GetActiveBmiGoalUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideSetActiveBmiGoalUseCase(preference: BmiGoalPreference): SetActiveBmiGoalUseCase {
        return SetActiveBmiGoalUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideCompleteGoalUseCase(repository: BmiGoalRecordRepository): CompleteGoalUseCase {
        return CompleteGoalUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAbandonGoalUseCase(repository: BmiGoalRecordRepository): AbandonGoalUseCase {
        return AbandonGoalUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideEvaluateBmiGoalProgressUseCase(): EvaluateBmiGoalProgressUseCase {
        return EvaluateBmiGoalProgressUseCase()
    }

    @Provides
    @Singleton
    fun provideSelectBmiRecordUseCase(): SelectBmiRecordUseCase {
        return SelectBmiRecordUseCase()
    }

    @Provides
    @Singleton
    fun provideSelectBmiGoalRecordUseCase(): SelectBmiGoalRecordUseCase {
        return SelectBmiGoalRecordUseCase()
    }

    @Provides
    @Singleton
    fun provideSelectFavoredBmiUseCase(): SelectFavoredBmiUseCase {
        return SelectFavoredBmiUseCase()
    }

    // preference related use cases

    @Provides
    @Singleton
    fun providePreferenceUseCases(
        getLastStoreUseCase: GetBmiScoreUseCase,
        updateLastScoreUseCase: UpdateLastSubmittedScoreUseCase,
        getUnitSystemUseCase: GetUnitSystemUseCase,
        updateUnitSystemUseCase: UpdateUnitSystemUseCase,
        getThemeUseCase: GetThemeUseCase,
        updateThemeUseCase: UpdateThemeUseCase
    ) : PreferenceUseCases {
        return PreferenceUseCases(
            getTheme = getThemeUseCase,
            updateTheme = updateThemeUseCase,
            getUnitSystem = getUnitSystemUseCase,
            updateUnitSystem = updateUnitSystemUseCase,
            getLastScore = getLastStoreUseCase,
            updateLastScore = updateLastScoreUseCase
        )
    }
    @Provides
    @Singleton
    fun provideGetLastScoreUseCase(preference : LastBmiScorePreference) : GetBmiScoreUseCase {
        return GetBmiScoreUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideUpdateLastScoreUseCase(preference: LastBmiScorePreference): UpdateLastSubmittedScoreUseCase {
        return UpdateLastSubmittedScoreUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideGetUnitSystemUseCase(preference : UnitSystemPreference) : GetUnitSystemUseCase {
        return GetUnitSystemUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideUpdateUnitSystemUseCase(preference: UnitSystemPreference) : UpdateUnitSystemUseCase {
        return UpdateUnitSystemUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideGetThemeUseCase(preference: ThemePreference) : GetThemeUseCase {
        return GetThemeUseCase(preference)
    }

    @Provides
    @Singleton
    fun provideUpdateThemeUseCase(preference: ThemePreference) : UpdateThemeUseCase {
        return UpdateThemeUseCase(preference)
    }

}
