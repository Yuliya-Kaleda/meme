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
    private Button chooseTemplate;
    private String option= "action";
    private static final String sharedpref = "nyc.c4q.fattyduck.meme.sharedpref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        take = (Button) findViewById(R.id.take);
        chooseGallery = (Button) findViewById(R.id.chooseGallery);
        chooseTemplate = (Button) findViewById(R.id.chooseTemp);
        take.setOnClickListener(this);
        chooseGallery.setOnClickListener(this);
        chooseTemplate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.take:
                saveString("take");
                startSecond();
                break;
            case R.id.chooseGallery:
                saveString("choose");
                break;
            case R.id.chooseTemp:
                Intent i = new Intent(this, Template.class);
                startActivity(i);
                break;
        }
    }

    public void saveString(String saveStr){
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(option, saveStr);
    }
    public void startSecond(){
        Intent i = new Intent(this,SecondActivity.class);
        startActivity(i);
    }


}