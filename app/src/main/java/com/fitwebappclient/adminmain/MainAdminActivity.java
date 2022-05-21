package com.fitwebappclient.adminmain;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.fitwebappclient.R;
import com.fitwebappclient.databinding.ActivityMainAdminBinding;
import com.fitwebappclient.loginandregistration.models.UserData;
import com.fitwebappclient.usermain.MainUserActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class MainAdminActivity extends AppCompatActivity implements Serializable {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainAdminBinding binding;
    private static UserData userData;

    public static void setUserData(UserData userData) {
        MainAdminActivity.userData = userData;
    }
    public static UserData getUserData() {
        return userData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserData((UserData) getIntent().getSerializableExtra("userData"));
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainUser.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_all_users,
                R.id.nav_admin_all_courses)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerLayout = navigationView.getHeaderView(0);

        if(getUserData() != null){
            setUpAdminHeader(headerLayout);
        }
    }

    private void setUpAdminHeader(View headerLayout) {
        TextView headerUserLogin = headerLayout.findViewById(R.id.admin_header_user_login);
        TextView headerUserEmail = headerLayout.findViewById(R.id.admin_header_user_email);
        headerUserLogin.setText(getUserData().getLogin());
        headerUserEmail.setText(getUserData().getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id ){
            case R.id.action_log_out_admin:
                logUserOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logUserOut() {
        MainAdminActivity.userData = null;
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}