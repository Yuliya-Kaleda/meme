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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;


public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Button take;
    Button choose;

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




}









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        take = (Button) findViewById(R.id.take);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        choose = (Button) findViewById(R.id.choose);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dispatchTakePictureIntent();
    }





    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
