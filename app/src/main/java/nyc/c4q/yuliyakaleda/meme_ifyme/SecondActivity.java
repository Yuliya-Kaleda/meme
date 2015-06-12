package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

    private ImageView image;
    private EditText edTop;
    private EditText edBottom;
    private Button save;
    private Button share;
    private Button demotivation;
    private Button vanilla;
    private Bitmap bm;
    private Bitmap modifiedBit;

    private boolean van = false;
    private boolean demo = false;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayoutView(savedInstanceState);
        initializeViews();
        getIntentInfo();
        saveData(savedInstanceState);
        setEventListener(true);
    }


    public void setLayoutView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            demo = savedInstanceState.getBoolean(DEMO);
            van = savedInstanceState.getBoolean(VAN);
            if (demo) {
                setContentView(R.layout.demotivational);
            } else if (van) {
                setContentView(R.layout.vanilla);
            } else {
                setContentView(R.layout.image);
            }
        } else {
            setContentView(R.layout.image);
        }
    }

    public void initializeViews() {
        image = (ImageView) findViewById(R.id.image1);
        demotivation = (Button) findViewById(R.id.demotivation);
        vanilla = (Button) findViewById(R.id.vanilla);
        save = (Button) findViewById(R.id.save_button);
        share = (Button) findViewById(R.id.share_button);
        edBottom = (EditText) findViewById(R.id.line_bottom);
        edTop = (EditText) findViewById(R.id.line_top);
    }

    //Retrieve the image from the camera or gallery intent (Main Activity class)
    public void getIntentInfo() {
        Intent intent = getIntent();
        bm = intent.getParcelableExtra("bitmap");
        int height = bm.getHeight();
        int width = bm.getWidth();
        bm = Bitmap.createScaledBitmap(bm, height, width, false);
        image.setImageBitmap(bm);
    }

    //restore the previous instance state
    public void saveData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bitmap bmInst = savedInstanceState.getParcelable(PICTURE);
            image.setImageBitmap(bmInst);

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            Refactored following code to one-line code
            String top = savedInstanceState.getString(LINE_TOP);
            String bottom = savedInstanceState.getString(LINE_BOTTOM);
            if (edTop != null) {
                edTop.setText(top);
                edBottom.setText(bottom);
            }
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

            if (edTop != null || edBottom != null) {
                edTop.setText(savedInstanceState.getString(LINE_TOP));
                edBottom.setText(savedInstanceState.getString(LINE_BOTTOM));
            }
        }
    }

    public void setEventListener(boolean setFlag) {
        if (!setFlag) {
            share.setOnClickListener(null);
            demotivation.setOnClickListener(null);
            vanilla.setOnClickListener(null);
        } else {

            if (demotivation != null) {
                demotivation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.demotivational);
                        demo = true;

                        ImageView imBlackBack = (ImageView) findViewById(R.id.image1);
                        imBlackBack.setImageBitmap(bm);

                        setEventListener(true);
                    }
                });
            }

            if (vanilla != null) {
                vanilla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.vanilla);
                        van = true;

                        ImageView imBlackBack = (ImageView) findViewById(R.id.image1);
                        imBlackBack.setImageBitmap(bm);

                        setEventListener(true);

                    }
                });
            }

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                    share.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    rl.setDrawingCacheEnabled(true);
                    rl.buildDrawingCache();
                    modifiedBit = rl.getDrawingCache();

                    saveImage(modifiedBit);
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                    save.setVisibility(View.GONE);
                    share.setVisibility(View.GONE);
                    rl.setDrawingCacheEnabled(true);
                    rl.buildDrawingCache();
                    modifiedBit = rl.getDrawingCache();

                    shareVia(modifiedBit);

                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        outState.putParcelable(PICTURE, bitmap);
        outState.putBoolean(DEMO, demo);
        outState.putBoolean(VAN, van);
        if (edTop != null || edBottom != null) {
            outState.putString(LINE_TOP, edTop.getText().toString());
            outState.putString(LINE_BOTTOM, edBottom.getText().toString());
        }
    }

    //Default file extension is .jpeg
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //method to share an image via social networks
    //and other applications that allow sharing of data (i.e. email)
    //creating a new .jpeg image file
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

    //
    public void saveImage(Bitmap mBitmap) {

        File fileDirectory;
        String timeStamp;
        String imageFileName;
        File file;
        FileOutputStream fileOutputStream;

        //Name the image with the date created
        timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        imageFileName = "Meme " + timeStamp + ".jpeg"; //Must add the .jpeg file extension for the system to know it's a picture file

        //check if external storage is available
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                //Make new directory inside the Pictures directory
                fileDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/SavedMemes");
                fileDirectory.mkdirs();

                //Make the image file to be saved in the newly created SavedMemes directory
                file = new File(fileDirectory, imageFileName);
                fileOutputStream = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //if external storage is not available, save to internal storage
        else {
            try {
                fileOutputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

