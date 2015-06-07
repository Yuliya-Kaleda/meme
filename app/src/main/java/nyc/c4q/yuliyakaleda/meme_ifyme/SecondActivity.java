package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

    private ImageView image;
    private Button share;
    private Button demotivation;
    private Button vanilla;
    private Bitmap bm;
    private Bitmap modifiedBit;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        initializeViews();
        getIntentInfo();
        setEventListener(true);
    }

    public void initializeViews() {
        image = (ImageView) findViewById(R.id.image1);
        demotivation = (Button) findViewById(R.id.demotivation);
        vanilla = (Button) findViewById(R.id.vanilla);
        share = (Button) findViewById(R.id.share_button);
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
        if(!setFlag) {
            share.setOnClickListener(null);
            demotivation.setOnClickListener(null);
            vanilla.setOnClickListener(null);
        }
        else {

            demotivation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.demotivational);

                   // RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                    ImageView imBlackBack = (ImageView) findViewById(R.id.image_black_back);
                    imBlackBack.setImageBitmap(bm);
//
//                    rl.setDrawingCacheEnabled(true);
//                    rl.buildDrawingCache();
//                    modifiedBit = rl.getDrawingCache();
                    share = (Button) findViewById(R.id.share_button);
                    setEventListener(true);

                }
            });
            vanilla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.vanilla);

                    ImageView imBlackBack = (ImageView) findViewById(R.id.image_black_back);
                    imBlackBack.setImageBitmap(bm);

                    share = (Button) findViewById(R.id.share_button);
                    setEventListener(true);

                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareVia(bm);
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
    }

