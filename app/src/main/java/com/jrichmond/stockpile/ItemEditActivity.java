package com.jrichmond.stockpile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ItemEditActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "com.jrichmond.stockpile.item";

    private EditText mItemName;
    private EditText mItemNumber;
    private EditText mItemDescription;

    private ItemDatabase mItemDb;
    private long mItemId;
    private Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        mItemName = findViewById(R.id.detLocationName);
        mItemNumber = findViewById(R.id.detItemNumber);
        mItemDescription = findViewById(R.id.detLocationDesc);

        mItemDb = ItemDatabase.getInstance(getApplicationContext());

        // Get item ID from ItemDetailActivity
        Intent intent = getIntent();
        mItemId = intent.getLongExtra(EXTRA_ITEM_ID, -1);

        ActionBar actionBar = getSupportActionBar();

        if (mItemId == -1) {
            // Add new item
            mItem = new Item();
            setTitle(R.string.add_item);
        }
        else {
            // Update existing item
            mItem = mItemDb.itemDao().getItem(mItemId);

            mItemName.setText(mItem.getItemName());
            mItemNumber.setText(mItem.getItemNumber());
            mItemDescription.setText(mItem.getItemDescription());


            setTitle(R.string.update_item);
        }

    }

    public void saveButtonClick(View view) {

        mItem.setItemName(mItemName.getText().toString());
        mItem.setItemNumber(mItemNumber.getText().toString());
        mItem.setItemDescription(mItemDescription.getText().toString());

        if (mItemId == -1) {
            // New item
            mItemDb.itemDao().insertItem(mItem);
        } else {
            // Existing item
            mItemDb.itemDao().updateItem(mItem);
        }

        // Send back item ID
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ITEM_ID, mItem.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}