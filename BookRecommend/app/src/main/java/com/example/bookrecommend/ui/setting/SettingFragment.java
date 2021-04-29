package com.example.bookrecommend.ui.setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookrecommend.constant.ConstVariable;
import com.example.bookrecommend.constant.Tools;
import com.example.bookrecommend.databinding.FragmentSettingBinding;
import com.example.bookrecommend.db.DBUtil;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.ImageURL;
import com.example.bookrecommend.pojo.UserInfo;
import com.example.bookrecommend.service.SaveFileService;
import com.example.bookrecommend.service.UserService;
import com.example.bookrecommend.sington.HttpStatus;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.SingleObject;

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

public class SettingFragment extends Fragment implements ActivityResultCaller {

    private FragmentSettingBinding binding;

    // 每次上传图像文件后路径
    private String imageURL;

    private String currentPhotoPath;

    private EditText input;

    private String m_Text = "";

    // 每次从缓冲字节数组中读取额字节数
    private int len = 0;

    // 将输入的字节流缓冲到这个数组，按照一般的 OS 的文件块大小，4KB 是最合适的
    private final byte[] buffer = new byte[4096];

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
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

                Log.d("ImageUri", imageUri.toString());

                File file = generateImageFile();
                try {
                    final InputStream inputStream = requireContext()
                            .getContentResolver().openInputStream(imageUri);
                    OutputStream out = new FileOutputStream(file);
                    while ((len = inputStream.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }

                    assert file != null;
                    updateImageURL(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    );

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        input = new EditText(requireContext());

        UserInfoEntity obj = SingleObject.getUserInfoEntity();
        // 加载用户的头像到对应的 ImageView 元素中
        Glide.with(requireContext())
                .load(obj.getHeadImage())
                .into(binding.userHeadImage);

        binding.settingUserName.setText(obj.getUserName());

        if (0 != obj.getUserPhone().trim().length())
            binding.settingUserAccount.setText(obj.getUserPhone());
        else
            binding.settingUserAccount.setText(obj.getUserEmail());

        binding.userHeadImage.setOnClickListener(v -> selectImage());

        binding.settingUserName.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("修改姓名");
            // Set up the input
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                m_Text = input.getText().toString();
                UserService userService = ServiceSingle.getUserService();

                Call<HttpStatus> userInfoByName = userService.findUserInfoByName(m_Text);
                userInfoByName.enqueue(new Callback<HttpStatus>() {
                    @Override
                    public void onResponse(@NotNull Call<HttpStatus> call,
                                           @NotNull Response<HttpStatus> response) {
                        if (!response.isSuccessful()) {
                            Log.d("UpdateUserName", "Update User Name Failed");
                            Toast.makeText(requireContext(), "更新用户姓名失败", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (HttpStatus.ACCEPTED == response.body()) {
                            Log.d("UpdateUserName", "User Name Has Been Exist");
                            Toast.makeText(requireContext(), "该用户名已经存在", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UserInfoEntity entity = SingleObject.getUserInfoEntity();
                        entity.setUserName(m_Text);
                        entity.setIsUpdate((short) 1);
                        DBUtil.saveUserInfo(requireContext(), entity);

                        UserInfo userInfo = Tools.convertToUserInfo(entity);

                        UserService service = ServiceSingle.getUserService();
                        Call<HttpStatus> update = service.update(userInfo);

                        update.enqueue(new Callback<HttpStatus>() {
                            @Override
                            public void onResponse(@NotNull Call<HttpStatus> call,
                                                   @NotNull Response<HttpStatus> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("UpdateUserName", "Update User Name Failed");
                                    Toast.makeText(requireContext(), "更新用户姓名失败", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Toast.makeText(requireContext(), "更新用户姓名成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NotNull Call<HttpStatus> call,
                                                  @NotNull Throwable t) {
                                Log.d("UpdateUserName", Objects.requireNonNull(t.getLocalizedMessage()));
                                Toast.makeText(requireContext(), "更新用户姓名失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull Call<HttpStatus> call,
                                          @NotNull Throwable t) {
                    }
                });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        });

        binding.settingThroughRecord.setOnClickListener(v -> {
            NavDirections action =
                    SettingFragmentDirections
                            .actionNavigationSettingToThroughRecordFragment(SingleObject
                                    .getUserInfoEntity().getUserId());

            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.settingStarRecord.setOnClickListener(v -> {
            NavDirections action =
                    SettingFragmentDirections
                            .actionNavigationSettingToHasBeenStarFragment(obj.getUserId());

            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.settingCollected.setOnClickListener(v -> {
            NavDirections action =
                    SettingFragmentDirections
                            .actionNavigationSettingToHasBeenCollectedFragment(obj.getUserId());

            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.settingCommented.setOnClickListener(v -> {
            NavDirections action =
                    SettingFragmentDirections
                            .actionNavigationSettingToHasBeenCommentedFragment(obj.getUserId());

            Navigation.findNavController(requireView()).navigate(action);
        });

        return binding.getRoot();
    }

    private void updateImageURL(@NotNull File file) {
        // 上传的文件对象
        RequestBody requestFile = RequestBody
                .create(file, MediaType.parse("image/*"));
        UserInfoEntity obj = SingleObject.getUserInfoEntity();

        // 用户 ID 请求体对象
        RequestBody userId = RequestBody
                .create(String.valueOf(obj.getUserId()),
                        MediaType.parse("multipart/form-data"));

        // 请求体文件对象
        MultipartBody.Part body = MultipartBody.Part.createFormData(ConstVariable.IMAGE_FILE_STRING,
                file.getName(), requestFile);

        SaveFileService saveFileService = ServiceSingle.getSaveFileService();

        Call<ImageURL> call = saveFileService.uploadImage(body, userId);

        call.enqueue(new Callback<ImageURL>() {
            @Override
            public void onResponse(@NotNull Call<ImageURL> call,
                                   @NotNull Response<ImageURL> response) {
                if (!response.isSuccessful()) {
                    Log.d("ResponseResult", "Failed");
                    return;
                }

                ImageURL obj = response.body();
                assert obj != null;
                imageURL = obj.getImageUrl();

                assert imageURL != null;
                Log.d("UploadImage", imageURL);

                UserInfoEntity userInfoEntity = SingleObject.getUserInfoEntity();
                userInfoEntity.setHeadImage(imageURL);
                userInfoEntity.setIsUpdate((short) 1);

                Call<HttpStatus> updateCall = ServiceSingle
                        .getUserService()
                        .update(Tools.convertToUserInfo(userInfoEntity));

                updateCall.enqueue(new Callback<HttpStatus>() {
                    @Override
                    public void onResponse(@NotNull Call<HttpStatus> call,
                                           @NotNull Response<HttpStatus> response) {
                        if (!response.isSuccessful()) {
                            Log.d("UpdateUserInfo", "Update UserInfo failed");
                            return;
                        }

                        if (HttpStatus.OK == response.body()) {
                            Log.d("UpdateUserInfo", "Update UserInfo successful");

                            Log.d("UpdateUserIfo", Tools.convertToUserInfo(userInfoEntity).toString());

                            Toast.makeText(requireContext(), "更新头像成功", Toast.LENGTH_SHORT).show();

                            DBUtil.saveUserInfo(requireContext(), userInfoEntity);

                            return;
                        }

                        Log.d("UpdateUserInfo", "Update UserInfo accept");
                    }

                    @Override
                    public void onFailure(@NotNull Call<HttpStatus> call,
                                          @NotNull Throwable t) {
                        Log.d("UpdateUserInfo", Objects.requireNonNull(t.getLocalizedMessage()));
                    }
                });

                Glide.with(requireView()).load(imageURL).into(binding.userHeadImage);
            }

            @Override
            public void onFailure(@NotNull Call<ImageURL> call,
                                  @NotNull Throwable t) {
                Log.d("UploadImage", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

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

    private void selectImage() {
        final CharSequence[] options = {"拍照获取", "从已有照片中获取", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("更换头像");
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

    @SuppressLint({"QueryPermissionsNeeded", "ShowToast"})
    private void dispatchTakePictureIntent() {
        Log.d("FragmentExternalDir", requireContext().getExternalFilesDir(null).toString());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null != takePictureIntent.resolveActivity(
                requireActivity().getPackageManager()
        )
        ) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TakePhoto", "Get Photo file is null");
            }

            assert photoFile != null;
            Uri photoUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.bookrecommend.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            mGetContent.launch(takePictureIntent);
        } else {
            Toast.makeText(requireView().getContext(), "Load Camera failed", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @SuppressLint({"QueryPermissionsNeeded", "ShowToast"})
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (null != intent.resolveActivity(requireActivity().getPackageManager())) {
            mFromPhoto.launch(intent);
        } else {
            Toast.makeText(requireView().getContext(), "Pick Image failed", Toast.LENGTH_SHORT);
        }
    }
}