package com.jrichmond.stockpile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LocationEditActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION_ID = "com.jrichmond.stockpile.location";

    private EditText mLocationName;
    private EditText mLocationDescription;

    private ItemDatabase mItemDb;
    private long mLocationId;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);

        mLocationName = findViewById(R.id.detLocationName);
        mLocationDescription = findViewById(R.id.detLocationDesc);

        mItemDb = ItemDatabase.getInstance(getApplicationContext());

        // Get item ID from ItemDetailActivity
        Intent intent = getIntent();
        mLocationId = intent.getLongExtra(EXTRA_LOCATION_ID, -1);

        ActionBar actionBar = getSupportActionBar();

        if (mLocationId == -1) {
            // Add new item
            mLocation = new Location();
            setTitle(R.string.add_item);
        }
        else {
            // Update existing item
            mLocation = mItemDb.locationDao().getLocation(mLocationId);

            mLocationName.setText(mLocation.getLocationName());
            mLocationDescription.setText(mLocation.getLocationDescription());


            setTitle(R.string.update_item);
        }

    }

    public void saveButtonClick(View view) {

        mLocation.setLocationName(mLocationName.getText().toString());
        mLocation.setLocationDescription(mLocationDescription.getText().toString());

        if (mLocationId == -1) {
            // New item
            mItemDb.locationDao().insertLocation(mLocation);
        } else {
            // Existing item
            mItemDb.locationDao().updateLocation(mLocation);
        }

        // Send back item ID
        Intent intent = new Intent();
        intent.putExtra(EXTRA_LOCATION_ID, mLocation.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu for the app bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine which app bar item was chosen
        switch (item.getItemId()) {

            case R.id.locationDelete:
                AlertDialog diaBox = deleteDialog();
                diaBox.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog deleteDialog() {
        AlertDialog deleteDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle(R.string.delete_location)
                .setMessage(R.string.delete_location_warning_message)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteLocation();
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

    private void deleteLocation() {
        mItemDb.locationDao().deleteLocation(mLocation);
        finish();
    }

}