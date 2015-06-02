package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.content.Intent;
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
