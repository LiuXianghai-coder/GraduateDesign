package com.example.bookrecommend.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.R;
import com.example.bookrecommend.constant.ConstVariable;
import com.example.bookrecommend.databinding.FragmentCreateBookReviewBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.ImageURL;
import com.example.bookrecommend.pojo.UserBookReview;
import com.example.bookrecommend.service.SaveFileService;
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

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateBookReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBookReviewFragment
        extends
            Fragment
        implements
            ActivityResultCaller {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String imageURL;

    private FragmentCreateBookReviewBinding binding;

    private RichEditor mEditor;

    private String currentPhotoPath;

    private String reviewContent;

    private long userId;

    private long isbn;

    private int len = 0;

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

    public CreateBookReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateBookReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateBookReviewFragment newInstance(String param1, String param2) {
        CreateBookReviewFragment fragment = new CreateBookReviewFragment();
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
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateBookReviewBinding
                .inflate(inflater, container, false);

        userId = SingleObject.getUserInfoEntity().getUserId();

        mEditor = binding.editor;

        assert getArguments() != null;
        isbn = getArguments().getLong("isbn");

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");

        mEditor.setOnTextChangeListener(text -> {
            reviewContent = text;
            Log.d("ReviewContent", reviewContent);
        });

        binding.actionUndo.setOnClickListener(v -> mEditor.undo());

        binding.actionRedo.setOnClickListener(v -> mEditor.redo());

        binding.actionBold.setOnClickListener(v -> mEditor.setBold());

        binding.actionItalic.setOnClickListener(v -> mEditor.setItalic());

        binding.actionSubscript.setOnClickListener(v -> mEditor.setSubscript());

        binding.actionSuperscript.setOnClickListener(v -> mEditor.setSuperscript());

        binding.actionStrikethrough.setOnClickListener(v -> mEditor.setStrikeThrough());

        binding.actionUnderline.setOnClickListener(v -> mEditor.setUnderline());

        binding.actionHeading1.setOnClickListener(v -> mEditor.setHeading(1));

        binding.actionHeading2.setOnClickListener(v -> mEditor.setHeading(2));

        binding.actionHeading3.setOnClickListener(v -> mEditor.setHeading(3));

        binding.actionHeading4.setOnClickListener(v -> mEditor.setHeading(4));

        binding.actionHeading5.setOnClickListener(v -> mEditor.setHeading(5));

        binding.actionHeading6.setOnClickListener(v -> mEditor.setHeading(6));

        binding.actionTxtColor.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        binding.actionBgColor.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        binding.actionIndent.setOnClickListener(v -> mEditor.setIndent());

        binding.actionOutdent.setOnClickListener(v -> mEditor.setOutdent());

        binding.actionAlignLeft.setOnClickListener(v -> mEditor.setAlignLeft());

        binding.actionAlignCenter.setOnClickListener(v -> mEditor.setAlignCenter());

        binding.actionAlignRight.setOnClickListener(v -> mEditor.setAlignRight());

        binding.actionBlockquote.setOnClickListener(v -> mEditor.setBlockquote());

        binding.actionInsertBullets.setOnClickListener(v -> mEditor.setBullets());

        binding.actionInsertNumbers.setOnClickListener(v -> mEditor.setNumbers());

        binding.actionInsertImage.setOnClickListener(v -> {
            selectImage();
        });

        binding.actionInsertLink.setOnClickListener(v ->
                mEditor.insertLink("https://www.baidu.com", "baidu"));
        binding.actionInsertCheckbox.setOnClickListener(v -> mEditor.insertTodo());

        binding.uploadCreateBookReviewButton.setOnClickListener(v -> {
            UserBookReview review = new UserBookReview();

            long userId = SingleObject.getUserInfoEntity().getUserId();
            review.setIsbn(isbn);
            review.setUserId(userId);
            review.setContent(reviewContent);

            setBookReviewHeader(review);
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
                mEditor.insertImage(imageURL, "Image", 320);
            }

            @Override
            public void onFailure(@NotNull Call<ImageURL> call,
                                  @NotNull Throwable t) {
                Log.d("UploadImage", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"拍照获取", "从已有照片中获取", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加照片");
        builder.setItems(options, (dialog, item) -> {
            if (0 == item) {
                Log.d("SelectImage", "SelectImage");
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

            Log.i("DispatcherPhoto", "start get photo file");
            assert photoFile != null;
            Uri photoUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.bookrecommend.fileprovider",
                    photoFile);
            Log.i("DispatcherPhoto", "get file success");
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

    private void setBookReviewHeader(UserBookReview userBookReview) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.add_comment_dialog, null);

        builder.setView(dialogView)
                .setPositiveButton(R.string.ok_string, (dialog, which) -> {
                    EditText editText = dialogView.findViewById(R.id.bookCommentInput);

                    userBookReview.setReviewHead(editText.getText().toString());

                    Call<HttpStatus> addReviewCall = ServiceSingle
                            .getBookService().addBookReviews(userBookReview);

                    addReviewCall.enqueue(new Callback<HttpStatus>() {
                        @Override
                        public void onResponse(@NotNull Call<HttpStatus> call,
                                               @NotNull Response<HttpStatus> response) {
                            if (!response.isSuccessful()) {
                                Log.d("AddReview", "Add Book Review Failed.");
                                return;
                            }

                            Log.d("AddReview", "");

                            NavDirections action =
                                    CreateBookReviewFragmentDirections
                                            .actionCreateBookReviewFragmentToBookReviewFragment();
                            Navigation.findNavController(requireView()).navigate(action);
                        }

                        @Override
                        public void onFailure(@NotNull Call<HttpStatus> call,
                                              @NotNull Throwable t) {
                            Log.d("AddBookReview", Objects.requireNonNull(t.getLocalizedMessage()));
                        }
                    });
                })
                .setNegativeButton(R.string.cancel_string, (dialog, which) -> dialog.dismiss())
                .create();
        builder.show();
    }
}