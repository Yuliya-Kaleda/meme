package nyc.c4q.yuliyakaleda.meme_ifyme;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final String TAG = "MEME_ACTIVITY";
    Button take;
    Button choose;
    Button blackFrame;
    ImageView image;
    Uri imageUri;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        take = (Button) findViewById(R.id.take);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (taker.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(taker, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        choose = (Button) findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "pressed");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent chooser = Intent.createChooser(intent, "Select Picture");
                startActivityForResult(chooser, SELECT_PICTURE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.format("onActivityResult(): SELECT_PICTURE==%s, REQUEST_IMAGE_CAPTURE==%s, resultCode==%s",
                requestCode == SELECT_PICTURE,
                requestCode == REQUEST_IMAGE_CAPTURE,
                resultCode == RESULT_OK));
        Bitmap imageBitmap = null;
        switch (requestCode) {
            case SELECT_PICTURE:
                Log.d(TAG, "onActivityResult(): I just selected a picture");
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
// http://stackoverflow.com/a/10281667, image is too big so you gotta resize
                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, 200, true);
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                Log.d(TAG, "onActivityResult(): I just took a picture");
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                }
                break;
        }
        Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
        sentTo.putExtra("bitmap", imageBitmap);
        startActivity(sentTo);
    }
}