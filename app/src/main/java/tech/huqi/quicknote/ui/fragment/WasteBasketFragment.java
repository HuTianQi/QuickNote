package tech.huqi.quicknote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.db.NoteDatabaseHelper;
import tech.huqi.quicknote.entity.Note;
import tech.huqi.quicknote.view.ItemListDialogFragment;

/**
 * Created by hzhuqi on 2019/4/6
 * 废纸篓Fragment，用来显示用户最近删除的笔记的视图
 */
public class WasteBasketFragment extends BaseNoteListFragment implements ItemListDialogFragment.Listener {
    private static final String TAG = "QuickNote.WasteBasketFragment";
    private static final int BOTTOM_SHEET_ITEM_RECOVERY = 0;
    private static final int BOTTOM_SHEET_ITEM_DELETE_FOREVER = 1;
    private int mLongClickNoteId;

    @Override
    View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    void initData() {

    }

    @Override
    List<Note> onGetNotes() {
        mNotes = NoteDatabaseHelper.getInstance().getAllWastedNotes();
        return mNotes;
    }

    @Override
    void onNoteItemLongClick(View v, int position) {
        mLongClickNoteId = position;
        createAndShowDialog();
    }

    @Override
    void initView() {
    }

    @Override
    void onRefreshData() {
        Log.d(TAG, "刷新WasteBasketFragment数据");
        mNotes = NoteDatabaseHelper.getInstance().getAllWastedNotes();
        mAdapter.refreshData(mNotes);
    }

    private void createAndShowDialog() {
        int[] icons = {R.drawable.ic_restore_black_24dp, R.drawable.ic_delete_forever_24dp};
        String[] texts = {getString(R.string.recovery), getString(R.string.delete)};
        ItemListDialogFragment.newInstance(icons, texts).setListener(this).show(getActivity().getSupportFragmentManager(), TAG);
    }

    @Override
    public void onItemClicked(int position) {
        switch (position) {
            case BOTTOM_SHEET_ITEM_RECOVERY: {
                recoveryNote();
            }
            break;
            case BOTTOM_SHEET_ITEM_DELETE_FOREVER: {
                deleteNote();
            }
            break;
        }
    }

    private void recoveryNote() {
        NoteDatabaseHelper.getInstance().recovery(mNotes.get(mLongClickNoteId).getId());
        mNotes = NoteDatabaseHelper.getInstance().getAllWastedNotes();
        mAdapter.refreshData(mNotes);
    }

    private void deleteNote() {
        NoteDatabaseHelper.getInstance().delete(mNotes.get(mLongClickNoteId).getId());
        mNotes = NoteDatabaseHelper.getInstance().getAllWastedNotes();
        mAdapter.refreshData(mNotes);
    }
}
