package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by c4q-george on 6/12/15.
 */
public class MemeBitmapUtils extends BitmapDrawable {

    private int degree;
    private Bitmap bitmap;
    private Paint paint;

    MemeBitmapUtils(Bitmap bitmap, int degree) {
        super(bitmap);
        this.bitmap = bitmap;
        this.paint = new Paint();
        this.degree = degree;
    }

    @Override
    public int getIntrinsicWidth() {
        if (degree == 90 || degree == 270) {
            return bitmap.getHeight();
        } else
            return bitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        if (degree == 90 || degree == 270) {
            return bitmap.getWidth();
        } else
            return bitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        switch (degree) {
            case 90:
                canvas.rotate(degree);
                canvas.drawBitmap(bitmap, 0, -bitmap.getHeight(), paint);
                break;
            case 180:
                canvas.rotate(degree);
                canvas.drawBitmap(bitmap, -bitmap.getWidth(), -bitmap.getHeight(), paint);
                break;
            case 270:
                canvas.rotate(degree);
                canvas.drawBitmap(bitmap, -bitmap.getWidth(), 0, paint);
                break;
            default:
                canvas.drawBitmap(bitmap, 0, 0, paint);
                break;
        }
    }
    // TO USE THIS CORRECTLY PASS IN A VIEW AND THE SIZE YOU WOULD LIKE YOUR BITMAP TO BE
    public static Uri makeViewBitmap(View view, int width, int height) {

        // VIEW TO BITMAP
        Bitmap viewBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(viewBitmap);
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(canvas);

        // FILE SETUP
        String uniqueIdentifier = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());
        String fileName = "Snapmeme" + uniqueIdentifier;
        File fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // CREATING TEMP FILE
        File imageFILE = null;
        try {
            imageFILE = File.createTempFile(fileName, ".jpg", fileDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // STREAM INTO TEMP FILE
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFILE);
            viewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }



        // URI TO BE PASSED
        return Uri.fromFile(imageFILE);
    }
}