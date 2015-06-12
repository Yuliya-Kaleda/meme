package nyc.c4q.yuliyakaleda.meme_ifyme;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private Button take;
    private Button choose;
    private Uri imageUri;

    private static final String LINE_TOP = "top";
    private static final String LINE_BOTTOM = "bottom";
    private static final String PICTURE = "pict";
    private static final String DEMO = "demo";
    private static final String VAN = "van";

    private ImageView image;
    private EditText edTop;
    private EditText edBottom;
    private Button shareButton;
    private Button demotivationButton;
    private Button vanillaButton;
    private Bitmap bm;
    private Bitmap modifiedBit;

    private boolean van = false;
    private boolean demo = false;
    private String mCurrentPhotoPath;

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("SelectedImagePath", imageUri.toString());
//    }
//
//    public void setLayoutView(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            demo = savedInstanceState.getBoolean(DEMO);
//            van = savedInstanceState.getBoolean(VAN);
//            if (demo) {
//                //setContentView(R.layout.demotivational);
//            } else if (van) {
//                //setContentView(R.layout.vanilla);
//            } else {
//                //setContentView(R.layout.image);
//            }
//        } else {
//            setContentView(R.layout.image);
//        }
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        outState.putParcelable(PICTURE, bitmap);
        outState.putBoolean(DEMO, demo);
        outState.putBoolean(VAN, van);
        if (edTop!=null) {
            outState.putString(LINE_TOP, edTop.getText().toString());
            outState.putString(LINE_BOTTOM, edBottom.getText().toString());
        }
    }

    public void saveData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bitmap bmInst = savedInstanceState.getParcelable(PICTURE);
            image.setImageBitmap(bmInst);
            String top = savedInstanceState.getString(LINE_TOP);
            String bottom = savedInstanceState.getString(LINE_BOTTOM);
            if (edTop!=null) {
                edTop.setText(top);
                edBottom.setText(bottom);
            }
        }
    }


    public void initializeViews() {
        image = (ImageView) findViewById(R.id.image1);
        //demotivation = (Button) findViewById(R.id.demotivation);
        //vanilla = (Button) findViewById(R.id.vanilla);
        shareButton = (Button) findViewById(R.id.share_button);
        edBottom = (EditText) findViewById(R.id.line_bottom);
        edTop = (EditText) findViewById(R.id.line_top);
    }

    public void getIntentInfo() {
        Intent intent = getIntent();
        bm = intent.getParcelableExtra("bitmap");
        image.setImageBitmap(bm);

        int height = bm.getHeight();
        int width = bm.getWidth();
        bm = Bitmap.createScaledBitmap(bm, height, width, false);
    }

    public void setEventListener(boolean setFlag) {
        shareButton = (Button) findViewById(R.id.share_button);
        if(!setFlag) {
            shareButton.setOnClickListener(null);
            demotivationButton.setOnClickListener(null);
            vanillaButton.setOnClickListener(null);
        }
        else {

            if(demotivationButton!=null) {
                demotivationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //setContentView(R.layout.demotivational);
                        demo = true;

                        ImageView imBlackBack = (ImageView) findViewById(R.id.image1);
                        imBlackBack.setImageBitmap(bm);

                        setEventListener(true);
                    }
                });
            }
            if (vanillaButton!=null) {
                vanillaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // setContentView(R.layout.vanilla);
                        van = true;

                        ImageView imBlackBack = (ImageView) findViewById(R.id.image1);
                        imBlackBack.setImageBitmap(bm);

                        setEventListener(true);

                    }
                });
            }
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                    shareButton.setVisibility(View.GONE);
                    rl.setDrawingCacheEnabled(true);
                    rl.buildDrawingCache();
                    modifiedBit = rl.getDrawingCache();

                    shareVia(modifiedBit);
                }
            });
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //method to share an image via social networks
    public void shareVia(Bitmap mBitmap) {
        File f;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        try {
            f = createImageFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(mCurrentPhotoPath));
        startActivity(Intent.createChooser(share, "Share Image"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((savedInstanceState != null)) {
           // imageUri = savedInstanceState.getString("SelectedImagePath");
        }

        take = (Button) findViewById(R.id.take);
        choose = (Button) findViewById(R.id.choose);

        setEventListener(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap imageBitmap = null;
        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        // http://stackoverflow.com/a/10281667, image is too big so you gotta resize
                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, 200, true);
                    } catch (IOException e) {
                    }
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                }
                break;
        }

        //setLayoutView(out);
        initializeViews();
        getIntentInfo();
        //saveData(savedInstanceState);
        setEventListener(true);

//       // Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
//        sentTo.putExtra("bitmap", imageBitmap);
//        startActivity(sentTo);
    }


//    public void setEventListener(boolean setFlag) {
//        if (!setFlag) {
//            take.setOnClickListener(null);
//            choose.setOnClickListener(null);
//        }
//        else {
//            take.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent taker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    taker.putExtra("imageUri", imageUri);
//                    if (taker.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(taker, REQUEST_IMAGE_CAPTURE);
//                    }
//                }
//            });
//            choose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.putExtra("imageUri", imageUri);
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    Intent chooser = Intent.createChooser(intent, "Select Picture");
//                    startActivityForResult(chooser, SELECT_PICTURE);
//                }
//            });
//        }
//    }
 }