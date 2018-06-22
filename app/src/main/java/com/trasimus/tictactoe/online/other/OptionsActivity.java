package com.trasimus.tictactoe.online.other;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.trasimus.tictactoe.online.R;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends AppCompatActivity {

    private SeekBar primaryChar;
    private SeekBar secondaryChar;
    private SeekBar gameColor;

    private Button donate;

    private ImageView question1;
    private ImageView question2;
    private ImageView question3;

    private SharedPreferences prefs;
    public static final String CHAT_PREFS = "ChatPrefs";

    private BillingClient mBillingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        prefs = getSharedPreferences(CHAT_PREFS, 0 );

        primaryChar = (SeekBar) findViewById(R.id.primaryChar);
        secondaryChar = (SeekBar) findViewById(R.id.secondaryChar);
        gameColor = (SeekBar) findViewById(R.id.gameColor);
        question1 = (ImageView) findViewById(R.id.question1);
        question2 = (ImageView) findViewById(R.id.question2);
        question3 = (ImageView) findViewById(R.id.question3);

        donate = (Button) findViewById(R.id.donate);

        String char1 = prefs.getString("primaryChar", null);
        if (char1 == null){
            primaryChar.setProgress(0);
        } else {
            switch (char1) {
                case "x":
                    primaryChar.setProgress(0);
                    break;
                case "a":
                    primaryChar.setProgress(1);
                    break;
                case "b":
                    primaryChar.setProgress(2);
                    break;
                case "c":
                    primaryChar.setProgress(3);
                    break;
                case "d":
                    primaryChar.setProgress(4);
                    break;
                default:
                    secondaryChar.setProgress(0);
                    break;
            }
        }

        primaryChar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                switch (progress) {
                    case 0:
                        prefs.edit().putString("primaryChar", "x").apply();
                        break;
                    case 1:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        primaryChar.setProgress(0);
                        break;
                    case 2:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        primaryChar.setProgress(0);
                        break;
                    case 3:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        primaryChar.setProgress(0);
                        break;
                    case 4:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        primaryChar.setProgress(0);
                        break;
                    default:
                        break;
                }
            }
        });

        String char2 = prefs.getString("secondaryChar", null);
        if (char2 == null){
            secondaryChar.setProgress(0);
        } else {
            switch (char2) {
                case "x":
                    secondaryChar.setProgress(0);
                    break;
                case "a":
                    secondaryChar.setProgress(1);
                    break;
                case "b":
                    secondaryChar.setProgress(2);
                    break;
                case "c":
                    secondaryChar.setProgress(3);
                    break;
                case "d":
                    secondaryChar.setProgress(4);
                    break;
                default:
                    secondaryChar.setProgress(0);
                    break;
            }
        }

        secondaryChar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                switch (progress) {
                    case 0:
                        prefs.edit().putString("secondaryChar", "x").apply();
                        break;
                    case 1:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        secondaryChar.setProgress(0);
                        break;
                    case 2:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        secondaryChar.setProgress(0);
                        break;
                    case 3:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        secondaryChar.setProgress(0);
                        break;
                    case 4:
                        Toast.makeText(OptionsActivity.this, "These options are not available yet. Please wait until next update", Toast.LENGTH_LONG).show();
                        secondaryChar.setProgress(0);
                        break;
                    default:
                        break;
                }
            }
        });

        String color = prefs.getString("gameColor", null);
        if (color == null){
            gameColor.setProgress(0);
        } else {
            switch (color) {
                case "red":
                    gameColor.setProgress(0);
                    break;
                case "blue":
                    gameColor.setProgress(1);
                    break;
                case "green":
                    gameColor.setProgress(2);
                    break;
                default:
                    gameColor.setProgress(0);
                    break;
            }
        }

        gameColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                switch (progress){
                    case 0:
                        prefs.edit().putString("gameColor", "red").apply();
                        break;
                    case 1:
                        prefs.edit().putString("gameColor", "blue").apply();
                        break;
                    case 2:
                        prefs.edit().putString("gameColor", "green").apply();
                        break;
                    default:
                        break;
                }
            }
        });

//        donate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //doPayment();
//            }
//        });

        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OptionsActivity.this, "In offline mode, this character is Player's 1 character, in online mode, this character will be used in case you host the game", Toast.LENGTH_LONG).show();
            }
        });

        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OptionsActivity.this, "In offline mode, this character is Player's 2 character, in online mode, this character will be used in case you connect to the game", Toast.LENGTH_LONG).show();
            }
        });

        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OptionsActivity.this, "This option select the color mode for in-game environment", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doPayment(){
//        Activity mActivity = OptionsActivity.this;
//        mBillingClient = BillingClient.newBuilder(mActivity).setListener(new PurchasesUpdatedListener() {
//            @Override
//            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
//
//            }
//        }).build();
//        mBillingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
//                if (billingResponseCode == BillingClient.BillingResponse.OK) {
//                    // The billing client is ready. You can query purchases here.
//                    List skuList = new ArrayList<>();
//                    skuList.add("donation");
//                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
//                    mBillingClient.querySkuDetailsAsync(params.build(),
//                            new SkuDetailsResponseListener() {
//                                @Override
//                                public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
//                                    // Process the result.
//                                    if (responseCode == BillingClient.BillingResponse.OK
//                                            && skuDetailsList != null) {
//                                    }
//                                }
//                            });
//                }
//            }
//            @Override
//            public void onBillingServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//            }
//        });
    }

}
