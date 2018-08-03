package com.test.restaurant.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.test.restaurant.Call.APIService;
import com.test.restaurant.R;
import com.test.restaurant.adapter.ItemWithHeaderAdapter;
import com.test.restaurant.adapterCallBack.ItemClickCallback;
import com.test.restaurant.pojo.Categories;
import com.test.restaurant.pojo.CategoryDetails;
import com.test.restaurant.pojo.ContentItem;
import com.test.restaurant.pojo.Details;
import com.test.restaurant.pojo.Header;
import com.test.restaurant.pojo.ItemDetails;
import com.test.restaurant.pojo.ItemRequest;
import com.test.restaurant.pojo.Items;
import com.test.restaurant.pojo.ListItem;
import com.test.restaurant.pojo.MenuDetails;
import com.test.restaurant.pojo.ResponseData;
import com.test.restaurant.utils.ApiUtils;
import com.test.restaurant.utils.LinearLayoutManagerWithSmoothScroller;
import com.test.restaurant.utils.LockableBottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity implements ItemClickCallback {
    @BindView(R.id.item_recycler_view)
    RecyclerView itemListView;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.star_and_time)
    TextView ratingAndTime;
    @BindView(R.id.ph_no)
    TextView phoneNo;
    @BindView(R.id.main_lay)
    CoordinatorLayout mainLay;
    @BindView(R.id.total_count)
    TextView total_count;
    @BindView(R.id.footer_lay)
    LinearLayout footer_lay;
    @BindView(R.id.menu_lay)
    LinearLayout menu_lay;
    List<List<ListItem>> arraylist;
    ItemWithHeaderAdapter adapter;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.item_header_list)
    RecyclerView itemHeaderList;
    @BindView(R.id.item_header_bottom_sheet)
    LinearLayout itemHeaderLayoutBottomSheet;
    @BindView(R.id.collapsing_toolbar)
    Toolbar toolbar;
    BottomSheetBehavior itemHeaderSheetBehavior;
    ArrayList<Header> headerList = new ArrayList<>();
    private APIService mAPIService;
    public static final int REQUEST_CODE = 1;
    SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss");
    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
    // public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ButterKnife.bind(this);
        context = this;
        progressDialog = new ProgressDialog(this);
        mAPIService = ApiUtils.getAPIService();
        itemHeaderSheetBehavior = BottomSheetBehavior.from(itemHeaderLayoutBottomSheet);
        footer_lay.setVisibility(View.GONE);
        mainLay.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display home menu item.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    collapsingToolbarLayout.setTitle("Inka Restaurant");

                } else {
                    //Expanded
                    collapsingToolbarLayout.setTitle("");

                }
            }
        });


        menu_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog();
            }
        });
        itemHeaderSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        itemHeaderSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        if (itemHeaderSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) itemHeaderSheetBehavior).setLocked(true);
                        }
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        footer_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.putParcelableArrayListExtra("itemValues", (ArrayList<? extends Parcelable>) (adapter.list));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadItems();
        // testLoad();

    }

    private void testLoad() {
        String res = "{\"status\":true,\"msg\":\"menus data loaded\",\"data\":{\"service_id\":1,\"group_id\":2,\"crm_id\":1,\"name\":\"Inka\",\"description\":\"La plongÃ©e dans lâ€™AmÃ©rique du sud, aux confins des Andes, se poursuit Ã  table. Le jeune Julien Burbaud (candidat Top Chef 2012) et son Ã©quipe composent une cuisine ethnique, adaptÃ©e aux palais occidentaux, oÃ¹ les contrastes de texture vont une fois de plus troubler les sens.\",\"phone_number\":\"+33 142717715\",\"logo\":\"http://54.153.23.209/Hive-Admin/uploads/profile/XOKoBGDYyudsJ95HSa7L.png\",\"lat\":\"48.86395\",\"longs\":\"2.365954\",\"cover_img\":\"https://media-cdn.tripadvisor.com/media/photo-s/12/53/13/e0/1k-pisco-bar.jpg\",\"start_time\":\"08:00:00\",\"end_time\":\"23:00:00\",\"status\":1,\"created_by\":1,\"avg_dining_time\":2,\"temp\":1,\"table_reservation\":1,\"is_discover\":1,\"reviews_count\":0,\"reviews_avg\":0,\"menu\":[{\"menu_service_mapping_id\":1,\"menu_id\":1,\"service_id\":1,\"group_id\":2,\"status\":1,\"menu_details\":{\"menu_id\":1,\"name\":\"Menu Inka\",\"group_id\":2,\"status\":1,\"categories\":[{\"menu_category_mapping_id\":1,\"menu_id\":1,\"category_id\":1,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":1,\"group_id\":2,\"name\":\"Snacks\",\"description\":\"Snacks\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":1,\"item_id\":1,\"category_id\":1,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":1,\"group_id\":2,\"name\":\"Potato Chips\",\"description\":\"Tricolore\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"4\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":2,\"item_id\":1,\"item_ingredient_id\":3,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}},{\"item_ingredients_mapping_id\":3,\"item_id\":1,\"item_ingredient_id\":1,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":1,\"name\":\"Beef\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":2,\"item_id\":2,\"category_id\":1,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":2,\"group_id\":2,\"name\":\"Pickles\",\"description\":\"Escabeche Style\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"3\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":6,\"item_id\":2,\"item_ingredient_id\":3,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":3,\"item_id\":3,\"category_id\":1,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":3,\"group_id\":2,\"name\":\"Trio Maïs\",\"description\":\"Surprise\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"3\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":4,\"item_id\":3,\"item_ingredient_id\":2,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":2,\"name\":\"Pork\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":4,\"item_id\":4,\"category_id\":1,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":4,\"group_id\":2,\"name\":\"Chicharron\",\"description\":\"Pickles, chipotle mayo\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"6\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":1,\"item_id\":4,\"item_ingredient_id\":2,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":2,\"name\":\"Pork\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":5,\"item_id\":5,\"category_id\":1,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":5,\"group_id\":2,\"name\":\"Guacamole\",\"description\":\"Jalapeño\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"6\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":6,\"item_id\":6,\"category_id\":1,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":6,\"group_id\":2,\"name\":\"Edamame\",\"description\":\"Fumé\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"6\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}}]},{\"menu_category_mapping_id\":2,\"menu_id\":1,\"category_id\":2,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":2,\"group_id\":2,\"name\":\"Salades\",\"description\":\"Salades\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":7,\"item_id\":7,\"category_id\":2,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":7,\"group_id\":2,\"name\":\"Maïs Bio\",\"description\":\"Poivron Rouge, citron, coriandre\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"8\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":8,\"item_id\":8,\"category_id\":2,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":8,\"group_id\":2,\"name\":\"Crudite Asperge/Mango\",\"description\":\"Avocat, Rhubarbe\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"9\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":9,\"item_id\":9,\"category_id\":2,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":9,\"group_id\":2,\"name\":\"Quinoa Edamame\",\"description\":\"Grenade, vinaigrette péruvienne\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"9\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":5,\"item_id\":9,\"item_ingredient_id\":3,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}}]},{\"menu_category_mapping_id\":3,\"menu_id\":1,\"category_id\":3,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":3,\"group_id\":2,\"name\":\"Taquitos (deux par commande)\",\"description\":\"Taquitos (deux par commande)\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":10,\"item_id\":10,\"category_id\":3,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":10,\"group_id\":2,\"name\":\"Joue de bœuf confite à la bierre\",\"description\":\"coriandre, crema\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"10\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":7,\"item_id\":10,\"item_ingredient_id\":2,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":2,\"name\":\"Pork\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":11,\"item_id\":11,\"category_id\":3,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":11,\"group_id\":2,\"name\":\"Baja Fish Tacos\",\"description\":\"Beignet de poisson, coleslaw de fenouil\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"12\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":8,\"item_id\":11,\"item_ingredient_id\":3,\"group_id\":1,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":12,\"item_id\":12,\"category_id\":3,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":12,\"group_id\":2,\"name\":\"Pastéque grillée\",\"description\":\"queso fresco, menthe\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"8\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}}]},{\"menu_category_mapping_id\":4,\"menu_id\":1,\"category_id\":4,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":4,\"group_id\":2,\"name\":\"Clasicos del Inka\",\"description\":\"Clasicos del Inka\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":13,\"item_id\":13,\"category_id\":4,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":13,\"group_id\":2,\"name\":\"Ceviche Inka\",\"description\":\"Poisson du jour, leche de tigre, cancha\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"12\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":9,\"item_id\":13,\"item_ingredient_id\":3,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}},{\"item_ingredients_mapping_id\":15,\"item_id\":13,\"item_ingredient_id\":4,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":4,\"name\":\"Dairy\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":14,\"item_id\":14,\"category_id\":4,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":14,\"group_id\":2,\"name\":\"Aeropuerto de Quinoa\",\"description\":\"Cochon, gambas, légumes sautés\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"17\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":10,\"item_id\":14,\"item_ingredient_id\":3,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":15,\"item_id\":15,\"category_id\":4,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":15,\"group_id\":2,\"name\":\"Pollo a la brasa\",\"description\":\"poulet rôti, pommes de terre\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"16\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":11,\"item_id\":15,\"item_ingredient_id\":3,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}}]},{\"menu_category_mapping_id\":5,\"menu_id\":1,\"category_id\":5,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":5,\"group_id\":2,\"name\":\"Ceviches y Crudos\",\"description\":\"Ceviches y Crudos\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":16,\"item_id\":16,\"category_id\":5,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":16,\"group_id\":2,\"name\":\"Ceviche de bar\",\"description\":\"aji amarillo, avocat, popcorn\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"13\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":12,\"item_id\":16,\"item_ingredient_id\":3,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":17,\"item_id\":17,\"category_id\":5,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":17,\"group_id\":2,\"name\":\"Tostada Thon Rouge de Méditerranée\",\"description\":\"Thon Balfego, chipotle mayo, avocat, sesame,\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"15\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":13,\"item_id\":17,\"item_ingredient_id\":3,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}},{\"category_item_mapping_id\":18,\"item_id\":18,\"category_id\":5,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":18,\"group_id\":2,\"name\":\"Tiradito de Daurade\",\"description\":\"mango, fruit de la passion, radis\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"14\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[{\"item_ingredients_mapping_id\":14,\"item_id\":18,\"item_ingredient_id\":3,\"group_id\":2,\"status\":1,\"created_at\":\"2018-01-22 13:00:00\",\"updated_at\":\"2018-01-22 13:00:00\",\"ingredient_info\":{\"item_ingredient_id\":3,\"name\":\"Nuts\",\"status\":1,\"created_at\":\"2018-01-23 13:00:00\",\"updated_at\":\"2018-01-23 13:00:00\"}}]}}]},{\"menu_category_mapping_id\":6,\"menu_id\":1,\"category_id\":6,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":6,\"group_id\":2,\"name\":\"La Tierra\",\"description\":\"La Tierra\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":19,\"item_id\":19,\"category_id\":6,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":19,\"group_id\":2,\"name\":\"Bao de Cochon de Lait\",\"description\":\"Hoisin bbq, carrot, coriandre\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"16\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":20,\"item_id\":20,\"category_id\":6,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":20,\"group_id\":2,\"name\":\"Canard Nikkei\",\"description\":\"Sauce tamarin, lentilles du Pérou, oignon grillée\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"17\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":21,\"item_id\":21,\"category_id\":6,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":21,\"group_id\":2,\"name\":\"Wagyu cœur de rumsteak (150GR)\",\"description\":\"Du Chili, arroz negro (riz noir), asperges\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"25\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}}]},{\"menu_category_mapping_id\":7,\"menu_id\":1,\"category_id\":7,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":7,\"group_id\":2,\"name\":\"La Mar\",\"description\":\"La Mar\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":22,\"item_id\":22,\"category_id\":7,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":22,\"group_id\":2,\"name\":\"Tartare de Gambas / Daurade\",\"description\":\"Avocat, aji panka, mayo japonaise, potato chips\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"17\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":23,\"item_id\":23,\"category_id\":7,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":23,\"group_id\":2,\"name\":\"Bar a la Plancha\",\"description\":\"Pommes de terre, chimichurri\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"19\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":24,\"item_id\":24,\"category_id\":7,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":24,\"group_id\":2,\"name\":\"Maquereau en escabeche\",\"description\":\"Tomate, fenouil, aji amarillo confit\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"15\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}}]},{\"menu_category_mapping_id\":8,\"menu_id\":1,\"category_id\":8,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":8,\"group_id\":2,\"name\":\"Dulces\",\"description\":\"Dulces\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":25,\"item_id\":25,\"category_id\":8,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":25,\"group_id\":2,\"name\":\"Lucuma et Agrumes\",\"description\":\"Du Pérou, kumquat, citron\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"9\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":26,\"item_id\":26,\"category_id\":8,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":26,\"group_id\":2,\"name\":\"Pisco-Coco\",\"description\":\"Pisco sour, noix de coco\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"10\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":27,\"item_id\":27,\"category_id\":8,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":27,\"group_id\":2,\"name\":\"Chocolate\",\"description\":\"Cafe grand cru Pérou\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"10\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}}]},{\"menu_category_mapping_id\":9,\"menu_id\":1,\"category_id\":9,\"group_id\":2,\"status\":1,\"category_details\":{\"category_id\":9,\"group_id\":2,\"name\":\"Autres\",\"description\":\"Autres\",\"image\":\"\",\"status\":1},\"items\":[{\"category_item_mapping_id\":28,\"item_id\":28,\"category_id\":9,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":28,\"group_id\":2,\"name\":\"Degustation Six Platillos\",\"description\":\"Accord mets et vins / cocktails + €22\\t\\t\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"48\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":29,\"item_id\":29,\"category_id\":9,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":29,\"group_id\":2,\"name\":\"Ceviche Omakase\",\"description\":\"Servi exclusivement au ceviche bar\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"39\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":30,\"item_id\":30,\"category_id\":9,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":30,\"group_id\":2,\"name\":\"Dejeuner Inka Express\",\"description\":\"ceviche + salade + dessert du jour\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"19\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}},{\"category_item_mapping_id\":31,\"item_id\":31,\"category_id\":9,\"group_id\":2,\"status\":1,\"item_details\":{\"item_id\":31,\"group_id\":2,\"name\":\"Sunday Brunch\",\"description\":\"Latin style buffet 12h-16h\",\"image\":\"\",\"prep_time\":\"30\",\"currency\":\"Euros\",\"price\":\"39\",\"status\":1,\"item_type_id\":1,\"item_ingredients\":[]}}]}]}}]}}";
        ResponseData responseData = new Gson().fromJson(res, ResponseData.class);
        loadService(responseData);
    }

    private void loadItems() {
        try {
            isShowProgress();
            if (isInternetAvailable()) {
                ItemRequest task = new ItemRequest("2", "1");
                mAPIService.getData(task).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            loadService(response.body());
                        }else {
                            showMsg(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        showMsg(getApplicationContext().getString(R.string.no_server));
                    }
                });
            } else {
                showAlertDialog(this, getApplicationContext().getString(R.string.no_net));
            }
        } catch (Exception e) {
            showMsg(getApplicationContext().getString(R.string.error_msg));
            e.printStackTrace();
        }
    }


    private void loadService(final ResponseData respData) {
        runOnUiThread(new Runnable() {
            public void run() {
                ArrayList<ListItem> arrayList = new ArrayList<>();
                try {
                    if (respData.getStatus()) {
                        Details headerDetails = respData.getData();
                        final String headerImgUrl = headerDetails.getCover_img();
                        Picasso.with(context).load(headerImgUrl).fit().centerCrop()
                                .placeholder(R.drawable.no_img)
                                .error(R.drawable.no_img)
                                .into(headerImage);
                        headerTitle.setText(headerDetails.getName() + " Restaurant");
                        phoneNo.setText(headerDetails.getPhone_number());
                        Date from = formatDate.parse(headerDetails.getStart_time());
                        Date to = formatDate.parse(headerDetails.getEnd_time());
                        ratingAndTime.setText(headerDetails.getReviews_avg() + "(" + headerDetails.getReviews_count() + "+) | All days : " + dateFormat.format(from) + " - " + dateFormat.format(to));
                        ArrayList<com.test.restaurant.pojo.Menu> menuArray = headerDetails.getMenu();
                        if (menuArray.size() > 0) {
                            com.test.restaurant.pojo.Menu menuRowObj = menuArray.get(0);
                            MenuDetails menuDetailsObj = menuRowObj.getMenu_details();
                            ArrayList<Categories> categoriesArray = menuDetailsObj.getCategories();
                            int headerPos = 0;
                            for (int i = 0; i < categoriesArray.size(); i++) {
                                Categories categoryRow = categoriesArray.get(i);
                                CategoryDetails categoryObj = categoryRow.getCategory_details();
                                ArrayList<Items> itemsArray = categoryRow.getItems();
                                Header header = new Header();
                                String headerName = categoryObj.getName();
                                header.setHeader(headerName);
                                header.setHeaderPos(headerPos);
                                header.setItemCount(itemsArray.size());
                                headerList.add(header);
                                arrayList.add(header);
                                for (int j = 0; j < itemsArray.size(); j++) {
                                    Items itemRow = itemsArray.get(j);
                                    ItemDetails itemDetailsObj = itemRow.getItem_details();
                                    headerPos++;
                                    ContentItem item = new ContentItem();
                                    item.setName(headerName);
                                    item.setItemName(itemDetailsObj.getName());
                                    item.setItemSubName(itemDetailsObj.getDescription());
                                    item.setPrice(itemDetailsObj.getPrice());
                                    item.setCount(0);
                                    item.setItemPos(headerPos);
                                    arrayList.add(item);
                                }
                                headerPos++;
                            }
                        }
                        adapter = new ItemWithHeaderAdapter(HomePageActivity.this, arrayList);
                        itemListView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));
                        itemListView.setAdapter(adapter);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        mainLay.setVisibility(View.VISIBLE);
                    } else {
                        showMsg(respData.getMsg());
                    }

                } catch (Exception e) {
                    showMsg(getApplicationContext().getString(R.string.error_msg));
                    e.printStackTrace();
                }

            }
        });

    }

    private void showMsg(String msg) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        Toast.makeText(context, msg,
                Toast.LENGTH_SHORT).show();
    }


    private boolean isInternetAvailable() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    private void isShowProgress() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getApplicationContext().getString(R.string.pb_title));
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();
    }

    private void showAlertDialog(Context activity, String err_msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(err_msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder1.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                loadItems();
            }
        });
        try {
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

                adapter.setItemValues(data.<ListItem>getParcelableArrayListExtra("itemValues"));
                adapter.notifyDataSetChanged();
                onChangeCallback();
            }
        } catch (Exception ex) {
            Toast.makeText(HomePageActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onChangeCallback() {
        List<ListItem> listValue = adapter.getList();
        int selectedCount = 0;
        for (ListItem li : listValue) {
            if (!isPositionHeader(li)) {
                ContentItem Item = (ContentItem) li;
                if (Item.getCount() > 0)
                    selectedCount += Item.getCount();
            }
        }
        if (selectedCount > 0) {
            footer_lay.setVisibility(View.VISIBLE);
            total_count.setText("VIEW CART(" + selectedCount + " ITEMS)");
        } else {
            footer_lay.setVisibility(View.GONE);
        }
    }

    private boolean isPositionHeader(ListItem li) {

        return li instanceof Header;

    }

    private void loadDialog() {
        stopScrolling();
        LinearLayoutManager layoutManager = ((LinearLayoutManager) itemListView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        List<ListItem> listValue = adapter.getList();
        List<String> displayItems = new ArrayList<>();
        for (int i = firstVisiblePosition; i < lastVisiblePosition; i++) {
            ListItem li = listValue.get(i);
            if (!isPositionHeader(li)) {
                ContentItem Item = (ContentItem) li;
                displayItems.add(Item.getName());
            }
        }
        Set<String> set = new LinkedHashSet<>();
        set.addAll(displayItems);
        int selCount = 0;
        String selName = "";
        for (String s : set) {
            if (selCount <= Collections.frequency(displayItems, s)) {
                selCount = Collections.frequency(displayItems, s);
                selName = s;
            }
        }
        //Toast.makeText(getApplicationContext(), selName, Toast.LENGTH_LONG).show();
        ItemHeaderAdapter mAdapter = new ItemHeaderAdapter(headerList, selName);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        itemHeaderList.setLayoutManager(mLayoutManager);
        itemHeaderList.setItemAnimator(new DefaultItemAnimator());
        itemHeaderList.setAdapter(mAdapter);
        itemHeaderSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }


    private void stopScrolling() {
        long now = SystemClock.uptimeMillis();
        MotionEvent cancel = MotionEvent.obtain(now, now, MotionEvent.ACTION_CANCEL, 0, 0, 0);
        itemListView.dispatchTouchEvent(cancel);
    }

    private void setSelection(int pos, boolean jumpToBottom) {
        appBarLayout.setExpanded(false);
        itemHeaderSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // setSelection(itemListView, pos, jumpToBottom);
        itemListView.smoothScrollToPosition(pos);
    }

    private static void setSelection(final RecyclerView listView, int pos, boolean jumpToBottom) {
        if (jumpToBottom) {
            final View lastChild = listView.getChildAt(listView.getChildCount() - 1);
            if (lastChild != null) {
                listView.getLayoutManager().scrollToPosition(pos);
                return;
            }
        }
        listView.getLayoutManager().scrollToPosition(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (itemHeaderSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            itemHeaderSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else
            super.onBackPressed();
    }


    public class ItemHeaderAdapter extends RecyclerView.Adapter<ItemHeaderAdapter.MyViewHolder> {

        private ArrayList<Header> itemsHeaderList;
        String selectedHeaderName;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView headerName, count;

            public MyViewHolder(View view) {
                super(view);
                headerName = (TextView) view.findViewById(R.id.header_name);
                count = (TextView) view.findViewById(R.id.item_count);
            }
        }


        public ItemHeaderAdapter(ArrayList<Header> itemsHeaderList, String selectedHeaderName) {
            this.itemsHeaderList = itemsHeaderList;
            this.selectedHeaderName = selectedHeaderName;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header_dialog_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Header header = itemsHeaderList.get(position);
            holder.headerName.setText(header.getHeader());
            holder.count.setText(String.valueOf(header.getItemCount()));
            if (selectedHeaderName.equalsIgnoreCase(header.getHeader())) {
                holder.headerName.setTypeface(null, Typeface.BOLD);
                holder.count.setTypeface(null, Typeface.BOLD);
            } else {
                holder.headerName.setTypeface(null, Typeface.NORMAL);
                holder.count.setTypeface(null, Typeface.NORMAL);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelection(header.getHeaderPos(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemsHeaderList.size();
        }
    }

}
