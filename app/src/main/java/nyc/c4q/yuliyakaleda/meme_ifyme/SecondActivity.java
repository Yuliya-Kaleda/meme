package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by July on 5/31/15.
 */
public class SecondActivity extends Activity {
    private static final String TAG = SecondActivity.class.getSimpleName();

    public static final String MY_FILE = "photo";
    ImageView image;
    ViewGroup tv;
    Button share;
    Button motivation;
    Button vanilla;
    Uri myUri;
    Bitmap bm;
    EditText text1;
    EditText text2;
    EditText text3;
    ImageView imageBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, "SecondActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        motivation = (Button) findViewById(R.id.motivation);
        motivation.setOnClickListener(new View.OnClickListener() {
                  @Override
                       public void onClick(View view) {
                         Intent intent = new Intent();
                         imageBackground.setVisibility(View.VISIBLE);
                                          }
                                      });

        vanilla = (Button) findViewById(R.id.vanilla);
        vanilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                imageBackground.setVisibility(View.INVISIBLE);
            }
        });



        text1 =(EditText) findViewById(R.id.addText1);
        text2 =(EditText) findViewById(R.id.addText2);
        text3 =(EditText) findViewById(R.id.addText3);


       image = (ImageView) findViewById(R.id.image1);
       //tv = (RelativeLayout) findViewById(R.id.merge_image);
       share = (Button) findViewById(R.id.share_button);
       //get the uri from the intent sent from MainActivity
        Intent intent = getIntent();
        Log.d(MainActivity.TAG, String.format("SecondActivity.onCreate() intent:", intent));
        bm = intent.getParcelableExtra("bitmap");
        int height = bm.getHeight();
        int width = bm.getWidth();
        bm = Bitmap.createScaledBitmap(bm, height, width, false);
        image.setImageBitmap(bm);


     // make a screen shot of our layout to convert into an image to share
//        View u = findViewById(R.id.scroll);
//        u.setDrawingCacheEnabled(true);
//        ScrollView z = (ScrollView) findViewById(R.id.screen);
//        int totalHeight = z.getChildAt(0).getHeight();
//        int totalWidth = z.getChildAt(0).getWidth();
//        u.layout(0, 0, totalWidth, totalHeight);
//        u.buildDrawingCache(true);
//        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
//        u.setDrawingCacheEnabled(false);

//
//        //Save bitmap
//        String extr = Environment.getExternalStorageDirectory().toString() +   File.separator + "Folder";
//        String fileName = new SimpleDateFormat("yyyyMMddhhmm'_report.jpg'").format(new Date());
//        File myPath = new File(extr, fileName);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(myPath);
//            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
//        }catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//

//        String path = Environment.getExternalStorageDirectory().toString();
//        OutputStream fOut = null;
//        File file = new File(path, "FitnessGirl.jpg"); // the File to save to
//        try {
//            fOut = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        b.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//        try {
//            fOut.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fOut.close(); // do not forget to close the stream
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "sharedImage";
                try {
                    File tempFile = File.createTempFile(filename, "jpg");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    bm.compress(Bitmap.CompressFormat.JPEG,100, fos);
                    shareVia(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
            }
        });


    }
    //method to share an image via social networks
    public void shareVia(File attachment) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, attachment.toURI());
       // startActivity(Intent.createChooser(se(getResources((R.id.merge_image));
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }
}
