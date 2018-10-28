package com.daracul.android.secondexercizeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.daracul.android.secondexercizeapp.data.Category;
import com.daracul.android.secondexercizeapp.data.ResultDTO;
import com.daracul.android.secondexercizeapp.network.DefaultResponse;
import com.daracul.android.secondexercizeapp.network.RestApi;
import com.daracul.android.secondexercizeapp.utils.Utils;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewsListActivity extends AppCompatActivity {
    private static final int SPACE_BETWEEN_CARDS_IN_DP = 4;
    private static final String CATEGORY_SPINNER_KEY = "category_key";
    private static final String LOG_TAG = NewsListActivity.class.getSimpleName();
    private RecyclerView list;
    private NewsRecyclerAdapter adapter;
    private Button btnTryAgain;
    private View viewError;
    private View viewLoading;
    private View viewNoData;
    private TextView tvError;
    private int spinnerPosition ;
    private Bundle bundle;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final NewsRecyclerAdapter.OnItemClickListener clickListener =
            new NewsRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String url) {
                    NewsDetailActivity.start(NewsListActivity.this, url);
                }
            };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        if (savedInstanceState!=null){
            bundle = savedInstanceState;
        }
        Log.d("myLogs", "position is " + spinnerPosition);
        setupUI();
        setupUX();
        loadNews(getResources().getStringArray(R.array.category_spinner)
                [spinnerPosition].toLowerCase());

    }


    private void setupUX() {

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNews(getResources().getStringArray(R.array.category_spinner)
                        [spinnerPosition].toLowerCase());
            }
        });
    }

    private void setupUI() {
        setupRecyclerView();
        findViews();
    }

    private void setupRecyclerView() {
        list = findViewById(R.id.recycler);
        adapter = new NewsRecyclerAdapter(this, clickListener);
        list.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager;
        if (Utils.isHorizontal(this)) {
            layoutManager = new GridLayoutManager(this, 2);
        } else layoutManager = new LinearLayoutManager(this);
        list.addItemDecoration(
                new VerticalSpaceItemDecoration(Utils.convertDpToPixel(SPACE_BETWEEN_CARDS_IN_DP,
                        this)));
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
    }

    private void findViews() {
        btnTryAgain = findViewById(R.id.btn_try_again);
        viewError = findViewById(R.id.lt_error);
        viewLoading = findViewById(R.id.lt_loading);
        viewNoData = findViewById(R.id.lt_no_data);
        tvError = findViewById(R.id.tv_error);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (list != null) list = null;
        if (adapter != null) adapter = null;
        compositeDisposable.clear();

    }

    public void loadNews(String category) {
        showState(State.Loading);
        final Disposable disposable = RestApi.getInstance()
                .news()
                .newsObject(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<DefaultResponse<List<ResultDTO>>>>() {
                    @Override
                    public void accept(Response<DefaultResponse<List<ResultDTO>>> response) throws Exception {
                        checkResponseAndShowState(response);
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
        if (throwable instanceof IOException) {
            showState(State.NetworkError);
            return;
        }
        showState(State.ServerError);
    }


    private void checkResponseAndShowState(@NonNull Response<DefaultResponse<List<ResultDTO>>> response) {

        if (!response.isSuccessful()) {
            showState(State.ServerError);
            return;
        }

        final DefaultResponse<List<ResultDTO>> body = response.body();
        if (body == null) {
            showState(State.HasNoData);
            return;
        }

        final List<ResultDTO> data = body.getData();
        if (data == null) {
            showState(State.HasNoData);
            return;
        }

        if (data.isEmpty()) {
            showState(State.HasNoData);
            return;
        }

        adapter.swapData(data);
        list.scrollToPosition(0);
        showState(State.HasData);
    }


    public void showState(@NonNull State state) {

        switch (state) {
            case HasData:
                viewError.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                list.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                list.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                viewError.setVisibility(View.VISIBLE);
                viewNoData.setVisibility(View.VISIBLE);
                break;
            case NetworkError:
                list.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_network));
                viewError.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                list.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_server));
                viewError.setVisibility(View.VISIBLE);
                break;
            case Loading:
                viewError.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;


            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        setupSpinner(menu);
        return true;
    }

    private void setupSpinner(Menu menu) {
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner)item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_spinner,R.layout.spinner_item_xml);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (bundle!=null){
            spinner.setSelection(bundle.getInt(CATEGORY_SPINNER_KEY,0));
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
                loadNews(getResources().getStringArray(R.array.category_spinner)[position].toLowerCase());

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
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CATEGORY_SPINNER_KEY, spinnerPosition);

    }
}
