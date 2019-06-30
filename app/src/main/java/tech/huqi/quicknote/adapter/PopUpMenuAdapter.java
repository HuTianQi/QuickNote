package tech.huqi.quicknote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import tech.huqi.quicknote.R;


public class PopUpMenuAdapter extends BaseAdapter {
    private int[] mIcons;
    private String[] mTexts;
    private Context mContext;

    public PopUpMenuAdapter(Context context, int[] icons, String[] texts) {
        mIcons = icons;
        mTexts = texts;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_menu_attachment_item, null);
            viewHolder.ivItemIcon = (ImageView) convertView.findViewById(R.id.popup_menu_item_iv);
            viewHolder.tvItemText = convertView.findViewById(R.id.popup_menu_item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItemText.setText(mTexts[position]);
        viewHolder.ivItemIcon.setImageResource(mIcons[position]);
        return convertView;
    }

    static class ViewHolder {
        ImageView ivItemIcon;
        TextView tvItemText;
    }

    @Override
    public int getCount() {
        return mTexts == null ? 0 : mTexts.length;
    }

    @Override
    public Object getItem(int position) {
        return mTexts[position];
    }

//    @Override
//    public String getItem(int position) {
//        return mTexts[position];
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
