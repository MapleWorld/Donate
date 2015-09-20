package donate.mudio.co.donate;
/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;

import donate.mudio.co.donate.utils.Utils;

/**
 * Minimal activity demonstrating basic Google Sign-In.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;

    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;

    private AuthorizationCheckTask mAuthTask;
    private String mEmailAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailAccount = Utils.getEmailAccount(this);

        // Large sign-in
        ((SignInButton) findViewById(R.id.sign_in_button)).setSize(SignInButton.SIZE_WIDE);

        // Start with sign-in button disabled until sign-in either succeeds or fails
        findViewById(R.id.sign_in_button).setEnabled(false);

        // Set up view instances
        mStatus = (TextView) findViewById(R.id.status);
    }

    /*
    * Selects an account for talking to Google Play services. If there is more than one account on
    * the device, it allows user to choose one.
    */
    private void selectAccount() {
        Account[] accounts = Utils.getGoogleAccounts(this);
        int numOfAccount = accounts.length;
        switch (numOfAccount) {
            case 0:
                // No accounts registered, nothing to do.
                Toast.makeText(this, "No accounts registered",
                        Toast.LENGTH_LONG).show();
                break;
            case 1:
                mEmailAccount = accounts[0].name;
                performAuthCheck(mEmailAccount);
                break;
            default:
                // More than one Google Account is present, a chooser is necessary.
                // Invoke an {@code Intent} to allow the user to select a Google account.
                Intent accountSelector = AccountPicker.newChooseAccountIntent(null, null,
                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false,
                        "Select account for sign in", null, null, null);
                startActivityForResult(accountSelector, ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(null != mEmailAccount)
            performAuthCheck(mEmailAccount);
        else
            selectAccount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuthTask != null){
            mAuthTask.cancel(true);
            mAuthTask = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION && resultCode == RESULT_OK) {
            // This path indicates the account selection activity resulted in the user selecting a
            // Google account and clicking OK.
            mEmailAccount = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        } else {
            finish();
        }
    }

    /*
     * Schedule the authorization check.
     */
    private void performAuthCheck(String email) {
        // Cancel previously running tasks.
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
        }

        // Start task to check authorization.
        mAuthTask = new AuthorizationCheckTask();
        mAuthTask.execute(email);
        if (email != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Empty Email", Toast.LENGTH_SHORT).show();
        }
    }

    private class AuthorizationCheckTask extends AsyncTask<String, Integer, Boolean>{
        private final static boolean SUCCESS = true;
        private final static boolean FAILURE = false;

        @Override
        protected void onPreExecute() {
            mAuthTask  = this;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        @Override
        protected Boolean doInBackground(String... emailAccounts) {
            if(!Utils.checkGooglePlayServicesAvailable(LoginActivity.this)){
                return FAILURE;
            }

            String emailAccount = emailAccounts[0];
            mAuthTask = this;

            if(TextUtils.isEmpty(emailAccount)) return FAILURE;

            mEmailAccount = emailAccount;
            Utils.saveEmailAccount(LoginActivity.this, emailAccount);

            return SUCCESS;
        }
    }

}