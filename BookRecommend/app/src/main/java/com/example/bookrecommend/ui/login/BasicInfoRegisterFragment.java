package com.example.bookrecommend.ui.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookrecommend.constant.ConstVariable;
import com.example.bookrecommend.databinding.FragmentBasicInfoRegisterBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.ImageURL;
import com.example.bookrecommend.service.SaveFileService;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicInfoRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class BasicInfoRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BasicInfoRegisterFragment() {
        // Required empty public constructor
    }

    private String currentPhotoPath;

    // ?????????????????????????????????
    private String imageURL;

    // ????????????????????????????????????????????????
    private int len = 0;

    // ???????????????????????????????????????????????????????????? OS ?????????????????????4KB ???????????????
    private final byte[] buffer = new byte[4096];

    private FragmentBasicInfoRegisterBinding binding;

    private UserInfoEntity userInfo = new UserInfoEntity();

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                assert data != null;

                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);

                Log.i("ContentUri", contentUri.toString());

                try {
                    updateImageURL(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    );

    private final ActivityResultLauncher<Intent> mFromPhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                assert data != null;
                final Uri imageUri = data.getData();

                File file = generateImageFile();
                try {
                    final InputStream inputStream = requireContext()
                            .getContentResolver().openInputStream(imageUri);
                    OutputStream out = new FileOutputStream(file);
                    while ((len = inputStream.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }

//                    FileUtils.copy(inputStream, new FileOutputStream(file)); // ????????????FileUtils ?????????

                    // ??????????????????????????????????????????????????????
                    assert file != null;
                    updateImageURL(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    );

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicInfoRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicInfoRegisterFragment newInstance(String param1, String param2) {
        BasicInfoRegisterFragment fragment = new BasicInfoRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasicInfoRegisterBinding.inflate(inflater, container, false);

        assert getArguments() != null;
        userInfo = (UserInfoEntity) getArguments().get("userInfo");

        binding.headImage.setOnClickListener(v -> selectImage());

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.userName.getText().toString();
                if (null == userName || 0 == userName.trim().length()) {
                    Toast.makeText(requireContext(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (binding.genderRadioGroup.getCheckedRadioButtonId() == binding.maleRadio.getId()) {
                    userInfo.setUserSex(ConstVariable.MALE_GENDER);
                } else {
                    userInfo.setUserSex(ConstVariable.FEMALE_GENDER);
                }

                userInfo.setUserName(userName);

                // ?????????????????????????????????
                NavDirections action = BasicInfoRegisterFragmentDirections
                        .actionBasicInfoRegisterFragmentToRegisterFeatureFragment(userInfo);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return binding.getRoot();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????? ImageView ??????
     * ?????????????????????????????????????????????
     *
     * @param file ??? ?????????????????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private void updateImageURL(@NotNull File file) {
        // ?????????????????????
        RequestBody requestFile = RequestBody
                .create(file, MediaType.parse("image/*"));

        // ?????? ID ???????????????
        RequestBody userId = RequestBody
                .create(String.valueOf(userInfo.getUserId()),
                        MediaType.parse("multipart/form-data"));

        // ?????????????????????
        MultipartBody.Part body = MultipartBody.Part.createFormData(ConstVariable.IMAGE_FILE_STRING,
                file.getName(), requestFile);

        SaveFileService saveFileService = ServiceSingle.getSaveFileService();

        Call<ImageURL> call = saveFileService.uploadImage(body, userId);

        call.enqueue(new Callback<ImageURL>() {
            @Override
            public void onResponse(@NotNull Call<ImageURL> call,
                                   @NotNull Response<ImageURL> response) {
                if (!response.isSuccessful()) {
                    Log.d("ResponseResult", "Failed Upload Image file.......");
                    return;
                }

                ImageURL obj = response.body();
                assert obj != null;
                imageURL = obj.getImageUrl();

                assert imageURL != null;
                Log.d("UploadHeadImage", imageURL);
                // ?????????????????????
                userInfo.setHeadImage(imageURL);
                // ???????????????????????? ImageView ???
                Glide.with(requireView()).load(imageURL).into(binding.headImage);
            }

            @Override
            public void onFailure(@NotNull Call<ImageURL> call,
                                  @NotNull Throwable t) {
                Log.d("UploadImage", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    /**
     * ??????????????????????????????????????????????????? ??????????????????????????????????????????????????????
     *
     * @return ??? ?????????????????????????????????????????????????????????
     */
    private File generateImageFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }


    /**
     * ?????????????????????????????????????????????????????????????????? ??????????????????????????????
     * ????????????????????? ????????????????????????????????????
     */
    private void selectImage() {
        final CharSequence[] options = {"????????????", "????????????????????????", "??????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("????????????");
        builder.setItems(options, (dialog, item) -> {
            if (0 == item) {
                dispatchTakePictureIntent();
            } else if (1 == item) {
                pickImage();
            } else if (2 == item) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * ??????????????????????????????????????????????????????
     */
    @SuppressLint({"QueryPermissionsNeeded", "ShowToast"})
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null != takePictureIntent.resolveActivity(
                requireActivity().getPackageManager()
        )
        ) {
            File photoFile;

            photoFile = generateImageFile();

            assert photoFile != null;
            Uri photoUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.bookrecommend.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            mGetContent.launch(takePictureIntent);
        } else {
            Toast.makeText(requireView().getContext(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    @SuppressLint({"QueryPermissionsNeeded", "ShowToast"})
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (null != intent.resolveActivity(requireActivity().getPackageManager())) {
            mFromPhoto.launch(intent);
        } else {
            Toast.makeText(requireView().getContext(), "??????????????????????????????", Toast.LENGTH_SHORT);
        }
    }
}