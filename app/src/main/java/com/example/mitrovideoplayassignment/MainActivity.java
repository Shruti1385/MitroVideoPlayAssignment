package com.example.mitrovideoplayassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.mitrovideoplayassignment.Adapter.CustomGridViewAdapter;
import com.example.mitrovideoplayassignment.Model.VideoDetailsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_READ = 0;
    public static ArrayList<VideoDetailsModel> videoArrayList;
    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    SearchView searchView;
    CustomGridViewAdapter customAdapter;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView) findViewById(R.id.search);
        videoArrayList = new ArrayList<>();



        if (checkPermission()) {
            initRecyclerview();


        }

    }

    //get video files from storage
    public void getVideos() {
        orientation = this.getResources().getConfiguration().orientation;
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {


            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String formatted_date = getDate(Integer.parseInt(date));
                VideoDetailsModel  videoDetailsModel  = new VideoDetailsModel ();

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (videoArrayList.size() == 4)//2*2 grid
                        break;
                }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){//3*3 grid
                    if(videoArrayList.size() == 9)
                        break;
                }

                videoDetailsModel .setVideoName(title);
                videoDetailsModel .setVideoUri(Uri.parse(data));
                videoDetailsModel .setVideoCreatedDate(formatted_date);
                videoArrayList.add(videoDetailsModel);

            } while (cursor.moveToNext());

        }
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }

        });
    }

    public void initRecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        orientation = this.getResources().getConfiguration().orientation;

        // set a GridLayoutManager with number of columns based on device screen oreintation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        getVideos();

        customAdapter = new CustomGridViewAdapter(MainActivity.this, videoArrayList);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }

    //format String  from Media.Date_Added
    public String getDate(long val){
        val*=1000L;
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(val));
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {
                        initRecyclerview();
                    }
                }
            }
        }
    }
}