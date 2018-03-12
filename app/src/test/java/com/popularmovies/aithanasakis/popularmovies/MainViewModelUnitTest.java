package com.popularmovies.aithanasakis.popularmovies;

import com.popularmovies.aithanasakis.popularmovies.viewmodel.MainActivityViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Created by 3piCerberus on 09/03/2018.
 */

public class MainViewModelUnitTest {

private MainActivityViewModel viewModel;

    @Before
    public void setUp() throws Exception {
      //  MockitoAnnotations.initMocks(this);// required for the "@Mock" annotations

        // Make presenter a mock while using mock repository and viewContract created above
        viewModel = Mockito.spy(new MainActivityViewModel());
    }

    @Test
    public void test1(){

    }


}
