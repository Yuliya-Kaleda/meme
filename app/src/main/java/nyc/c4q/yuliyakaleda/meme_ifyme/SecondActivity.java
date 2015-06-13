package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by July on 5/31/15.
 */
public class SecondActivity extends Activity {

    private static final String LINE_TOP = "top";
    private static final String LINE_BOTTOM = "bottom";
    private static final String PICTURE = "pict";
    private static final String DEMO = "demo";
    private static final String VAN = "van";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int RESULT_LOAD_IMG = 1;

    private ImageView image;
    private EditText edTop;
    private EditText edBottom;
    private Button share;
    private Button demotivation;
    private Button vanilla;
    private Bitmap bm;
//    private boolean van = false;
//    private boolean demo = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        initializeViews();
        Intent intent = getIntent();
        String option = intent.getExtras().getString("string");
        if (option.equals("take")) {
            Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(openCamera, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        if (option.equals("choose")){
            Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            openGallery.setType("image/*");
            startActivityForResult(openGallery, RESULT_LOAD_IMG);
        }
    }

//    public void setLayoutView(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            demo = savedInstanceState.getBoolean(DEMO);
//            van = savedInstanceState.getBoolean(VAN);
//            if (demo) {
//                setContentView(R.layout.demotivational);
//            } else if (van) {
//                setContentView(R.layout.vanilla);
//            } else {
//                setContentView(R.layout.image);
//            }
//        } else {
//            setContentView(R.layout.image);
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        outState.putParcelable(PICTURE, bitmap);
//        outState.putBoolean(DEMO, demo);
//        outState.putBoolean(VAN, van);
//        if (edTop!=null) {
//            outState.putString(LINE_TOP, edTop.getText().toString());
//            outState.putString(LINE_BOTTOM, edBottom.getText().toString());
//        }
//    }

//    public void saveData(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            Bitmap bmInst = savedInstanceState.getParcelable(PICTURE);
//            image.setImageBitmap(bmInst);
//            String top = savedInstanceState.getString(LINE_TOP);
//            String bottom = savedInstanceState.getString(LINE_BOTTOM);
//            if (edTop!=null) {
//                edTop.setText(top);
//                edBottom.setText(bottom);
//            }
//        }
//    }


    public void initializeViews() {
        image = (ImageView) findViewById(R.id.image1);
        demotivation = (Button) findViewById(R.id.demotivation);
        vanilla = (Button) findViewById(R.id.vanilla);
        share = (Button) findViewById(R.id.share_button);
        edBottom = (EditText) findViewById(R.id.line_bottom);
        edTop = (EditText) findViewById(R.id.line_top);
    }

//    public void setEventListener(boolean setFlag) {
//        share = (Button) findViewById(R.id.share_button);
//        if(!setFlag) {
//            share.setOnClickListener(null);
//            demotivation.setOnClickListener(null);
//            vanilla.setOnClickListener(null);
//        }
//        else {
//
//            if(demotivation!=null) {
//                demotivation.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setContentView(R.layout.demotivational);
//                        demo = true;
//
//                        ImageView imBlackBack = (ImageView) findViewById(R.id.image1);
//                        imBlackBack.setImageBitmap(bm);
//
//                        setEventListener(true);
//                    }
//                });
//            }
//            if (vanilla!=null) {
//                vanilla.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setContentView(R.layout.vanilla);
//                        van = true;
//
//                        ImageView imBlackBack = (ImageView) findViewById(R.id.image1);
//                        imBlackBack.setImageBitmap(bm);
//
//                        setEventListener(true);
//
//                    }
//                });
//            }
//            share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
//                    share.setVisibility(View.GONE);
//                    rl.setDrawingCacheEnabled(true);
//                    rl.buildDrawingCache();
//
//                }
//            });
//        }
//    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri selectedImage = data.getData();
                getContentResolver().notifyChange(selectedImage, null);
                ContentResolver cr = getContentResolver();
                image.setImageBitmap(bm);


            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            bm = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bm);
        }
    }

    }

