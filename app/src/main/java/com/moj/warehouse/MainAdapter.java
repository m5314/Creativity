package com.moj.warehouse;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MainAdapter(List<String> data) {
        super(android.R.layout.simple_list_item_1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String s) {
        helper.setText(android.R.id.text1, s);
    }
}
