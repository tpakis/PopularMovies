package com.popularmovies.aithanasakis.popularmovies.dagger2;

import com.popularmovies.aithanasakis.popularmovies.network.MovieDBService;
import com.popularmovies.aithanasakis.popularmovies.repository.PopularMoviesRepository;
import com.popularmovies.aithanasakis.popularmovies.ui.details.DetailsActivity;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.DetailsActivityViewModel;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.MainActivityViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 3piCerberus on 12/03/2018.
 */

//we expose only top level depedencies to our ViewModel, and it has only one the Repository.
//The repository has other depedencies that will be provided by the RepositoryModule
@Singleton
@Component(modules = {PopularMoviesRepositoryModule.class,AppModule.class})
public interface MainActivityViewModelComponent {

    void inject(MainActivityViewModel viewModel);
    void inject(DetailsActivityViewModel viewModel);
    PopularMoviesRepository getPopularMoviesRepository();
}
