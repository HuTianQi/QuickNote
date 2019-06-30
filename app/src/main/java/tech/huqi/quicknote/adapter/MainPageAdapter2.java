package tech.huqi.quicknote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.entity.Note;

/**
 * Created by hzhuqi on 2019/4/7
 */
@Deprecated
public class MainPageAdapter2 extends RecyclerView.Adapter<MainPageAdapter2.MainPageViewHolder> {
    private static final String TAG = "QuickNote.MainPageAdapter2";
    private Context mContext;
    private List<Note> mNotes;
    private boolean isGridMode = true;

    public MainPageAdapter2(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    @NonNull
    @Override
    public MainPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "==============onCreateViewHolder=================" + parent);
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycle_main_page_note_item, parent, false);
        return new MainPageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainPageViewHolder viewHolder, int position) {
        Note note = mNotes.get(position);
        viewHolder.tvNoteTitle.setText(note.getTitle());
        viewHolder.tvNoteMainBody.setText(note.getContent());
        viewHolder.tvNoteDate.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
    }

    public void setIsGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
    }

    class MainPageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNoteTitle;
        private TextView tvNoteMainBody;
        private TextView tvNoteDate;
        private int defaultWidth = mContext.getResources().getDisplayMetrics().widthPixels / 2;
        private int defaultHeight = (int) mContext.getResources().getDimension(R.dimen.space_12);

        public MainPageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tv_note_title);
            tvNoteMainBody = itemView.findViewById(R.id.tv_note_main_body);
            if (isGridMode) {
                adjustBodyLayoutParams();
            }
            tvNoteDate = itemView.findViewById(R.id.tv_note_date);
        }

        private void adjustBodyLayoutParams() {
            ViewGroup.LayoutParams params = tvNoteMainBody.getLayoutParams();
            params.width = defaultWidth;
            params.height = (params.width - defaultHeight);
            tvNoteMainBody.setLayoutParams(params);
        }
    }


}
