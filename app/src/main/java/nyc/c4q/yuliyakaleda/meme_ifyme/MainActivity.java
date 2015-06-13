package nyc.c4q.yuliyakaleda.meme_ifyme;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import java.io.IOException;


public class MainActivity extends Activity {
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Button gotoCamera;
    private Button gotoGallery;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize widgets
        gotoCamera = (Button) findViewById(R.id.goto_camera_btn);
        gotoGallery = (Button) findViewById(R.id.goto_gallery_btn);
        // Set Listeners
        gotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent chooser = Intent.createChooser(intent, "Select Picture");
                startActivityForResult(chooser, SELECT_PICTURE);
            }
        });
        gotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (taker.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(taker, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
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
                        Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
                        sentTo.putExtra("bitmap", imageBitmap);
                        startActivity(sentTo);
                    }
                    catch (IOException e) {
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
                    sentTo.putExtra("bitmap", imageBitmap);
                    startActivity(sentTo);
                }
                break;
        }
    }

 }