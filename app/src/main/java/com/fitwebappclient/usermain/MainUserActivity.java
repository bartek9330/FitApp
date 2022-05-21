package com.fitwebappclient.usermain;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.databinding.ActivityMainUserBinding;
import com.fitwebappclient.loginandregistration.models.UserData;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainUserActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainUserBinding binding;
    private static UserData userData;

    public static void setUserData(UserData userData) {
        MainUserActivity.userData = userData;
    }
    public static UserData getUserData() {
        return userData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUserData((UserData) getIntent().getSerializableExtra("userData"));

        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNormal.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_all_available_courses, R.id.nav_my_courses, R.id.nav_bmi_calculator,R.id.nav_bmi_analyzer)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_normal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerLayout = navigationView.getHeaderView(0);

        if(userData != null){
            setUpAdminHeader(headerLayout);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_user, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id ){
            case R.id.action_log_out_user:
                logUserOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logUserOut() {
        MainUserActivity.userData = null;
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_normal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("SetTextI18n")
    private void setUpAdminHeader(View headerLayout) {
        TextView headerUserLogin = headerLayout.findViewById(R.id.nav_header_main_user_title);
        headerUserLogin.setText(getText(R.string.say_hello) + " " + getUserData().getLogin());
    }
}