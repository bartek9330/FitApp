package com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui.managestep;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.interfaces.Checker;
import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ManageStepFragment extends Fragment implements Checker, BitmapHandler {
    private static final int CAMERA_PIC_REQUEST = 2137;
    private static final int GALLERY_PIC_REQUEST = 997;
    private View root;
    private StepDao stepDao;
    private TextInputLayout textInputLayoutStepName;
    private TextInputLayout textInputLayoutStepDescription;
    private TextInputLayout textInputLayoutStepNumber;
    private Button buttonCreateNewStep;
    private Button buttonDeleteStep;

    private boolean isEdited = false;
    private ManageStepViewModel viewModel;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getText(R.string.create_new_step));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_manage_step, container, false);
        this.progressDialog = new ProgressDialog(root.getContext());
        setUpView();
        viewModel =
                new ViewModelProvider(this).get(ManageStepViewModel.class);

        Object objectStep = getArguments().getSerializable("stepDao");
        Object objectCourse = getArguments().getSerializable("courseDao");

        try {
            if (objectStep != null) {
                this.prepareViewForUpdate(objectStep);
            } else if (objectCourse != null) {
                this.prepareViewForNewStep(objectCourse);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            this.backToCourseManager();
        }

        getButtonStepImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOpenGallery();
            }
        });

        getButtonStepImage().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handleOpenCamera();
                return false;
            }
        });

        getButtonCreateNewStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickButton();
            }
        });
        return root;
    }

    private void prepareViewForNewStep(Object objectCourse) {
        StepDao newStepDao = new StepDao();
        CourseDao courseDao = (CourseDao) objectCourse;
        newStepDao.setCourseId(courseDao.getId());
        setStepDao(newStepDao);
        isEdited = false;
        getButtonDeleteStep().setVisibility(View.INVISIBLE);
    }

    private void prepareViewForUpdate(Object objectStep) {
        prepareDeleteButton();
        setStepDao((StepDao) objectStep);
        isEdited = true;
        setUpDataInView(getStepDao());
        getButtonCreateNewStep().setText(getText(R.string.update_this_step));
    }

    private void prepareDeleteButton() {
        getButtonDeleteStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeleteStepOnServer();
            }
        });

    }

    private void handleDeleteStepOnServer() {
        LiveData<ServerResponse> serverResponse = viewModel.deleteStepFromServer(getStepDao());
        if (!serverResponse.hasActiveObservers()) {
            enableLoadingBar();
            serverResponse.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse response) {
                    disableLoadingBar();
                    if (response != null) {
                        handleSuccessLoadingData();
                    } else {
                        handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    private void handleClickButton() {
        String stepName = checkValueInTextInputLayout(getTextInputLayoutStepName(), root);
        String stepDescription = this.checkValueInTextInputLayout(getTextInputLayoutStepDescription(), root);
        String stepNumber = this.checkValueInTextInputLayout(getTextInputLayoutStepNumber(), root);
        String image = this.checkImageButton(getStepDao().getImage());
        if (stepName != null && stepDescription != null && stepNumber != null && image != null) {
            getStepDao().setDescription(stepDescription);
            getStepDao().setName(stepName);
            getStepDao().setStepNumber(Integer.parseInt(stepNumber));
            if (isEdited) {
                handleStepOnServer(true);
            } else {
                handleStepOnServer(false);
            }
        }
    }

    private String checkImageButton(String image) {
        if (image == null) {
            Toast.makeText(root.getContext(), getText(R.string.photo_is_null), Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return image;
        }
    }

    private void handleStepOnServer(boolean isUpdate) {
        LiveData<ServerResponse> serverResponse = null;
        if (isUpdate) {
            serverResponse = viewModel.updateStepOnServer(getStepDao());
        } else {
            serverResponse = viewModel.createStepOnServer(getStepDao());
        }
        if (!serverResponse.hasActiveObservers()) {
            enableLoadingBar();
            serverResponse.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse response) {
                    disableLoadingBar();
                    if (response != null) {
                        handleSuccessLoadingData();
                    } else {
                        handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    private void handleSuccessLoadingData() {
        Toast.makeText(root.getContext(), getText(R.string.step_updated), Toast.LENGTH_SHORT).show();
        setStepDao(null);
        this.backToCourseManager();
    }

    private void backToCourseManager() {
        getActivity().onBackPressed();
    }

    private void handleErrorWhileLoadingData() {
        Toast.makeText(root.getContext(), getText(R.string.step_update_fail), Toast.LENGTH_SHORT).show();
    }

    private void setUpDataInView(StepDao stepDao) {
        getTextInputLayoutStepName().getEditText().setText(stepDao.getName());
        getTextInputLayoutStepDescription().getEditText().setText(stepDao.getDescription());
        getTextInputLayoutStepNumber().getEditText().setText(String.valueOf(stepDao.getStepNumber()));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(stepDao.getName());
        prepareImageButton(stepDao);
    }

    private void prepareImageButton(StepDao stepDao) {
        try {
            getButtonStepImage().setImageBitmap(getImageFromBase64(stepDao.getImage()));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public TextInputLayout getTextInputLayoutStepName() {
        return textInputLayoutStepName;
    }

    public void setTextInputLayoutStepName(TextInputLayout textInputLayoutStepName) {
        this.textInputLayoutStepName = textInputLayoutStepName;
    }

    public TextInputLayout getTextInputLayoutStepDescription() {
        return textInputLayoutStepDescription;
    }

    public void setTextInputLayoutStepDescription(TextInputLayout textInputLayoutStepDescription) {
        this.textInputLayoutStepDescription = textInputLayoutStepDescription;
    }

    public TextInputLayout getTextInputLayoutStepNumber() {
        return textInputLayoutStepNumber;
    }

    public void setTextInputLayoutStepNumber(TextInputLayout textInputLayoutStepNumber) {
        this.textInputLayoutStepNumber = textInputLayoutStepNumber;
    }

    public Button getButtonDeleteStep() {
        return buttonDeleteStep;
    }

    public void setButtonDeleteStep(Button buttonDeleteStep) {
        this.buttonDeleteStep = buttonDeleteStep;
    }

    private void setUpView() {
        setTextInputLayoutStepName(root.findViewById(R.id.textinputlayout_admin_step_name));
        setTextInputLayoutStepDescription(root.findViewById(R.id.textinputlayout_admin_step_description));
        setTextInputLayoutStepNumber(root.findViewById(R.id.textinputlayout_admin_step_number));
        setButtonCreateNewStep(root.findViewById(R.id.button_admin_create_step));
        setButtonStepImage(root.findViewById(R.id.step_image_button));
        setButtonDeleteStep(root.findViewById(R.id.button_admin_delete_step));
    }

    private void handleOpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select img"), GALLERY_PIC_REQUEST);
    }

    private void handleOpenCamera() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            getButtonStepImage().setImageBitmap(image);
            convertImageToBase(image);
        } else if (requestCode == GALLERY_PIC_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                ContentResolver contentResolver = this.getContext().getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                getButtonStepImage().setImageBitmap(bitmap);
                convertImageToBase(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertImageToBase(Bitmap image) {
        getStepDao().setImage(getBase64FromImage(image));
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

    public ImageButton getButtonStepImage() {
        return buttonStepImage;
    }

    public void setButtonStepImage(ImageButton buttonStepImage) {
        this.buttonStepImage = buttonStepImage;
    }

    private ImageButton buttonStepImage;

    public Button getButtonCreateNewStep() {
        return buttonCreateNewStep;
    }

    public void setButtonCreateNewStep(Button buttonCreateNewStep) {
        this.buttonCreateNewStep = buttonCreateNewStep;
    }

    public ManageStepFragment() {
    }

    public StepDao getStepDao() {
        return stepDao;
    }

    public void setStepDao(StepDao stepDao) {
        this.stepDao = stepDao;
    }

}

