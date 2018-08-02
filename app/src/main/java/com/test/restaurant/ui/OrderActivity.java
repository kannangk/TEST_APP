package com.test.restaurant.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.restaurant.R;
import com.test.restaurant.adapter.OrderAdapter;
import com.test.restaurant.adapterCallBack.OrderChangeCallBack;
import com.test.restaurant.utils.LinearLayoutManagerWithSmoothScroller;
import com.test.restaurant.pojo.ContentItem;
import com.test.restaurant.pojo.Header;
import com.test.restaurant.pojo.ListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity implements OrderChangeCallBack {
    @BindView(R.id.order_recycler_view)
    RecyclerView orderListView;
    @BindView(R.id.total_cost)
    TextView totalCost;
    @BindView(R.id.footer_text)
    TextView footerText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    List<ListItem> list = new ArrayList<>();
    OrderAdapter orderAdapter;
    int totalAmount = 0;
    boolean isShowMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);
        ButterKnife.bind(this);
        init();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        footerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMore = !isShowMore;
                setAdapter();
            }
        });
    }

    private void init() {
        orderListView.setNestedScrollingEnabled(false);
        list = getIntent().getParcelableArrayListExtra("itemValues");
        setAdapter();
    }

    private void setAdapter() {
        orderAdapter = new OrderAdapter(this, getSelectedItems());
        orderListView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
        orderListView.setAdapter(orderAdapter);
    }

    private ArrayList<ContentItem> getSelectedItems() {
        totalAmount = 0;
        ArrayList<ContentItem> selectedItemValues = new ArrayList<>();
        for (ListItem li : list) {
            if (!isPositionHeader(li)) {
                ContentItem Item = (ContentItem) li;
                if (Item.getCount() > 0) {
                    selectedItemValues.add(Item);
                    totalAmount = totalAmount + (Item.getCount() * Item.getPrice());
                }
            }
        }
        totalCost.setText(String.valueOf(totalAmount));
        if (selectedItemValues.size() > 2) {
            footerText.setPaintFlags(footerText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            footerText.setVisibility(View.VISIBLE);
            if (isShowMore) {
                footerText.setText("Show Less");
            } else {
                ArrayList<ContentItem> selectedMinItemValues = new ArrayList<>();
                selectedMinItemValues.add(selectedItemValues.get(0));
                selectedMinItemValues.add(selectedItemValues.get(1));
                footerText.setText("Show More");
                return selectedMinItemValues;
            }
        } else {
            footerText.setVisibility(View.GONE);
        }
        return selectedItemValues;

    }

    private boolean isPositionHeader(ListItem li) {

        return li instanceof Header;

    }

    @Override
    public void onChangeCallback(Boolean isAdd, ContentItem item) {
        list.set(item.getItemPos(), item);
        if (isAdd)
            totalAmount += item.getPrice();
        else
            totalAmount -= item.getPrice();
        if (item.getCount() == 0) {
            setAdapter();
        }

        totalCost.setText(String.valueOf(totalAmount));
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra("itemValues", (ArrayList<? extends Parcelable>) (list));
        setResult(RESULT_OK, intent);
        finish();
    }
}
