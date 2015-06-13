package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {


    private Button take;
    private Button chooseGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        take = (Button) findViewById(R.id.take);
        chooseGallery = (Button) findViewById(R.id.chooseGallery);
        take.setOnClickListener(this);
        chooseGallery.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.take){
            startSecond("take");
        }else{
            startSecond("choose");
        }
    }

    public void startSecond(String extra){
        Intent i = new Intent(this,SecondActivity.class);
        i.putExtra("string", extra);
        startActivity(i);
    }


}