package com.jrichmond.stockpile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {
// TODO update item location count list when closing qtyChange dialog

    public static final String EXTRA_ITEM = "com.jrichmond.stockpile.item";
    public static final String EXTRA_ITEM_ID = "com.jrichmond.stockpile.item_id";
    private final int REQUEST_CODE_UPDATE_ITEM = 1;
    private ItemDatabase mItemDb;
    private long mItemId;
    private Item mItem;
    private TextView mItemName;
    private TextView mItemNumber;
    private TextView mItemDescription;

    private EditText qtyEdit;


    private CountAdapter mCountAdapter;

    private RecyclerView mRecyclerView;

    private int currentQty;
    Count selectedCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        mItemDb = ItemDatabase.getInstance(getApplicationContext());

        mItemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        mItem = mItemDb.itemDao().getItem(mItemId);

        mItemName = findViewById(R.id.detLocationName);
        mItemNumber = findViewById(R.id.detItemNumber);
        mItemDescription = findViewById(R.id.detLocationDesc);

        showItem(mItem);

        mRecyclerView = findViewById(R.id.countsRecyclerView);

        // Create Linear Layout
        RecyclerView.LayoutManager linearLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Shows counts of items
        mCountAdapter = new CountAdapter(loadCounts());
        mRecyclerView.setAdapter(mCountAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu for the app bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine which app bar item was chosen
        switch (item.getItemId()) {

            case R.id.itemEdit:
                editItem();
                return true;
            case R.id.itemDelete:
                AlertDialog deleteDialog = deleteDialog();
                deleteDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editItem() {
        Intent intent = new Intent(this, ItemEditActivity.class);
        intent.putExtra(EXTRA_ITEM, mItemId);
        long itemId = mItemId;
        intent.putExtra(ItemEditActivity.EXTRA_ITEM_ID
                , itemId);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_UPDATE_ITEM) {
            // Get updated item
            long itemId = data.getLongExtra(ItemEditActivity.EXTRA_ITEM_ID, -1);
            Item updatedItem = mItemDb.itemDao().getItem(itemId);

            // Replace current item updated item
            Item currentItem = mItem;
            currentItem.setItemName(updatedItem.getItemName());
            currentItem.setItemNumber(updatedItem.getItemNumber());
            currentItem.setItemDescription(updatedItem.getItemDescription());

            showItem(currentItem);

            Toast.makeText(this, R.string.item_updated, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        mItemDb.countDao().deleteCountsByItemId(mItem.getId());
        mItemDb.itemDao().deleteItem(mItem);
        finish();
    }


    private AlertDialog deleteDialog() {
        AlertDialog deleteDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle(R.string.delete_item)
                .setMessage(R.string.delete_item_warning_message)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteItem();
                    }

                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return deleteDialogBox;
    }

    private Dialog qtyChangeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.qty_change_dialog, null);

        qtyEdit = dialogView.findViewById(R.id.editTextNumber);


        // logic to populate locations spinner
        List<Location> allLocations = mItemDb.locationDao().getLocations();
        List<String> locationNames = new ArrayList<>();
        List<Long> locationIds = new ArrayList<>();


        for (Location location : allLocations) {
            locationIds.add(location.getId());
            locationNames.add(location.getLocationName());
        };


        int singleItemRes = R.layout.simple_spinner_item;
        Spinner spinner = dialogView.findViewById(R.id.locationSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                singleItemRes, locationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // get current count at selected location
                try {
                    selectedCount = mItemDb.countDao().getItemCountSelectedLocation(
                            locationIds.get(position),
                            mItem.getId());
                    currentQty = selectedCount.getQty();
                }
                catch(NullPointerException e) {
                    selectedCount = new Count();
                    selectedCount.setItem(mItem.getId());
                    selectedCount.setLocation(locationIds.get(position));
                    currentQty = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // build dialog box
        builder.setTitle(R.string.add_qty)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int addQty = Integer.parseInt(qtyEdit.getText().toString());
                        // add entered qty to current qty
                        int newQty = currentQty + addQty;
                        selectedCount.setQty(newQty);
                        mItemDb.countDao().insertCount(selectedCount);

                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    public void addQtyClick(View view) {
        Dialog addDialog = qtyChangeDialog();
        addDialog.show();
        ((AlertDialog) addDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        // Now set the textchange listener for edittext
        qtyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s) || Integer.parseInt(s.toString()) == 0) {
                    // Disable ok button
                    ((AlertDialog) addDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) addDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });


    }

    public void subQtyClick(View view) {
    }

    private void showItem(Item item) {

        mItemName.setText(item.getItemName());
        mItemNumber.setText(item.getItemNumber());
        mItemDescription.setText(item.getItemDescription());

    }

    private List<Count> loadCounts() {
        return mItemDb.countDao().getCountsOfItem(mItemId);
    }

    private class CountAdapter extends RecyclerView.Adapter<CountHolder> {

        private final List<Count> mCountList;

        public CountAdapter(List<Count> counts) {
            mCountList = counts;
        }

        @Override
        public CountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new CountHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CountHolder holder, int position) {
            holder.bind(mCountList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mCountList.size();
        }

    }

    private class CountHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView mLocationView;
        private final TextView mQuantityView;
        private String mCountLocationName;


        public CountHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_count, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mLocationView = itemView.findViewById(R.id.detItemLocation);
            mQuantityView = itemView.findViewById(R.id.detItemCount);

        }

        public void bind(Count count, int position) {
            mCountLocationName = mItemDb.locationDao().getLocation(count.getLocation()).getLocationName();

            mLocationView.setText(mCountLocationName);
            mQuantityView.setText(Integer.toString(count.getQty()));

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public void onClick(View v) {
        }
    }

}

