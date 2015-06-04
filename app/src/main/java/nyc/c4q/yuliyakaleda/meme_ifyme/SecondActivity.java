package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

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

    private ShareActionProvider mShareActionProvider;
    private Button background;
    private Button addText;

        ImageView image;
        TextView tv;
        Button share;
        Uri myUri;
        private android.widget.RelativeLayout.LayoutParams layoutParams;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.image);
            image = (ImageView) findViewById(R.id.image1);
            tv = (TextView) findViewById(R.id.text);
            share = (Button) findViewById(R.id.share_button);
//get the uri from the intent sent from MainActivity
            Intent intent = getIntent();
            myUri = intent.getParcelableExtra("uri");
            image.setImageURI(myUri);
            tv.setText("hello world");
// tv.setOnLongClickListener(new View.OnLongClickListener() {
// @Override
// public boolean onLongClick(View v) {
// ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
// String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//
// ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
// View.DragShadowBuilder myShadow = new View.DragShadowBuilder(tv);
//
// v.startDrag(dragData,myShadow,null,0);
// return true;
// }
// });
            tv.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                            Log.d("y", "Action is DragEvent.ACTION_DRAG_STARTED");
// Do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d("y", "Action is DragEvent.ACTION_DRAG_ENTERED");
                            int x_cord = (int) event.getX();
                            int y_cord = (int) event.getY();
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d("y", "Action is DragEvent.ACTION_DRAG_EXITED");
                            x_cord = (int) event.getX();
                            y_cord = (int) event.getY();
                            layoutParams.leftMargin = x_cord;
                            layoutParams.topMargin = y_cord;
                            v.setLayoutParams(layoutParams);
                            tv.setText("h");
                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d("y", "Action is DragEvent.ACTION_DRAG_LOCATION");
                            x_cord = (int) event.getX();
                            y_cord = (int) event.getY();
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d("y", "Action is DragEvent.ACTION_DRAG_ENDED");
// Do nothing
                            break;
                        case DragEvent.ACTION_DROP:
                            Log.d("y", "ACTION_DROP event");
                            tv.getX();
                            tv.getY();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(tv);
                        tv.startDrag(data, shadowBuilder, tv, 0);
                        tv.setVisibility(View.INVISIBLE);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
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
