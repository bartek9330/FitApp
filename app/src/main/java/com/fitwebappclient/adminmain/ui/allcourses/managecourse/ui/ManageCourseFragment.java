package com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui;

import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fitwebappclient.adminmain.models.ServerResponseMessages;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ManageCourseActivity;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ManageCourseFragment extends Fragment implements Checker, BitmapHandler {
    private static final int CAMERA_PIC_REQUEST = 2137;
    private static final int GALLERY_PIC_REQUEST = 997;
    private CourseDao courseDao;
    private ProgressDialog progressDialog;
    private TextInputLayout courseNameTextInputLayout;
    private TextInputLayout courseDescriptionTextInputLayout;
    private ImageButton courseImageButton;
    private RecyclerView recyclerView;
    private CourseStepsCustomAdapter courseStepsCustomAdapter;
    private View root;
    private ManageCourseViewModel viewModel;
    private FloatingActionButton floatingActionButtonAddNewStep;
    private ExtendedFloatingActionButton floatingActionButtonUpdateCourse;
    private DialogsHandling dialogsHandling;

    public static ManageCourseFragment newInstance() {
        return new ManageCourseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_manage_course, container, false);
        dialogsHandling = new DialogsHandling(root);
        viewModel = new ViewModelProvider(this).get(ManageCourseViewModel.class);
        viewModel.setManageCourseFragment(this);
        this.progressDialog = new ProgressDialog(root.getContext());

        prepareView();
        setUpLocalData();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public TextInputLayout getCourseNameTextInputLayout() {
        return courseNameTextInputLayout;
    }

    public void setCourseNameTextInputLayout(TextInputLayout courseNameTextInputLayout) {
        this.courseNameTextInputLayout = courseNameTextInputLayout;
    }

    public TextInputLayout getCourseDescriptionTextInputLayout() {
        return courseDescriptionTextInputLayout;
    }

    public void setCourseDescriptionTextInputLayout(TextInputLayout courseDescriptionTextInputLayout) {
        this.courseDescriptionTextInputLayout = courseDescriptionTextInputLayout;
    }

    public ImageButton getCourseImageButton() {
        return courseImageButton;
    }

    public void setCourseImageButton(ImageButton courseImageButton) {
        this.courseImageButton = courseImageButton;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public FloatingActionButton getFloatingActionButtonAddNewStep() {
        return floatingActionButtonAddNewStep;
    }

    public void setFloatingActionButtonAddNewStep(FloatingActionButton floatingActionButtonAddNewStep) {
        this.floatingActionButtonAddNewStep = floatingActionButtonAddNewStep;
    }

    public ExtendedFloatingActionButton getFloatingActionButtonUpdateCourse() {
        return floatingActionButtonUpdateCourse;
    }

    public void setFloatingActionButtonUpdateCourse(ExtendedFloatingActionButton floatingActionButtonUpdateCourse) {
        this.floatingActionButtonUpdateCourse = floatingActionButtonUpdateCourse;
    }

    private void prepareView() {
        this.setCourseNameTextInputLayout(root.findViewById(R.id.textinputlayout_admin_course_name));
        this.setCourseDescriptionTextInputLayout(root.findViewById(R.id.textinputlayout_admin_course_description));
        this.setCourseImageButton(root.findViewById(R.id.course_image_button));
        this.setRecyclerView(root.findViewById(R.id.recyclerview_admin_steps));
        this.setFloatingActionButtonAddNewStep(root.findViewById(R.id.admin_add_new_step_floatingbutton));
        this.setFloatingActionButtonUpdateCourse(root.findViewById(R.id.admin_update_course_floatingbutton));

        this.getFloatingActionButtonAddNewStep().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.openStepActivity(null, getCourseDao());
            }
        });

        this.getCourseImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOpenGallery();
            }
        });

        this.getCourseImageButton().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handleOpenCamera();
                return false;
            }
        });
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
            setUpBitmapData(image);
        } else if (requestCode == GALLERY_PIC_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                ContentResolver contentResolver = this.getContext().getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                setUpBitmapData(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpBitmapData(Bitmap image) {
        getCourseImageButton().setImageBitmap(image);
        getCourseDao().setImage(getBase64FromImage(image));
    }


    private void setUpLocalData() {
        if (ManageCourseActivity.courseDao != null) {
            setCourseDao(ManageCourseActivity.courseDao);
            setUpDataFromServer();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(courseDao.getName());
            handleOptionalUpdateCourse();
        } else {
            setCourseDao(new CourseDao());
            prepareCreateNewCourse();
        }
    }

    private void handleOptionalUpdateCourse() {
        getFloatingActionButtonUpdateCourse().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCourse(true);
            }
        });
    }

    private void prepareCreateNewCourse() {
        ViewGroup parent = ((ViewGroup) getFloatingActionButtonAddNewStep().getParent());
        parent.removeView(getFloatingActionButtonAddNewStep());
        parent.removeView(getFloatingActionButtonUpdateCourse());
        ViewGroup children = ((ViewGroup) getRecyclerView().getParent());
        children.removeView(getRecyclerView());

        Button addNewCourseButton = this.getNewCourseButton();
        children.addView(addNewCourseButton);

        handleAddNewCourseButton(addNewCourseButton);
    }

    private void handleAddNewCourseButton(Button addNewCourseButton) {
        addNewCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCourse(false);
            }
        });
    }

    private void handleCourse(boolean isUpdate) {
        String courseName = checkValueInTextInputLayout(getCourseNameTextInputLayout(), root);
        String courseDescription = checkValueInTextInputLayout(getCourseDescriptionTextInputLayout(), root);
        String courseImage = getCourseDao().getImage();

        if (courseImage == null) {
            handleCourseImageIsEmpty();
        }

        if (courseName != null && courseDescription != null && courseImage != null) {
            getCourseDao().setDescription(courseDescription);
            getCourseDao().setName(courseName);
            if (isUpdate) {
                handleUpdateCourse(getCourseDao());
            } else {
                handleCreateNewCourse(getCourseDao());
            }
        }
    }

    private void handleUpdateCourse(CourseDao courseDao) {
        LiveData<ServerResponse> response = viewModel.updateCourseOnServer(courseDao);
        if (!response.hasActiveObservers()) {
            response.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse != null) {
                        try {
                            if (serverResponse.getMessage() != null) {
                                Toast.makeText(root.getContext(), getText(R.string.course_updated_successfully), Toast.LENGTH_SHORT).show();
                                backToAllCourses();
                            } else {
                                dialogsHandling.handleErrorWhileLoadingData();
                            }
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    private void handleCreateNewCourse(CourseDao courseDao) {
        LiveData<ServerResponse> response = viewModel.createNewCourseOnServer(courseDao);
        if (!response.hasActiveObservers()) {
            response.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse.getMessage() != null) {
                        try {
                            if (serverResponse.getMessage().equals(ServerResponseMessages.OK.getResponse())) {
                                backToAllCourses();
                            } else if (serverResponse.getMessage().equals(ServerResponseMessages.EXIST.getResponse())) {
                                handleCourseExist();
                            } else {
                                dialogsHandling.handleErrorWhileLoadingData();
                            }
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                    }
                }
            });
        }
    }

    private void handleCourseExist() {
        Toast.makeText(root.getContext(), getText(R.string.course_is_exist), Toast.LENGTH_SHORT).show();
    }

    private void backToAllCourses() {
        getActivity().finish();
    }

    private void handleCourseImageIsEmpty() {
        Toast.makeText(root.getContext(), getText(R.string.photo_is_null), Toast.LENGTH_SHORT).show();
    }

    private Button getNewCourseButton() {
        Button button = new Button(root.getContext());
        button.setText(getText(R.string.add_new_course));
        return button;
    }

    private void setUpDataFromServer() {
        viewModel.getDataFromServer(getCourseDao());
        LiveData<CourseDao> courseData = viewModel.getCourseData();
        dialogsHandling.enableLoadingBar();
        if (!courseData.hasActiveObservers()) {
            courseData.observe(getViewLifecycleOwner(), new Observer<CourseDao>() {
                @Override
                public void onChanged(CourseDao courseDao) {
                    dialogsHandling.disableLoadingBar();
                    CourseDao data = viewModel.getCourseData().getValue();
                    if (data != null) {
                        setUpView(data);
                        getCourseDao().setImage(data.getImage());
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }



    private void setUpView(CourseDao data) {
        this.getCourseNameTextInputLayout().getEditText().setText(data.getName());
        this.getCourseDescriptionTextInputLayout().getEditText().setText(data.getDescription());
        this.prepareRecyclerView();
        this.getCourseImageButton().setImageBitmap(getImageFromBase64(data.getImage()));
    }

    private void prepareRecyclerView() {
        courseStepsCustomAdapter = new CourseStepsCustomAdapter(viewModel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(512);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(courseStepsCustomAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);
    }


    public CourseDao getCourseDao() {
        return courseDao;
    }

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;

    }

    private void handleDeleteStepOnServer(StepDao stepDao) {
        dialogsHandling.enableLoadingBar();
        try {
            LiveData<ServerResponse> serverResponse = viewModel.deleteStepFromServer(stepDao);
            if (!serverResponse.hasActiveObservers()) {
                serverResponse.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                    @Override
                    public void onChanged(ServerResponse response) {
                        dialogsHandling.disableLoadingBar();
                        if (response != null) {
                            handleSuccessLoadingData();
                        } else {
                            dialogsHandling.handleErrorWhileLoadingData();
                        }
                    }
                });
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void handleSuccessLoadingData() {
        setUpDataFromServer();
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            handleDeleteStepOnServer(viewModel.getCourseData().getValue().getSteps()[position]);
        }
    };

}



