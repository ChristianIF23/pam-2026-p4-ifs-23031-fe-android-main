package org.delcom.pam_p4_ifs23031.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23031.network.games.service.IGenreMusikAppContainer
import org.delcom.pam_p4_ifs23031.network.games.service.IGenreMusikRepository
import org.delcom.pam_p4_ifs23031.network.games.service.GenreMusikAppContainer

@Module
@InstallIn(SingletonComponent::class)
object GenreMusikModule {
    @Provides
    fun provideGenreMusikContainer(): IGenreMusikAppContainer {
        return GenreMusikAppContainer()
    }

    @Provides
    fun provideGenreMusikRepository(container: IGenreMusikAppContainer): IGenreMusikRepository {
        return container.genreMusikRepository
    }
}