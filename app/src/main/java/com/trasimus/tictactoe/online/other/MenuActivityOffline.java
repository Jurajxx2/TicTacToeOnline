package com.trasimus.tictactoe.online.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.friends.FriendsActivity;
import com.trasimus.tictactoe.online.game.GameSizeChoose;
import com.trasimus.tictactoe.online.game.GameSizeChooseLocal;

public class MenuActivityOffline extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_offline);

        mAdapter = new ArrayAdapter<String>(this, R.layout.menu_item, R.id.menuItem);
        ListView menu = (ListView) findViewById(R.id.offlineMenu);
        menu.setAdapter(mAdapter);
        mAdapter.add("Play with friend");
        mAdapter.add("Options");
        mAdapter.add("Go to login");

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                            Intent intent = new Intent(MenuActivityOffline.this, GameSizeChooseLocal.class);
                            startActivity(intent);
                            break;

                    case 1:
                        Intent intent5 = new Intent(MenuActivityOffline.this, OptionsActivity.class);
                        startActivity(intent5);
                        break;
                    case 2:

                        Intent intent2 = new Intent(MenuActivityOffline.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
