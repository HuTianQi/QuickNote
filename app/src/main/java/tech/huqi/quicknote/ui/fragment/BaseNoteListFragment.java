package tech.huqi.quicknote.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.adapter.MainPageAdapter;
import tech.huqi.quicknote.adapter.base.BaseAdapter;
import tech.huqi.quicknote.adapter.base.OnItemClickListener;
import tech.huqi.quicknote.adapter.base.OnItemLongClickListener;
import tech.huqi.quicknote.entity.Note;
import tech.huqi.quicknote.ui.activity.MainActivity;
import tech.huqi.quicknote.ui.activity.NoteDetailActivity;
import tech.huqi.quicknote.ui.activity.SearchActivity;
import tech.huqi.quicknote.util.CommonUtil;
import tech.huqi.quicknote.util.PopupWindowFactory;

import static tech.huqi.quicknote.config.Constants.INTENT_NOTE_ITEM;

/**
 * Created by hzhuqi on 2019/4/6
 */
public abstract class BaseNoteListFragment extends Fragment {
    protected View mRootView;
    protected BaseAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected MainActivity mMainActivity;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected List<Note> mNotes;

    private boolean isGridMode = true;

    abstract View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    abstract void initData();

    abstract void initView();

    abstract void onRefreshData();

    abstract List<Note> onGetNotes();

    abstract void onNoteItemLongClick(View v, int position);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = createView(inflater, container, savedInstanceState);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mMainActivity = (MainActivity) getActivity();
        initBaseView();
        initData();
    }

    private void initBaseView() {
        mRecyclerView = mRootView.findViewById(R.id.rv_main_content);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRootView.getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainPageAdapter(getActivity(), onGetNotes());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh_main_page);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                gotoNoteDetailActivity(position);
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                onNoteItemLongClick(v, position);
            }
        });
        initView();
    }

    public void switchMode(boolean isGridMode) {
        if (isGridMode) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mRootView.getContext(), 2));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
            mRecyclerView.setAdapter(mAdapter);
        }
        ((MainPageAdapter) mAdapter).setIsGridMode(isGridMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        resumeUI();
    }

    /**
     * Fragment的hide与show不会走常规生命周期函数，通过该函数判断
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resumeUI();
        }
    }

    private void resumeUI() {
        if (!mMainActivity.getFabMenu().isShown()) {
            mMainActivity.getFabMenu().showMenu(true);
        }
        onRefreshData();
    }

    protected void gotoNoteDetailActivity(int position) {
        Note note = mNotes.get(position);
        Intent intent = new Intent(mMainActivity, NoteDetailActivity.class);
        intent.putExtra(INTENT_NOTE_ITEM, note);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search: {
                gotoSearchActivity();
            }
            break;
            case R.id.menu_sort: {
                handleSortEvent();
            }
            break;
            case R.id.action_settings: {
                mMainActivity.switchFragmentByTag(MainActivity.SETTINGS_FRAGMENT);
            }
            break;
            case R.id.switch_mode: {
                isGridMode = !isGridMode;
                if (isGridMode) {
                    CommonUtil.showToastOnUiThread("当前为网格模式");
                } else {
                    CommonUtil.showToastOnUiThread("当前为列表模式");
                }
                switchMode(isGridMode);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoSearchActivity() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    getActivity().findViewById((R.id.menu_search)),
                    getString(R.string.scene_transition_search)).toBundle();
            startActivity(new Intent(getActivity(), SearchActivity.class), options);
        } else {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }
    }

    private void handleSortEvent() {
        PopupWindowFactory.createAndShowPopupMenu(mMainActivity, getActivity().findViewById(R.id.menu_sort),
                R.menu.menu_toolbar_sort, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.sort_item_asc: {
                                Collections.sort(mNotes);
                                mAdapter.refreshData(mNotes);
                            }
                            break;
                            case R.id.sort_item_desc: {
                                Collections.sort(mNotes, Collections.reverseOrder());
                                mAdapter.refreshData(mNotes);
                            }
                            break;
                        }
                        return false;
                    }
                });
    }
}
