package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    TextView id;
    TextView infoLabel;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(com.trasimus.tictactoe.online.R.id.view_root));

        id = (TextView) findViewById(com.trasimus.tictactoe.online.R.id.id);
        infoLabel = (TextView) findViewById(com.trasimus.tictactoe.online.R.id.info_label);
        info = (TextView) findViewById(com.trasimus.tictactoe.online.R.id.info);

        if (AccessToken.getCurrentAccessToken() != null) {
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                //displayProfileInfo(currentProfile);
                id.setText(currentProfile.getId());
                infoLabel.setText("Name");
                info.setText(currentProfile.getName());
            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
                id.setText(currentProfile.getId());
                infoLabel.setText("Name");
                info.setText(currentProfile.getName());
            }
        }
        else{
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // Get Account Kit ID
                    String accountKitId = account.getId();
                    id.setText(accountKitId);

                    // Get phone number
                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    if (phoneNumber != null) {
                        String phoneNumberString = formatPhoneNumber(phoneNumber.toString());
                        info.setText(phoneNumberString);
                        infoLabel.setText(com.trasimus.tictactoe.online.R.string.phone_label);
                    } else {

                        // Get email
                        String email = account.getEmail();
                        info.setText(email);
                        infoLabel.setText(com.trasimus.tictactoe.online.R.string.email_label);
                    }
                }

                @Override
                public void onError(final AccountKitError error) {
                    String toast = error.getErrorType().getMessage();
                    Toast.makeText(AccountActivity.this, toast, Toast.LENGTH_LONG).show();
                    // Handle Error
                }
            });
        }




    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLogout(View view){
        AccountKit.logOut();
        LoginManager.getInstance().logOut();
        launchLoginActivity();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

}
