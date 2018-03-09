package com.popularmovies.aithanasakis.popularmovies.ui.main;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.popularmovies.aithanasakis.popularmovies.R;
import com.popularmovies.aithanasakis.popularmovies.adapter.StaggeredMoviesAdapter;
import com.popularmovies.aithanasakis.popularmovies.data.LocalPreferences;
import com.popularmovies.aithanasakis.popularmovies.model.Movie;
import com.popularmovies.aithanasakis.popularmovies.ui.details.DetailsActivity;
import com.popularmovies.aithanasakis.popularmovies.viewmodel.MainActivityViewModel;
import com.thanosfisherman.mayi.Mayi;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StaggeredMoviesAdapter.MovieDBResultsAdapterOnClickHandler {

    private static final String SORT_BY_POPULARITY = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    @BindView(R.id.rv_results)
    public RecyclerView rvMovies;
    @BindView(R.id.todo_list_empty_view)
    public LinearLayout emptyView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;
    NetworkBroadcastReceiver mNetworkReceiver;
    IntentFilter mNetworkIntentFilter;
    private MainActivityViewModel viewModel;
    private StaggeredMoviesAdapter mMoviesAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.v("Activity Started!");

        //check permissions for android M and above
        Mayi.withActivity(this)
                .withPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_CONTACTS)
                .onRationale(this::permissionRationaleMulti)
                .onResult(this::permissionResultMulti)
                .check();
        //recyclerview
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        rvMovies.setLayoutManager(mStaggeredGridLayoutManager);
        mMoviesAdapter = new StaggeredMoviesAdapter(this);
        rvMovies.setAdapter(mMoviesAdapter);

        //navigation drawer and actionbar settings
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // initialize observe on viewmodel livedata list
        viewModel.getItemsListObservable().observe(MainActivity.this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> myMovieItemsList) {
                if (myMovieItemsList.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mMoviesAdapter.setMonieRvResults(myMovieItemsList);
                runLayoutAnimation(rvMovies);
            }
        });

        //setup eplicit broadcast receiver
        mNetworkIntentFilter = new IntentFilter();
        mNetworkReceiver = new NetworkBroadcastReceiver();
        mNetworkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForInternet();
        callForData(LocalPreferences.getSortParameter(this));
    }

    // RecyclerView new items animation
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.rv_layout_animation);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    // Mayi library for permissions results
    private void permissionResultMulti(PermissionBean[] permissions) {
        //Toast.makeText(MainActivity.this, "MULTI PERMISSION RESULT " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show();
    }
    // Mayi library for permissions results
    private void permissionRationaleMulti(PermissionBean[] permissions, PermissionToken token) {
      //  Toast.makeText(MainActivity.this, "Rationales for Multiple Permissions " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show();
        token.continuePermissionRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sort_by_popularity:
                callForData(SORT_BY_POPULARITY);
                break;
            case R.id.sort_by_rating:
                callForData(SORT_BY_RATING);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkForInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        viewModel.setInternetState(netInfo != null && netInfo.isConnectedOrConnecting());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Temporarily link the navigation drawer with the options menu
        onOptionsItemSelected(item);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(Movie selectedMovieItem) {
        Context context = this;
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("item", selectedMovieItem);
        startActivity(intent);
    }

    //initialize a call for data from the viewmodel
    private void callForData(String sortParameter) {
        viewModel.getMoviesItemsList(sortParameter);
        LocalPreferences.setSortParameter(sortParameter, this);
    }

    private class NetworkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkForInternet();
        }
    }
}
