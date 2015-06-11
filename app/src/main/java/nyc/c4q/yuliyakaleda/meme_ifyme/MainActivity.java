package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    //hard coding constants are prone to errors so
    //made the request code constants here
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private Button take;
    private Button choose;

    private Bitmap imageBitmap;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        setEventListener(true);
    }

    public void initializeViews() {
        take = (Button) findViewById(R.id.take);
        choose = (Button) findViewById(R.id.choose);
    }

    public void setEventListener(boolean setFlag) {
        if (!setFlag) {
            take.setOnClickListener(null);
            choose.setOnClickListener(null);
        } else {
            //camera intent
            take.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent taker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (taker.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(taker, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });
            //gallery intent
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent chooser = Intent.createChooser(intent, "Select Picture");
                    startActivityForResult(chooser, SELECT_PICTURE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        imageBitmap = null;
        //the request codes differentiate which intent is returning the image
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

        //null pointer exception occurred
        //pressing the back button to go back to main activity screen
        //made the app crash when no picture was taken/selected in camera/gallery
        //added below condition so second activity starts only when there is an image stored
        if (imageBitmap != null) {
            Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
            sentTo.putExtra("bitmap", imageBitmap);
            startActivity(sentTo);
        } else {
            Toast.makeText(this, "No picture found", Toast.LENGTH_LONG).show();
        }
    }
}