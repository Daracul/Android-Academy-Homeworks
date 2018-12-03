package com.daracul.android.secondexercizeapp.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.sync.DownloadingNews;
import com.daracul.android.secondexercizeapp.ui.about.AboutActivity;
import com.daracul.android.secondexercizeapp.utils.State;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.daracul.android.secondexercizeapp.utils.VerticalSpaceItemDecoration;

import java.io.IOException;
import java.util.List;



import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsListFragment extends Fragment {
    private static final int SPACE_BETWEEN_CARDS_IN_DP = 4;
    private static final String CATEGORY_SPINNER_KEY = "category_key";
    private static final String LOG_TAG = "myLogs";
    private RecyclerView list;
    private NewsRecyclerAdapter adapter;
    private Button btnTryAgain;
    private View viewError;
    private View viewLoading;
    private View viewNoData;
    private SwipeRefreshLayout recyclerScreen;
    private TextView tvError;
    private int spinnerPosition;
    private Bundle bundle;
    private Db db;
    private DetailFragmentListener detailFragmentListener;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public interface DetailFragmentListener {
        void openDetailFragment(String id);
    }

    private final NewsRecyclerAdapter.OnItemClickListener clickListener =
            new NewsRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String id) {
                    if (detailFragmentListener != null) {
                        detailFragmentListener.openDetailFragment(id);
                    }
                }
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof DetailFragmentListener) {
            detailFragmentListener = (DetailFragmentListener) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.bundle = savedInstanceState;

        }
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        setHasOptionsMenu(true);
        setupUI(view);
        setupUX();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            db = new Db();
            subcribeToDataFromDb();
        }
    }

    private void setupUX() {
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNews(getResources().getStringArray(R.array.category_spinner)
                        [spinnerPosition].toLowerCase());
            }
        });

        recyclerScreen.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(getCurrentNewsCategory(spinnerPosition));
            }
        });

        //TODO 5. Move all listeners for views here
    }

    private void setupUI(View view) {
        setupRecyclerView(view);
        setupFab(view);
        setupActionBar();
        findViews(view);
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.app_name);
        }
    }

    private void setupFab(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNews(getCurrentNewsCategory(spinnerPosition));
            }
        });
    }


    private void setupRecyclerView(View view) {
        list = view.findViewById(R.id.recycler);
        adapter = new NewsRecyclerAdapter(view.getContext(), clickListener);
        list.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager;
//        if (Utils.isHorizontal(view.getContext())) {
//            layoutManager = new GridLayoutManager(view.getContext(), 2);
//        } else
        layoutManager = new LinearLayoutManager(view.getContext());
        list.addItemDecoration(
                new VerticalSpaceItemDecoration(Utils.convertDpToPixel(SPACE_BETWEEN_CARDS_IN_DP,
                        view.getContext())));
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
    }

    private void findViews(View view) {
        btnTryAgain = view.findViewById(R.id.btn_try_again);
        viewError = view.findViewById(R.id.lt_error);
        viewLoading = view.findViewById(R.id.lt_loading);
        viewNoData = view.findViewById(R.id.lt_no_data);
        tvError = view.findViewById(R.id.tv_error);
        recyclerScreen = view.findViewById(R.id.recycler_screen);

    }

    private void subcribeToDataFromDb() {
        Disposable disposable = db.getNewsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> newsList) throws Exception {
                        adapter.swapData(newsList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        db = null;
    }

    @Override
    public void onDetach() {
        detailFragmentListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (list != null) list = null;
        if (adapter != null) adapter = null;
    }

    private void loadNews(String category) {
        showState(State.Loading);
        recyclerScreen.setRefreshing(false);
        final Disposable disposable = DownloadingNews.updateNews(db,category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        list.scrollToPosition(0);
                        showState(State.HasData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        handleError(throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void handleError(Throwable throwable) {
        Log.d("myLogs",throwable.getClass().getName() +" message: "+throwable.getMessage());
        if (throwable instanceof IOException) {
            showState(State.NetworkError);
            return;
        }
        showState(State.ServerError);
    }



    private void showState(@NonNull State state) {
        // TODO 3. Refactor this
        switch (state) {
            case HasData:
                viewError.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);
                recyclerScreen.setVisibility(View.VISIBLE);
                break;
            case HasNoData:
                recyclerScreen.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewError.setVisibility(View.VISIBLE);
                viewNoData.setVisibility(View.VISIBLE);
                break;
            case NetworkError:
                recyclerScreen.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);
                tvError.setText(getText(R.string.error_network));
                viewError.setVisibility(View.VISIBLE);
                break;
            case ServerError:
                recyclerScreen.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);
                tvError.setText(getText(R.string.error_server));
                viewError.setVisibility(View.VISIBLE);
                break;
            case Loading:
                viewError.setVisibility(View.GONE);
                recyclerScreen.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        // TODO 4. Move spinned to toolbar using CoordinatorLayout, remove it in menu
        setupSpinner(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupSpinner(Menu menu) {
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spinner.getContext(),
                R.array.category_spinner, R.layout.spinner_item_xml);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (bundle != null) {
            spinner.setSelection(bundle.getInt(CATEGORY_SPINNER_KEY, 0));
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getCurrentNewsCategory(int spinnerPosition) {
        return getResources().getStringArray(R.array.category_spinner)[spinnerPosition].toLowerCase();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CATEGORY_SPINNER_KEY, spinnerPosition);

    }
}
