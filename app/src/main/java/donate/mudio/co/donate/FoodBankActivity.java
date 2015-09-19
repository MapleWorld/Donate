package donate.mudio.co.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class FoodBankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_bank);

        Intent intentN = getIntent();
        String foodBankName = intentN.getStringExtra("foodBankName");
        // Send the food bank name to server to get the info
        // Show the info in the page
        ((TextView) findViewById(R.id.food_bank_info)).setText(foodBankName + " Other Random Data");
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
}
