package com.test.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.restaurant.adapterCallBack.OrderChangeCallBack;
import com.test.restaurant.pojo.ContentItem;
import com.test.restaurant.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OrderChangeCallBack onChangeCallback;
    List<ContentItem> list;

    public OrderAdapter(OrderChangeCallBack callback, List<ContentItem> headerItems) {
        this.onChangeCallback = callback;
        this.list = headerItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_row, parent, false);
        return new VHItem(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ContentItem currentItem = (ContentItem) list.get(position);
        VHItem VHitem = (VHItem) holder;
        VHitem.itemName.setText(currentItem.getItemName());
        VHitem.subItemName.setText(currentItem.getItemSubName());
        VHitem.price.setText(String.valueOf(currentItem.getPrice()));
        if (currentItem.getCount() == 0) {
            VHitem.addLay.setVisibility(View.VISIBLE);
            VHitem.countLay.setVisibility(View.GONE);
        } else {
            VHitem.countValue.setText("" + currentItem.getCount());
            VHitem.addLay.setVisibility(View.GONE);
            VHitem.countLay.setVisibility(View.VISIBLE);
        }

        VHitem.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setCount(currentItem.getCount() + 1);
                notifyItemChanged(position);
                onChangeCallback.onChangeCallback(true,currentItem);
            }
        });
        VHitem.subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setCount(currentItem.getCount() - 1);
                notifyItemChanged(position);
                onChangeCallback.onChangeCallback(false,currentItem);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ContentItem> getList() {
        return list;
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView subItemName;
        LinearLayout countLay;
        LinearLayout addLay;
        TextView addButton;
        TextView subButton;
        TextView countValue;
        TextView price;

        public VHItem(View itemView) {
            super(itemView);
            this.itemName = (TextView) itemView.findViewById(R.id.item_value1);
            this.subItemName = (TextView) itemView.findViewById(R.id.item_value2);
            this.countLay = (LinearLayout) itemView.findViewById(R.id.count_lay);
            this.addLay = (LinearLayout) itemView.findViewById(R.id.add_lay);
            this.addButton = (TextView) itemView.findViewById(R.id.add_button);
            this.subButton = (TextView) itemView.findViewById(R.id.sub_button);
            this.countValue = (TextView) itemView.findViewById(R.id.count_value);
            this.price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}