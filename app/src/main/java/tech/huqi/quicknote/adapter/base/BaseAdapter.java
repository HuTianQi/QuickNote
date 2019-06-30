package tech.huqi.quicknote.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by hzhuqi on 2019/4/10
 */

/**
 * RecycleView Adapter的抽象基类，用来定义一些所有Adapter通用的属性方法
 *
 * @param <T> Adapter需要的数据类型的泛型表示
 * @param <V> Adapter对应的Item视图的泛型表示,必须实现IAdapterItem接口
 */
public abstract class BaseAdapter<T, V extends IAdapterItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "QuickNote.BaseAdapter";
    protected Context mContext;
    protected List<T> mDatas;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
    }

    protected abstract V inflateView(Context context, ViewGroup parent);

    public void refreshData(List<T> datas) {
        if (datas == null) return;
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.w(TAG, "[BaseAdapter]#onCreateViewHolder");
        View itemView = (View) inflateView(mContext, parent);
        if (mItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, (int) v.getTag());
                }
            });
        }
        if (mItemLongClickListener != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
                    return true;  // 返回true，拦截onClick事件
                }
            });
        }
        return new RecyclerView.ViewHolder(itemView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Log.d(TAG, "[BaseAdapter]#onBindViewHolder");
        IAdapterItem itemView = (V) viewHolder.itemView;
        viewHolder.itemView.setTag(position);
        itemView.bindDataToView(getItemData(position), position);
    }

    T getItemData(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }
}
