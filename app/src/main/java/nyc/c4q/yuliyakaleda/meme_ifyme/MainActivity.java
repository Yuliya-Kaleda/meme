package nyc.c4q.yuliyakaleda.meme_ifyme;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.app.Activity;
import android.content.Intent;
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
    private static final int CREATE_PICTURE = 2;
    Button take;
    Button choose;
    Button blackFrame;
    ImageView image;
    Uri imageUri;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    // Photo album for this app
    private String getAlbumNAme() {
        return getString(R.string.album_name);
    }
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumNAme());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        take = (Button) findViewById(R.id.take);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
                        String.valueOf(System.currentTimeMillis()) + ".jpg"));
                taker.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(taker, CREATE_PICTURE);
            }
        });
        choose = (Button) findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "pressed");
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
        switch (requestCode) {
            case SELECT_PICTURE:
                Log.d("MainActivity", "I just selected a picture");
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
                    sentTo.putExtra("uri", imageUri);
                    startActivity(sentTo);
                }
            case CREATE_PICTURE:
                Log.d("MainActivity", "I just took a picture");
                if(resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Bundle extras = data.getExtras();
                    Log.e("URI",imageUri.toString());
                    Bitmap bmp = (Bitmap) extras.get("data");
                    Intent sentTo = new Intent(MainActivity.this, SecondActivity.class);
                    sentTo.putExtra("uri", imageUri);
                    startActivity(sentTo);
                }
                break;
        }
    }
}
