package donate.mudio.co.donate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class FoodBankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_bank);

        Intent intentN = getIntent();
        String foodbank_id = intentN.getStringExtra("foodbank_id");
        // Send the food bank id to server to get the info
        // Show the info in the page
        //
        //TextView foodbank_info = (TextView) findViewById(R.id.foodbank_info);
        //foodbank_info.setText("asdsadsadsa");

        findViewById(R.id.donate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonateFragment new_donate_dialog = new DonateFragment();
                FragmentManager fm = getSupportFragmentManager();
                new_donate_dialog.show(fm, "donate_fragment_dialog");

                //setContentView(R.layout.donate_fragment_dialog);
                // Send data to server, code below doesn't work
                /*
                EditText donation_name = (EditText) findViewById(R.id.donation_name);
                EditText donation_weight = (EditText) findViewById(R.id.donation_weight);
                EditText donation_number = (EditText) findViewById(R.id.donation_number);
                String donation_name_string = donation_name.getText().toString();
                String donation_weight_string = donation_weight.getText().toString();
                String donation_number_string = donation_number.getText().toString();
                // Send these data to server
                Toast.makeText(getBaseContext(), donation_name_string + donation_weight_string + donation_number_string,
                        Toast.LENGTH_SHORT).show();
                */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_bank, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_location) {
            HomeActivity.UpdateLocationFragment new_location_dialog = new HomeActivity.UpdateLocationFragment();
            FragmentManager fm = getSupportFragmentManager();
            new_location_dialog.show(fm, "location_fragment_dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DonateFragment extends DialogFragment {

        public DonateFragment() {
            // Empty constructor required for DialogFragment
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.donate_fragment_dialog, container);
            view.findViewById(R.id.send_donate_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
