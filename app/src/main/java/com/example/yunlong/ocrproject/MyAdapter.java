package com.example.yunlong.ocrproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mList = new ArrayList<>();

    public MyAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
         ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.label_item, null);
            viewHolder.editText = (EditText) view.findViewById(R.id.labelname);
            viewHolder.button1 = (Button) view.findViewById(R.id.confirm);
            viewHolder.button2 = (Button) view.findViewById(R.id.deletelabel);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.editText.setText(mList.get(i));
        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemDeleteListener.onDeleteClick(i);
            }
        });


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = finalViewHolder.editText.getText().toString();
                mOnItemConfirmListener.onConfirmClick(i, s);

            }
        });

        return view;
        }


        /**
         * 删除按钮的监听接口
         */
        public interface onItemDeleteListener {
            void onDeleteClick(int i);
        }

        private onItemDeleteListener mOnItemDeleteListener;

        public void setOnItemDeleteClickListener (onItemDeleteListener mOnItemDeleteListener){
            this.mOnItemDeleteListener = mOnItemDeleteListener;
        }

        /**
         * 确认按钮的监听接口
         */
        public interface onItemConfirmListener {
            void onConfirmClick(int i, String s);
        }

        private onItemConfirmListener mOnItemConfirmListener;
        public void setOnItemConfirmClickLister (onItemConfirmListener mOnItemConfirmListener){
            this.mOnItemConfirmListener = mOnItemConfirmListener;
        }

    }

