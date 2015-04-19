package zhaohg.post;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import zhaohg.api.ApiErrno;
import zhaohg.api.image.UploadImage;
import zhaohg.api.image.UploadImagePostEvent;
import zhaohg.api.post.NewPost;
import zhaohg.api.post.NewPostPostEvent;
import zhaohg.api.post.Post;
import zhaohg.api.post.UpdatePost;
import zhaohg.api.post.UpdatePostPostEvent;
import zhaohg.main.R;
import zhaohg.testable.TestableActionBarActivity;

public class EditPostActivity extends TestableActionBarActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";
    public static final String EXTRA_POST_TYPE = "EXTRA_POST_TYPE";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final int WIDTH=200;
    public static final int HEIGHT=150;

    private EditText editTitle;
    private EditText editDescription;
    private Button postButton;
    private TextView textError;

    private String postId = "";
    private int postType = Post.POST_TYPE_SELL;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private ImageView addButton;
    private Bitmap bmpThumbnails;
    private Context context;
    private String thumbnailPath;
    private Uri selectedImageUri;
    private int imageNum = 1;
    private LinearLayout linearLayoutPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.editTitle = (EditText) findViewById(R.id.edit_title);
        this.editDescription = (EditText) findViewById(R.id.edit_description);
        this.textError = (TextView) findViewById(R.id.text_error);

        this.postButton = (Button) findViewById(R.id.button_post);
        this.postButton.setOnClickListener(new OnPostButtonClickListener());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.postId = extras.getString(EXTRA_POST_ID, "");
            this.postType = extras.getInt(EXTRA_POST_TYPE, Post.POST_TYPE_SELL);
            this.editTitle.setText(extras.getString(EXTRA_TITLE, ""));
            this.editDescription.setText(extras.getString(EXTRA_DESCRIPTION, ""));
        }

        this.addButton = (ImageView) findViewById(R.id.button_select_image);
        this.addButton.setOnClickListener(new OnAddButtonClickListener());



    }

    private void hideErrorMessage() {
        this.textError.setVisibility(View.GONE);
        this.textError.setText("");
    }

    private void showErrorMessage(String text) {
        this.textError.setVisibility(View.VISIBLE);
        this.textError.setText(text);
    }

    private class OnPostButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            hideErrorMessage();
            final Context context = v.getContext();
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            if (title.isEmpty() || description.isEmpty()) {
                showErrorMessage(context.getString(R.string.error_field_required));
                finishTest();
                return;
            }
            postButton.setEnabled(false);
            if (postId.isEmpty()) {
                // Create new post.
                NewPost newPost = new NewPost(context);
                newPost.setParameter(postType, title, description, new ArrayList<String>());
                newPost.setEvent(new NewPostPostEvent() {
                    @Override
                    public void onSuccess(String postId) {
                        Intent intent = new Intent(context, PostActivity.class);
                        intent.putExtra(PostActivity.EXTRA_POST_ID, postId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finishTest();
                        finish();
                    }
                    @Override
                    public void onFailure(int errno) {
                        postButton.setEnabled(true);
                        showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                        finishTest();
                    }
                });
                newPost.request();
            } else {
                // This is an update.
                UpdatePost updatePost = new UpdatePost(context);
                updatePost.setParameter(postId);
                updatePost.setUpdateTitle(editTitle.getText().toString());
                updatePost.setUpdateDescription(editDescription.getText().toString());
                updatePost.setEvent(new UpdatePostPostEvent() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(context, PostActivity.class);
                        intent.putExtra(PostActivity.EXTRA_POST_ID, postId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finishTest();
                        finish();
                    }
                    @Override
                    public void onFailure(int errno) {
                        postButton.setEnabled(true);
                        showErrorMessage(ApiErrno.getErrorMessage(context, errno));
                        finishTest();
                    }
                });
                updatePost.request();
            }
        }
    }

    private class OnAddButtonClickListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                selectedImagePath = getRealPathFromURI(selectedImageUri);
                System.out.println("selectedPath1 : " + selectedImagePath);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                bmpThumbnails = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImagePath, opt), WIDTH, HEIGHT, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                thumbnailPath = saveImagePath(selectedImagePath, bmpThumbnails);
                // System.out.println("thumbnailPath : " + thumbnailPath);
                UploadImage uploadImage = new UploadImage(context);
                File imageFile = new File(selectedImagePath);
                File thumbnailFile = new File(thumbnailPath);
                uploadImage.setParameter(imageFile, thumbnailFile);
                uploadImage.setEvent(new UploadImagePostEvent() {
                    public void onSuccess(String imageId) {
                        linearLayoutPost = (LinearLayout) findViewById(R.id.linear_layout_images);
                        LayoutInflater mInflater = LayoutInflater.from(EditPostActivity.this);
                        final View deleteView  = mInflater.inflate(R.layout.image_view_with_remove,null);
                        ImageView image = (ImageView)deleteView.findViewById(R.id.image);
                        deleteView.setId(imageNum++);
                        linearLayoutPost.addView(deleteView);

                        image.setScaleType(ImageView.ScaleType.CENTER);
                        image.setImageBitmap(bmpThumbnails);
                        ImageView removeImage = (ImageView)deleteView.findViewById(R.id.remove);
                        removeImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linearLayoutPost.removeView(deleteView);
                                System.out.println("image : " + deleteView.getId());
                            }
                        });
                    }
                    public void onFailure(int errno) {

                        System.out.println("errno " + errno);
                    }
                });
                uploadImage.request();
            }
        }
    }
    public String saveImagePath(String imagePath , Bitmap bitmap){
        String thumbnailFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/thumbnail";
        System.out.println("thumbnailFileName : " + thumbnailFileName);
        File newFile = new File(thumbnailFileName);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        String thumbnailPath = newFile.getAbsolutePath()+"/"+imageNum + ".jpg";
        System.out.println("thumbnailPath : " + thumbnailPath);
        File thumbnailFile = new File(thumbnailPath);
        try{
            FileOutputStream out =new FileOutputStream(thumbnailFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            out.flush();
            out.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return thumbnailPath;
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;

    }



}
