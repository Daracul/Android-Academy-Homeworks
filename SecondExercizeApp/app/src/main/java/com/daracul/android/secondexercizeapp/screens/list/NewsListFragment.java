package com.daracul.android.secondexercizeapp.screens.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.screens.about.AboutActivity;
import com.daracul.android.secondexercizeapp.screens.list.mvp.ListPresenter;
import com.daracul.android.secondexercizeapp.screens.list.mvp.ListView;
import com.daracul.android.secondexercizeapp.utils.State;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.daracul.android.secondexercizeapp.utils.VerticalSpaceItemDecoration;
import java.util.List;


public class NewsListFragment extends MvpAppCompatFragment implements ListView {
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
    private DetailFragmentListener detailFragmentListener;

    @InjectPresenter
    ListPresenter listPresenter;

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
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        setHasOptionsMenu(true);
        setupUI(view);
        setupUX();
        return view;
    }

    private void setupUX() {
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPresenter.loadNews();
            }
        });

        recyclerScreen.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listPresenter.loadNews();
            }
        });

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
                listPresenter.loadNews();
            }
        });
    }

    private void setupRecyclerView(View view) {
        list = view.findViewById(R.id.recycler);
        adapter = new NewsRecyclerAdapter(view.getContext(), clickListener);
        list.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager;
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

    public void showState(@NonNull State state) {
        switch (state) {
            case HasData:
                recyclerScreen.setRefreshing(false);
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

        spinner.setSelection(listPresenter.getSpinnerPosition());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listPresenter.setSpinnerPosition(position);
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

    @Override
    public void subcribeDataFromDb(@NonNull List<News> newsList) {
        adapter.swapData(newsList);
    }
}
