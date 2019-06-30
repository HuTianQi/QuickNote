package tech.huqi.quicknote.ui.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.adapter.MainPageAdapter;
import tech.huqi.quicknote.db.NoteDatabaseHelper;
import tech.huqi.quicknote.entity.Note;


/**
 * Created by hzhuqi on 2019/4/22
 */
public class SearchActivity extends BaseActivity {
    private RecyclerView mRvSearchResult;
    private View mNoContentView;
    private SearchView mSearchView;
    protected MainPageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_search);
        initView();
    }

    private void initView() {
        mRvSearchResult = findViewById(R.id.rv_search_items_result);
        mRvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mNoContentView = findViewById(R.id.no_content_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
        initSearchView(menu);
        return true;
    }

    private void initSearchView(Menu menu) {
        mSearchView = (SearchView) menu.findItem(R.id.menu_search_view).getActionView();
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAndShowResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String pattern) {
                return false;
            }
        });
    }

    private void searchAndShowResult(String query) {
        List<Note> notes = NoteDatabaseHelper.getInstance().getNotesByPattern(query);
        if (notes == null || notes.size() == 0) {
            mRvSearchResult.setVisibility(View.INVISIBLE);
            mNoContentView.setVisibility(View.VISIBLE);
        } else {
            mNoContentView.setVisibility(View.INVISIBLE);
            mRvSearchResult.setVisibility(View.VISIBLE);
            mAdapter = new MainPageAdapter(this, notes);
            mAdapter.setIsGridMode(false);
            mRvSearchResult.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
