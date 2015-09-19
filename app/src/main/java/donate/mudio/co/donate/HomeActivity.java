package donate.mudio.co.donate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;

public class HomeActivity extends AppCompatActivity {

    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Toast.makeText(this, location.getLatitude() + ", " + location.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putFloat(LATITUDE, (float) location.getLatitude());
            editor.putFloat(LONGITUDE, (float) location.getLongitude());
            editor.apply();
        }

        // Send the gps coordinates to the server
        // Server return a list of food banks nearby
        String[] stringData = {"a", "b", "c"};

        // Display all the nearby food bank in list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                stringData);
        ((ListView) findViewById(R.id.food_bank)).setAdapter(adapter);

        mListView = (ListView) findViewById(R.id.food_bank);
        TextView listTitle = new TextView(this);
        listTitle.setText("Nearby Charities:");
        mListView.addHeaderView(listTitle);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_name = parent.getItemAtPosition(position).toString();
                Toast.makeText(getBaseContext(), item_name + " is selected ", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(HomeActivity.this, FoodBankActivity.class);
                intent.putExtra("foodBankName", item_name);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_location) {
            UpdateLocationFragment new_location_dialog = new UpdateLocationFragment();
            FragmentManager fm = getSupportFragmentManager();
            new_location_dialog.show(fm, "location_fragment_dialog");
        } else if(id == R.id.sign_out_button) {
            if (LoginActivity.mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(LoginActivity.mGoogleApiClient);
                LoginActivity.mGoogleApiClient.disconnect();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static class UpdateLocationFragment extends DialogFragment {

        public UpdateLocationFragment() {
            // Empty constructor required for DialogFragment
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.location_fragment_dialog, container);
            final EditText editText = (EditText) view.findViewById(R.id.new_location_text);
            view.findViewById(R.id.update_location_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), editText.getText().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(HomeActivity.class.getSimpleName(), editText.getText().toString());
                            // Send new location to server
                            // Does something and get coordinate back from server

                            // Update the preference
                            //editor.putFloat(LATITUDE, (float) location.getLatitude());
                            //editor.putFloat(LONGITUDE, (float) location.getLongitude());
                            //editor.apply();

                            getDialog().dismiss();
                            // Refresh the current page
                            Activity activity = getActivity();
                            activity.finish();
                            startActivity(activity.getIntent());
                        }
                    });
            return view;
        }
    }
}