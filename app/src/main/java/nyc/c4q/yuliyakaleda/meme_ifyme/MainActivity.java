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
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;





public class MainActivity extends ActionBarActivity {

    private static final int SELECT_PICTURE = 1;
    private static final int CREATE_PICTURE = 2;
    Button take;
    Button choose;
    Button blackFrame;
    ImageView image;
    Uri imageUri;

    //Bitmap
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

 /* An album folder for MeMeApp Images*/
   private String getAlbumName(){
        return "MeMeApp";
    }

   private File getAlbumDir() {
       File storageDir = null;
       if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
           storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

           if (storageDir != null) {
               if (!storageDir.mkdir()) {
                   if (!storageDir.exists()) {
                       Log.d("MeMeApp", "failed to create directory");
                       return null;
                   }
               }
           }
       } else {
           Log.v("MeMeApp", "External storage is not mounted READ/WRITE.");
       }
       return storageDir;
   }
private File createImageFile() throws IOException {
    //Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = JPEG_FILE_PREFIX + timeStamp + " ";
    File albumFile = getAlbumDir();
    File imageFile = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumFile);
    return imageFile;
}

private File setUpPhotoFile() throws IOException {
    File file = createImageFile();
    mCurrentPhotoPath = file.getAbsolutePath();
    return file;
}
private void setPic(){

    }

    int targetWidth = mImageView.getWidth();
    int targetHeight = mImageView.getHeight();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choose = (Button) findViewById(R.id.choose);

        if(choose!=null) {

            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("MainActivity", "pressed");

        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CREATE_PICTURE);


                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent chooser = Intent.createChooser(intent, "Select Picture");
                    startActivityForResult(chooser, SELECT_PICTURE);

                }
            });
        }
    }
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
                break;
        }
    }
}
