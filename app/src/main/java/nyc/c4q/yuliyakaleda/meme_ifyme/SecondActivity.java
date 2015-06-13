package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
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

    // KEYS
    private static final String LINE_TOP = "top";
    private static final String LINE_BOTTOM = "bottom";
    private static final String PICTURE = "pict";
    private static final String DEMO = "demo";
    private static final String VAN = "van";
    private boolean isVanilla = false;
    private boolean isDemotivational = false;
    // IMAGE HOLDERS
    private ImageView imageHolder;
    private RelativeLayout layoutHolder;
    // TEXT HOLDERS
    private EditText etTop;
    private EditText etBottom;
    // BUTTONS
    private Button shareBtn;
    private Button saveBtn;
    private Button demotivationalBtn;
    private Button vanillaBtn;
    // BITMAP SOURCES
    private Bitmap intentBitmap;
    private Bitmap savedBitmap;

    private String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PICTURE, intentBitmap);
        outState.putBoolean(DEMO, isDemotivational);
        outState.putBoolean(VAN, isVanilla);
        if (etTop != null) {
            outState.putString(LINE_TOP, etTop.getText().toString());
            outState.putString(LINE_BOTTOM, etBottom.getText().toString());
        }
    }

    public void loadData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isDemotivational = savedInstanceState.getBoolean(DEMO);
            isVanilla = savedInstanceState.getBoolean(VAN);
            intentBitmap = savedInstanceState.getParcelable(PICTURE);
            if (isDemotivational) {
                setContentView(R.layout.demotivational);
                initializeViews();
                getIntentInfo();
                setEventListener();
            }
            else if (isVanilla) {
                setContentView(R.layout.vanilla);
                initializeViews();
                getIntentInfo();
                setEventListener();
            }
            else {
                setContentView(R.layout.neutral);
                initializeViews();
                getIntentInfo();
                setEventListener();
            }
            imageHolder.setImageBitmap(intentBitmap);
            String top = savedInstanceState.getString(LINE_TOP);
            String bottom = savedInstanceState.getString(LINE_BOTTOM);
            etTop.setText(top);
            etBottom.setText(bottom);
        }
        else {
            setContentView(R.layout.neutral);
            initializeViews();
            getIntentInfo();
            setEventListener();
        }
    }

    public void initializeViews() {
        layoutHolder = (RelativeLayout) findViewById(R.id.relative_layout_holder);
        imageHolder = (ImageView) findViewById(R.id.image_holder);
        demotivationalBtn = (Button) findViewById(R.id.goto_demotivational_layout);
        vanillaBtn = (Button) findViewById(R.id.goto_vanilla_layout);
        shareBtn = (Button) findViewById(R.id.share_button);
        saveBtn = (Button) findViewById(R.id.save_button);
        etBottom = (EditText) findViewById(R.id.bottom_edit);
        etTop = (EditText) findViewById(R.id.top_edit);
    }

    public void getIntentInfo() {
        intentBitmap = getIntent().getParcelableExtra("bitmap");
        imageHolder.setImageBitmap(intentBitmap);
        int height = intentBitmap.getHeight();
        int width = intentBitmap.getWidth();
        intentBitmap = Bitmap.createScaledBitmap(intentBitmap, height, width, false);
    }

    public void setEventListener() {
        if (demotivationalBtn != null) {
            demotivationalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.demotivational);
                    initializeViews();
                    setEventListener();
                    isDemotivational = true;
                    isVanilla = false;
                    imageHolder.setImageBitmap(intentBitmap);
                }
            });
        }
        if (vanillaBtn != null) {
            vanillaBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.vanilla);
                    initializeViews();
                    setEventListener();
                    isVanilla = true;
                    isDemotivational = false;
                    imageHolder.setImageBitmap(intentBitmap);
                }
            });
        }
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBtn.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.INVISIBLE);
                demotivationalBtn.setVisibility(View.INVISIBLE);
                vanillaBtn.setVisibility(View.INVISIBLE);

                layoutHolder.setDrawingCacheEnabled(true);
                layoutHolder.buildDrawingCache();
                savedBitmap = layoutHolder.getDrawingCache();

                shareThis(savedBitmap);

                shareBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                demotivationalBtn.setVisibility(View.VISIBLE);
                vanillaBtn.setVisibility(View.VISIBLE);

                Toast.makeText(getBaseContext(),"SHARED",Toast.LENGTH_SHORT).show();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtn.setVisibility(View.INVISIBLE);
                shareBtn.setVisibility(View.INVISIBLE);
                demotivationalBtn.setVisibility(View.INVISIBLE);
                vanillaBtn.setVisibility(View.INVISIBLE);

                layoutHolder.setDrawingCacheEnabled(true);
                layoutHolder.buildDrawingCache();
                savedBitmap = layoutHolder.getDrawingCache();

                saveThis(savedBitmap);

                shareBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                demotivationalBtn.setVisibility(View.VISIBLE);
                vanillaBtn.setVisibility(View.VISIBLE);

                Toast.makeText(getBaseContext(),"SAVED",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shareThis(Bitmap receivedBitmap) {
        ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();
        receivedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, compressedOutputStream);
        try {
            FileOutputStream outputStream = new FileOutputStream(createBlankImageFile());
            outputStream.write(compressedOutputStream.toByteArray());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(mCurrentPhotoPath));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void saveThis(Bitmap receivedBitmap) {
        ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();

        receivedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, compressedOutputStream);

        try {
            FileOutputStream outputStream = new FileOutputStream(createBlankImageFile());
            outputStream.write(compressedOutputStream.toByteArray());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.parse(mCurrentPhotoPath));
        sendBroadcast(mediaScanIntent);

    }

    private File createBlankImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();
        return imageFile;
    }

}

