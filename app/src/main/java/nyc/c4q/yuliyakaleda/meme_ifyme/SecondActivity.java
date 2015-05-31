package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by July on 5/31/15.
 */
public class SecondActivity extends Activity {


    ImageView image;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);


        Intent intent = getIntent();
        Uri myUri = intent.getParcelableExtra("uri");
        image = (ImageView) findViewById(R.id.image1);
        image.setImageURI(myUri);

        tv = (TextView) findViewById(R.id.text);
        tv.setText("hello world");

        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("Touch me ", "TV");
                return true;
            }
        });


    }


    }

