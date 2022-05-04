package com.example.pararell;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TableRow tableRow;
    ProgressBar progressBar;
    ArrayList imagesView = new ArrayList();
    AssyncImageLoader assyncImageLoader;

    public static final String[] imageUrls ={
            "https://s.fishki.net/upload/users/2020/11/10/542137/a5387a83dfc1c69627f7fc8e7d995d35.png",
            "https://pbs.twimg.com/media/E8HFdr9WYAMasFW.jpg",
            "https://tlum.ru/uploads/d76725085d759746e0c1d527fdab49b3f6afe97b256267f72b3c95b38b3e095f.jpeg"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tableRow = findViewById(R.id.table_images);
        progressBar = findViewById(R.id.load_progress);

        for (int i = 0; i < tableRow.getChildCount(); i++) {
            imagesView.add(tableRow.getChildAt(i));
        }


    }
    public void loadImages(View view) {

        if (assyncImageLoader == null || assyncImageLoader.getStatus() == AsyncTask.Status.FINISHED){
            assyncImageLoader = new AssyncImageLoader();
            assyncImageLoader.execute(imageUrls);
            progressBar.setProgress(0);
        }
        else {
            Toast.makeText(getApplicationContext(), "Поток запущен", Toast.LENGTH_SHORT).show();
        }

    }


    class AssyncImageLoader extends AsyncTask<String, Pair<Integer,Bitmap>,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            for (int i = 0; i < strings.length ; i++) {

                try {
                    URL url = new URL(imageUrls[0]);
                    Bitmap image = BitmapFactory.decodeStream(url.openStream());
                    Thread.sleep(500);
                    Pair<Integer,Bitmap> pair = new Pair<>(i, image);
                    publishProgress(pair);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Pair<Integer, Bitmap>... values) {
            int position = values[0].first;
            Bitmap image = values[0].second;
            progressBar.setProgress((position + 1) * 100);
            ((ImageView) imagesView.get(position)).setImageBitmap(image);
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(getApplicationContext(), "Загрузка завершена!", Toast.LENGTH_SHORT).show();
        }
    }



    }