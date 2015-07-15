package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class TemplateActivity extends ActionBarActivity {

    private ListView listView;
    private ImageView imageView;
    private Template template;
    private final String notSimply = "https://imgflip.com/s/meme/One-Does-Not-Simply.jpg";
    private final String grumpyCat = "https://imgflip.com/s/meme/Grumpy-Cat.jpg";
    private final String confessionBear = "https://imgflip.com/s/meme/Confession-Bear.jpg";
    private byte[] simply;
    private byte[] grumpy;
    private byte[] bear;

    private TemplateHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView);

        createDatabase();

        String[] items = {"One Does Not Simply...", "Grumpy Cat", "Confession Bear"};
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                template = (Template) listView.getItemAtPosition(position);
                setImageView();

            }
        });

    }


    private void createDatabase() {
        dbHelper = new TemplateHelper(this);
        getByteArrays();
        insertTemplate(new Template("0", "Not Simply", simply));
        insertTemplate(new Template("1", "Grumpy Cat", grumpy));
        insertTemplate(new Template("2", "Confession Bear", bear));

    }

    private void getByteArrays() {
        simply = downloadImage(notSimply);
        grumpy = downloadImage(grumpyCat);
        bear = downloadImage(confessionBear);
    }

    private byte[] downloadImage(String url){
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    public void insertTemplate(Template template) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String delSql = "DELETE FROM TEMPLATES";
        SQLiteStatement delStmt = db.compileStatement(delSql);
        delStmt.execute();

        String sql = "INSERT INTO TEMPLATES (_ID, _NAME, _IMAGE) VALUES(?,?,?)";
        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1, template.getId());
        insertStmt.bindString(2, template.getName());
        insertStmt.bindBlob(3, template.getImage());
        insertStmt.executeInsert();
        db.close();
    }

    public Template getTemplate() {
        Template template = new Template();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM TEMPLATES";
        Cursor cursor = db.rawQuery(sql, new String[] {});

        if(cursor.moveToFirst()){
            template.setId(cursor.getString(0));
            template.setName(cursor.getString(1));
            template.setImage(cursor.getBlob(2));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        if(cursor.getCount() == 0){
            return null;
        } else {
            return template;
        }
    }

    public void setImageView() {
        template = getTemplate();
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(template.getImage(),
                0, template.getImage().length));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_template, menu);
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

