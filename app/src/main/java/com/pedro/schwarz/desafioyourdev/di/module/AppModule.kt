package com.pedro.schwarz.desafioyourdev.di.module

import androidx.room.Room
import com.pedro.schwarz.desafioyourdev.database.AppDatabase
import com.pedro.schwarz.desafioyourdev.database.dao.MovieDAO
import com.pedro.schwarz.desafioyourdev.repository.MovieRepository
import com.pedro.schwarz.desafioyourdev.retrofit.client.MovieClient
import com.pedro.schwarz.desafioyourdev.retrofit.service.MovieService
import com.pedro.schwarz.desafioyourdev.ui.recyclerview.MoviesAdapter
import com.pedro.schwarz.desafioyourdev.ui.viewmodel.AppViewModel
import com.pedro.schwarz.desafioyourdev.ui.viewmodel.FavoriteMovieListViewModel
import com.pedro.schwarz.desafioyourdev.ui.viewmodel.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DATABASE_NAME = "desafio_your_dev"

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, DATABASE_NAME).build()
    }
}

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/movies/v2/reviews/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<MovieService> { get<Retrofit>().create(MovieService::class.java) }
    single<MovieClient> { MovieClient(get()) }
}

val daoModule = module {
    single<MovieDAO> { get<AppDatabase>().getMovieDAO() }
    single<MovieRepository> { MovieRepository(get(), get()) }
}

val uiModule = module {
    factory<MoviesAdapter> { MoviesAdapter() }
}

val viewModelModule = module {
    viewModel<MovieListViewModel> { MovieListViewModel(get()) }
    viewModel<FavoriteMovieListViewModel> { FavoriteMovieListViewModel(get()) }
    viewModel<AppViewModel> { AppViewModel() }
}