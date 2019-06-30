package tech.huqi.quicknote.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.huqi.quicknote.R;

/**
 * Created by hzhuqi on 2019/4/16
 * activity (or fragment) needs to implement {@link ItemListDialogFragment.Listener}.
 */
public class ItemListDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ItemListDialogFragment";
    private static int[] mIcons;
    private static String[] mTexts;
    private Listener mListener;

    public static ItemListDialogFragment newInstance(int[] icons, String[] texts) {
        final ItemListDialogFragment fragment = new ItemListDialogFragment();
        mIcons = icons;
        mTexts = texts;
        return fragment;
    }

    public ItemListDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter());
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        final Fragment parent = getParentFragment();
//        if (parent != null) {
//            mListener = (Listener) parent;
//        } else {
//            mListener = (Listener) context;
//        }
//    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout background;
        final ImageView icon;
        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_dialog_list_item, parent, false));
            background = itemView.findViewById(R.id.fragment_dialog_list_item_rl);
            icon = itemView.findViewById(R.id.fragment_dialog_list_item_iv);
            text = (TextView) itemView.findViewById(R.id.fragment_dialog_list_item_tv);
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.icon.setImageResource(mIcons[position]);
            holder.text.setText(mTexts[position]);
        }

        @Override
        public int getItemCount() {
            return mIcons == null ? 0 : mIcons.length;
        }

    }
}
