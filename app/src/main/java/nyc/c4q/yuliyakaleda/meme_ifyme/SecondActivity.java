package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    final static private String APP_KEY = "vaqyd4n8eyetosx";


    final static private String APP_SECRET = "4qgw2e2vt3lfvgn";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    public static final int DROPBOX_FINISHED = 1324;

    File dropFile=null;

    Handler backgroundHandler;
    Handler mainHandler;

    public static final int NOTIFICATION_ID = 1234;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(savedInstanceState);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(SecondActivity.this);

        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==DROPBOX_FINISHED){
                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(NOTIFICATION_ID, makeNotification("Dropbox", "Upload Complete"));
                }
            }
        };

    }

    public Notification makeNotification(String title,String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        // THINGS TO PASS INTO METHOD
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_stat_action_account_balance);
        return builder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
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

                // REPLACE THIS WITH DROPBOX

                HandlerThread backgroundThread = new HandlerThread("background");
                backgroundThread.start();

                Looper looper = backgroundThread.getLooper();

                backgroundHandler = new Handler(looper);

                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        File file = sendDropbox(savedBitmap);

                        FileInputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        DropboxAPI.Entry response = null;
                        try {
                            response = mDBApi.putFile("/meme.jpeg", inputStream,
                                    file.length(), null, null);
                        } catch (DropboxException e) {
                            e.printStackTrace();
                        }
                        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);

                        Message message = Message.obtain(mainHandler, DROPBOX_FINISHED);
                        mainHandler.sendMessage(message);
                    }
                });



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

    public File sendDropbox(Bitmap receivedBitmap){

        try {
            dropFile = createBlankImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();

        receivedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, compressedOutputStream);

        try {
            FileOutputStream outputStream = new FileOutputStream(dropFile);
            outputStream.write(compressedOutputStream.toByteArray());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return dropFile;
    }

    public void saveThis(Bitmap receivedBitmap) {

        File dropFile=null;
        try {
            dropFile = createBlankImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();

        receivedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, compressedOutputStream);

        try {
            FileOutputStream outputStream = new FileOutputStream(dropFile);
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

