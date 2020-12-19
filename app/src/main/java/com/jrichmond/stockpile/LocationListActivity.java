package com.jrichmond.stockpile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LocationListActivity extends AppCompatActivity {

    private final int REQUEST_CODE_NEW_LOCATION = 1;
    private final int REQUEST_CODE_UPDATED_LOCATION = 2;

    private RecyclerView mRecyclerView;
    private LocationListActivity.LocationAdapter mLocationAdapter;
    private ItemDatabase mItemDb;

    public static final String EXTRA_LOCATION_ID = "com.jrichmond.stockpile.location_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        setTitle(R.string.locations_title);

        // Singleton
        mItemDb = ItemDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.locationRecyclerView);

        // Create Linear Layout
        RecyclerView.LayoutManager linearLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Shows the available locations
        mLocationAdapter = new LocationListActivity.LocationAdapter(loadLocations());
        mRecyclerView.setAdapter(mLocationAdapter);
    }
    protected void onResume() {
        super.onResume();

        // Load items here in case settings changed
        mLocationAdapter = new LocationListActivity.LocationAdapter(loadLocations());
        mRecyclerView.setAdapter(mLocationAdapter);
    }

    private List<Location> loadLocations() {
        return mItemDb.locationDao().getLocations();
    }

    public void addLocationClick(View view) {
        Intent intent = new Intent(LocationListActivity.this, LocationEditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_LOCATION);
    }


    private class LocationAdapter extends RecyclerView.Adapter<LocationListActivity.LocationHolder> {

        private final List<Location> mLocationList;

        public LocationAdapter(List<Location> locations) {
            mLocationList = locations;
        }

        @Override
        public LocationListActivity.LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new LocationListActivity.LocationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocationListActivity.LocationHolder holder, int position) {
            holder.bind(mLocationList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mLocationList.size();
        }

        public void addItem(Location location) {
            // Add the new item at the beginning of the list
            mLocationList.add(0, location);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mRecyclerView.scrollToPosition(0);
        }

    }

    private class LocationHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private Location mLocation;
        private final TextView mNameView;


        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_location, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mNameView = itemView.findViewById(R.id.locationName);

        }

        public void bind(Location location, int position) {
            mLocation = location;
            mNameView.setText(location.getLocationName());

        }


        @Override
        public void onClick(View view) {
            // Start QuestionActivity, indicating what item was clicked
            Intent intent = new Intent(LocationListActivity.this, LocationEditActivity.class);
            intent.putExtra(LocationEditActivity.EXTRA_LOCATION_ID, mLocation.getId());
            startActivityForResult(intent, REQUEST_CODE_UPDATED_LOCATION);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private void editLocation() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_NEW_LOCATION) {
            Toast.makeText(this, R.string.location_added, Toast.LENGTH_SHORT).show();
        }

        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_UPDATED_LOCATION) {
            Toast.makeText(this, R.string.location_updated, Toast.LENGTH_SHORT).show();
        }


    }

}