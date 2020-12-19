package com.jrichmond.stockpile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private final int REQUEST_WRITE_CODE = 0;
    private final int REQUEST_CODE_NEW_ITEM = 1;
    private final int REQUEST_CODE_UPDATED_ITEM = 2;
    private ItemDatabase mItemDb;
    private ItemAdapter mItemAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Singleton
        mItemDb = ItemDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.itemRecyclerView);

        // Create Linear Layout
        RecyclerView.LayoutManager linearLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Shows the available items
        mItemAdapter = new ItemAdapter(loadItems());
        mRecyclerView.setAdapter(mItemAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load items here in case settings changed
        mItemAdapter = new ItemAdapter(loadItems());
        mRecyclerView.setAdapter(mItemAdapter);
    }

    private List<Item> loadItems() {
        // TODO: add options for sorting here
        return mItemDb.itemDao().getItems();
    }

    public void addItemClick(View view) {
        Intent intent = new Intent(ItemListActivity.this, ItemEditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_ITEM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.search:
                return true;

            case R.id.filter:
                return true;

            case R.id.alerts:
                // Request Permission: textbook Section 5.2
                boolean permitted = hasSmsPermissions();
                if (permitted) {
                    AlertDialog phoneNumDialog = phoneNumberDialog();
                    phoneNumDialog.show();
                }
                hasSmsPermissions();
                return true;

            case R.id.editLocations:
                Intent intent = new Intent(ItemListActivity.this, LocationListActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean hasSmsPermissions() {
        String smsPermission = Manifest.permission.SEND_SMS;
        if (ContextCompat.checkSelfPermission(this, smsPermission)
                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, smsPermission )) {
            //TODO add rationale dialog
//                showPermissionRationaleDialog();
//            } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{smsPermission}, REQUEST_WRITE_CODE);
//            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    phoneNumberDialog();
                } else {
                    // Permission denied
                }
                return;
            }
        }
    }

    private AlertDialog phoneNumberDialog() {
        final EditText input = new EditText(ItemListActivity.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setTextColor(getResources().getColor(R.color.secondaryTextColor));
        input.setGravity(Gravity.CENTER);

        AlertDialog phoneNumberDialog = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle(R.string.set_phone)
                .setMessage(R.string.sms_alerts_message)
                .setView(input)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        PhoneNumber mPhoneNumber = new PhoneNumber();
                        mPhoneNumber.setId(1);
                        mPhoneNumber.setPhoneNumber(input.getText().toString());
                        mItemDb.phoneDao().insertNumber(mPhoneNumber);
                    }

                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return phoneNumberDialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_NEW_ITEM) {
            Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_UPDATED_ITEM) {
            Toast.makeText(this, R.string.item_updated, Toast.LENGTH_SHORT).show();
        }

    }

    private class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView mNameView;
        private final TextView mNumberView;
        private final TextView mQtyView;
        private Item mItem;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mNameView = itemView.findViewById(R.id.itemName);
            mNumberView = itemView.findViewById(R.id.itemNumber);
            mQtyView = itemView.findViewById(R.id.itemQty);
        }

        public void bind(Item item, int position) {
            mItem = item;

            mNameView.setText(item.getItemName());
            mNumberView.setText(item.getItemNumber());
            // TODO sum item quantities in all locations to att to view
            mQtyView.setText("");

        }


        @Override
        public void onClick(View view) {
            // Start ItemDetailActivity, indicating what item was clicked
            Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
            intent.putExtra(ItemDetailActivity.EXTRA_ITEM_ID, mItem.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        private final List<Item> mItemList;

        public ItemAdapter(List<Item> items) {
            mItemList = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bind(mItemList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

    }

}