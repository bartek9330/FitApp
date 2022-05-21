package com.fitwebappclient.loginandregistration;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.fitwebappclient.R;

public class MainLoginActivity extends AppCompatActivity  {
    private AppBarConfiguration appBarConfiguration;
    public static String serverIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverIp = this.getResources().getString(R.string.server_ip);
        setContentView(R.layout.main_login_activity);

//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setReorderingAllowed(true);
//        transaction.replace(R.id.main_activity_fragment, LoginPageFragment.class, null);
//        transaction.commit();

        NavController navController = Navigation.findNavController(this, R.id.main_activity_login_page);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

}