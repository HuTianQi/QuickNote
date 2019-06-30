package tech.huqi.quicknote.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import java.util.List;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.db.NoteDatabaseHelper;
import tech.huqi.quicknote.entity.Note;
import tech.huqi.quicknote.util.CommonUtil;
import tech.huqi.quicknote.util.DialogFactory;
import tech.huqi.quicknote.view.ItemListDialogFragment;


/**
 * Created by hzhuqi on 2019/4/6
 * 主页Fragment，即默认显示的所有笔记的视图页面
 */
public class AllNoteFragment extends BaseNoteListFragment implements ItemListDialogFragment.Listener {
    private static final String TAG = "QuickNote.AllNoteFragment";
    private static final int BOTTOM_SHEET_ITEM_RENAME = 0;
    private static final int BOTTOM_SHEET_ITEM_REMOVE = 1;
    private static final int BOTTOM_SHEET_ITEM_ENCRYPT = 2;
    private int mLongClickNoteId;

    @Override
    View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    List<Note> onGetNotes() {
        mNotes = NoteDatabaseHelper.getInstance().getAllActiveNotes();
        return mNotes;
    }

    @Override
    void onNoteItemLongClick(View v, int position) {
        mLongClickNoteId = position;
        createAndShowDialog();
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
    }

    @Override
    void onRefreshData() {
        mNotes = NoteDatabaseHelper.getInstance().getAllActiveNotes();
        mAdapter.refreshData(mNotes);
    }

    private void createAndShowDialog() {
        int[] icons = {R.drawable.ic_rename_black_24dp, R.drawable.ic_delete_black_24dp, R.drawable.ic_encrypt_black_24dp};
        String[] texts = {getString(R.string.rename), getString(R.string.remove), getString(R.string.encrypt)};
        ItemListDialogFragment.newInstance(icons, texts).setListener(this).show(getActivity().getSupportFragmentManager(), TAG);
    }

    @Override
    public void onItemClicked(int position) {
        switch (position) {
            case BOTTOM_SHEET_ITEM_RENAME: {
                showRenameDialog();
            }
            break;
            case BOTTOM_SHEET_ITEM_REMOVE: {
                removeNote();
            }
            break;
            case BOTTOM_SHEET_ITEM_ENCRYPT: {
                CommonUtil.showToastOnUiThread(R.string.purchase_vip_tip);
            }
            break;
        }
    }

    private void removeNote() {
        NoteDatabaseHelper.getInstance().remove(mNotes.get(mLongClickNoteId).getId());
        mNotes = NoteDatabaseHelper.getInstance().getAllActiveNotes();
        mAdapter.refreshData(mNotes);
    }

    private void showRenameDialog() {
        final EditText popContentView = (EditText) View.inflate(getContext(), R.layout.view_pop_content_input, null);
        DialogFactory.showInputDialog(getActivity(), QuickNote.getString(R.string.rename), popContentView, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = popContentView.getText().toString();
                if (!TextUtils.isEmpty(newTitle)) {
                    Note current = mNotes.get(mLongClickNoteId);
                    current.setTitle(newTitle);
                    NoteDatabaseHelper.getInstance().update(current);
                }
                mAdapter.refreshData(mNotes);
            }
        });
    }
}
