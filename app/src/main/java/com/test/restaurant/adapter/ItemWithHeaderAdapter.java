package com.test.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.restaurant.adapterCallBack.ItemClickCallback;
import com.test.restaurant.pojo.ContentItem;
import com.test.restaurant.pojo.Header;
import com.test.restaurant.pojo.ListItem;
import com.test.restaurant.R;

import java.util.List;

public class ItemWithHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ItemClickCallback mItemClickCallback;
    public List<ListItem> list;

    public ItemWithHeaderAdapter(ItemClickCallback callback, List<ListItem> headerItems) {
        this.mItemClickCallback = callback;
        this.list = headerItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new VHHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
            return new VHItem(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHHeader) {
            // VHHeader VHheader = (VHHeader)holder;
            Header currentItem = (Header) list.get(position);
            VHHeader VHheader = (VHHeader) holder;
            VHheader.txtTitle.setText(currentItem.getHeader());
        } else if (holder instanceof VHItem) {
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
            VHitem.addLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.setCount(1);
                    notifyItemChanged(position);
                    mItemClickCallback.onChangeCallback();
                }
            });
            VHitem.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.setCount(currentItem.getCount() + 1);
                    notifyItemChanged(position);
                    mItemClickCallback.onChangeCallback();
                }
            });
            VHitem.subButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.setCount(currentItem.getCount() - 1);
                    notifyItemChanged(position);
                    mItemClickCallback.onChangeCallback();
                }
            });
        }
    }

    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {

        return list.get(position) instanceof Header;

    }

    public void setItemValue(ListItem itemValue, int pos) {
        this.list.set(pos, itemValue);
    }

    public void setItemValues(List<ListItem> itemValues) {
        this.list = itemValues;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ListItem> getList() {
        return list;
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = (TextView) itemView.findViewById(R.id.txtHeader);
        }
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