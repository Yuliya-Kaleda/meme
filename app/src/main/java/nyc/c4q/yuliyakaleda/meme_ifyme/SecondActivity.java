package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by July on 5/31/15.
 */
public class SecondActivity extends Activity {


    ImageView image;
    TextView tv;
    Button share;
    Uri myUri;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, "SecondActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        image = (ImageView) findViewById(R.id.image1);
        tv = (TextView) findViewById(R.id.text);
        share = (Button) findViewById(R.id.share_button);

        //get the uri from the intent sent from MainActivity
        Intent intent = getIntent();

        Log.d(MainActivity.TAG, String.format("SecondActivity.onCreate() intent:", intent));
        bm = intent.getParcelableExtra("bitmap");
        image.setImageBitmap(bm);



        //
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                shareVia();
            }
        });

    }

    //method to share an image via social networks
    public void shareVia() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }
}



