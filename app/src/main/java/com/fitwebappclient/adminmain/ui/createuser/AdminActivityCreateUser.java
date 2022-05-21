package com.fitwebappclient.adminmain.ui.createuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.fitwebappclient.interfaces.Checker;
import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.models.UserRoleDaoShort;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class AdminActivityCreateUser extends AppCompatActivity implements Checker {
    private AdminActivityCreateUserViewModel adminActivityCreateUserViewModel;
    private ProgressDialog progressDialog;
    private TextInputLayout userLoginLayout;
    private TextInputLayout userEmailLayout;
    private TextInputLayout userPasswordLayout;
    private CheckBox isAdministratorCheckBox;
    private View root;

    public View getRoot() {
        return root;
    }

    public void setRoot(View view) {
        this.root = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_user);
        setRoot(findViewById(android.R.id.content).getRootView());
        setTitle(getResources().getString(R.string.admin_add_new_user));
            adminActivityCreateUserViewModel =
                    new ViewModelProvider(this).get(AdminActivityCreateUserViewModel.class);

        this.progressDialog = new ProgressDialog(this);

        setUpView();


        Button randomPasswordButton = findViewById(R.id.admin_add_user_random_password);
        randomPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRandomPassword();
            }
        });


        Button registerButton = findViewById(R.id.admin_add_user_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    handleRegisterActionButton();
                }catch (Exception exception){
                    System.out.println(exception);
                }
            }
        });
    }

    private void setUpView() {
        isAdministratorCheckBox = findViewById(R.id.admin_add_user_is_admin_checkbox);
        userLoginLayout = findViewById(R.id.textinputlayout_admin_register_user_login);
        userEmailLayout = findViewById(R.id.textinputlayout_admin_register_user_email);
        userPasswordLayout = findViewById(R.id.textinputlayout_admin_register_user_password);
    }

    private void handleRandomPassword() {
        String randomPassword = getRandomPassword();
        userPasswordLayout.getEditText().setText(randomPassword);
    }

    private String getRandomPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    private void handleRegisterActionButton() {
        String userLogin = checkValueInTextInputLayout(userLoginLayout,getRoot());
        String userEmail = checkValueInTextInputLayout(userEmailLayout,getRoot());
        String userPassword = checkValueInTextInputLayout(userPasswordLayout,getRoot());

        if (userLogin != null && userPassword != null && userEmail != null) {
            UserDaoShort userDaoShort = new UserDaoShort();
            userDaoShort.setEmail(userEmail);
            userDaoShort.setLogin(userLogin);
            if(isAdministratorCheckBox.isChecked()){
                userDaoShort.setRoleId(getRoleByName("Administrator"));
            }else{
                userDaoShort.setRoleId(getRoleByName("User"));
            }
            userDaoShort.setPassword(userPassword);
            registerNewUser(userDaoShort);
        }
    }

    private int getRoleByName(String roleName) {
        return UserRoleDaoShort.fromName(roleName);
    }

    private void registerNewUser(UserDaoShort userDaoShort) {
        adminActivityCreateUserViewModel.registerUser(userDaoShort);
        LiveData<Boolean> status = adminActivityCreateUserViewModel.getStatus();
        if (!status.hasActiveObservers()) {
            enableLoadingBar();
            status.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean hasChange) {
                    if(hasChange){
                        disableLoadingBar();
                        if(adminActivityCreateUserViewModel.getStatus().getValue()){
                            finish();
                        }
                    }
                }
            });
        }
    }

    public void disableLoadingBar() {
        progressDialog.dismiss();
    }

    public void enableLoadingBar() {
        progressDialog.setTitle(getResources().getString(R.string.progress_dialog_title));
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog_describe));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


}