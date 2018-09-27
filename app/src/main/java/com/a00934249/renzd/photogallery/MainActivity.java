package com.a00934249.renzd.photogallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final static int REQUEST_IMAGE_CAPTURE = 1;
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    public static final int SEARCH_LIST_REQUEST_CODE = 2;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery;
    private ArrayList<String> gallerySearchList;
    public String caption;
    public Boolean onMainGallery = true;

    ImageView imageView;
    Button buttonLeft;
    Button buttonRight;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLeft = (Button)findViewById(R.id.btnLeft);
        buttonRight = (Button)findViewById(R.id.btnRight);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);


        caption = "topleft";
        et = (EditText)findViewById(R.id.captionText);

        imageView = (ImageView)findViewById(R.id.ivMain);
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        photoGallery = populateGallery(minDate, maxDate);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0) {
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        }
    }

    //unused
    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //NOT USED
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));

                photoGallery = populateGallery(new Date(), new Date());
                Log.d("onCreate, size", Integer.toString(photoGallery.size()));
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);

                displayPhoto(currentPhotoPath);
            }
        }
        //On image request, populate the gallery
        //then, display current index photo from the gallery array
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                Log.d("createImageFile", "Picture Taken");
                photoGallery = populateGallery(new Date(), new Date());
                currentPhotoIndex = photoGallery.size() - 1;

                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                displayPhoto(currentPhotoPath);

                onMainGallery = true;
            }
        }
        //Call search gallery
        if (requestCode == 999 && resultCode == RESULT_OK) {
            searchGallery();
        }
    }

    /**
     * Sets the gallery search array list and display
     * Calls the populateSearchGallery() to get searched photos
     * then displays the photo
     */
    public void searchGallery() {
        Log.d("Before", "populateSearchGallery");
        gallerySearchList = populateSearchGallery(new Date(), new Date());
        Log.d("Passed: ", "populateSearchGallery");

        if (gallerySearchList.size() != 0) {
            currentPhotoIndex = 0;
            Log.d("Passed", Integer.toString(gallerySearchList.size()) + " SIZE");
            currentPhotoPath = gallerySearchList.get(currentPhotoIndex);
            Log.d("Passed: ", "gallerySearchList");
            displayPhoto(currentPhotoPath);
            Log.d("Passed: ", "displayPhoto");
            onMainGallery = false;
        }
    }

    /**
     * Gets photos from the external storage
     * and stores them in an arraylist
     * @param minDate
     * @param maxDate
     * @return photoGallery
     */
    private ArrayList<String> populateGallery(Date minDate, Date maxDate) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.a00934249.renzd.photogallery/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            String fileName = "";
            for (File f : file.listFiles()) {

                String imageName[] = f.getPath().split("/");
                for (int xy = 0; xy < imageName.length; xy++) {
                    //Log.d("imageNAMES ", imageName[xy]);
                }
                fileName = imageName[imageName.length-1];
                String findCaption[] = fileName.split("_");
                for (int xy = 0; xy < findCaption.length; xy++) {
                    Log.d("fileName: ", findCaption[xy]);
                }
                Log.d("CAPTIONFINDER: ", findCaption[1]);
                photoGallery.add(f.getPath());
            }
        }
        return photoGallery;
    }

    /**
     * Gets photos from the external storage
     * filters the searched photos
     * and stores them in an arraylist
     * @param minDate
     * @param maxDate
     * @return gallerySearchList
     */
    private ArrayList<String> populateSearchGallery(Date minDate, Date maxDate) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.a00934249.renzd.photogallery/files/Pictures");
        gallerySearchList = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            String fileName = "";
            String cpt = "";
            //SCAN THROUGH ALL FILE LIST
            for (File f : file.listFiles()) {

                //CHECK FILENAME    D/Filename: JPEG_topleft_20180926_172446_200779324.jpg
                String imageName[] = f.getPath().split("/");
                fileName = imageName[imageName.length-1];
                Log.d("Filename", fileName);

                //SPLIT THE FILE NAME INTO findCaption[]
                //RESULT:
                //JPEG
                //Caption
                //20180926
                //173351
                //-71506678.jpg
                String findCaption[] = fileName.split("_");
                for (int xy = 0; xy < findCaption.length; xy++) {
                    Log.d("fileName: ", findCaption[xy]);
                }
                //RESULT:
                //D/FindCaption: Caption
                Log.d("FindCaption", findCaption[1]);
                //IF CAPTION MATCHES, ADD THIS FILE(f.getPath()) to gallerySearchList
                if (findCaption[1].equals(caption)) {
                    Log.d("Inside IF", findCaption[1]);
                    gallerySearchList.add(f.getPath());
                }
            }
        }
        //RETURNS 0
        Log.d("RETURN", Integer.toString(gallerySearchList.size()));
        return gallerySearchList;
    }

    //Sets the photo's bitmap
    private void displayPhoto(String path) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    /**
     * Calls camera intent
     * attempts to create an image/photo file
     * then start activity
     * @param v
     */
    public void takePicture(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("FileCreation", "Failed");
            }
            if (photoFile != null) {
                Log.d("FileCreation", "starting....");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.a00934249.renzd.fileprovider",
                        photoFile);
                Log.d("FileCreation", "Got URI");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Gets an image file .jpg
     * formatted with a caption and timestamp
     * @return image
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String caption = et.getText().toString();
        String imageFileName = "JPEG_" + caption + "_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        //Log.d("createImageFile", currentPhotoPath + " DONE");
        return image;
    }


    /**
     * Determines the index of current photo
     * and Display that photo
     * Checks if on main gallery or the search gallery arraylist
     * @param v view
     */
    public void onClick( View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                --currentPhotoIndex;
                break;
            case R.id.btnRight:
                ++currentPhotoIndex;
                break;
            default:
                break;
        }
        if (currentPhotoIndex < 0)
            currentPhotoIndex = 0;
        if (onMainGallery == true) {
            if (currentPhotoIndex >= photoGallery.size())
                currentPhotoIndex = photoGallery.size() - 1;

            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            Log.d("photoleft, size", Integer.toString(photoGallery.size()));
        } else {
            if (currentPhotoIndex >= gallerySearchList.size())
                currentPhotoIndex = gallerySearchList.size() - 1;

            currentPhotoPath = gallerySearchList.get(currentPhotoIndex);
            Log.d("photoleft, size", Integer.toString(gallerySearchList.size()));
        }
        Log.d("photoleft, index", Integer.toString(currentPhotoIndex));
        displayPhoto(currentPhotoPath);

    }

    /**
     * Opens the search activity
     * @param v
     */
    public void goToSearch(View v) {
        startActivityForResult(new Intent(getApplicationContext(),SearchActivity.class), 999);
    }

    /**
     * Opens the settings activity
     * currently empty
     * @param v
     */
    public void goToSettings(View v) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    /**
     * Opens the display activity
     * @param x
     */
    public void goToDisplay(String x) {
        Intent i = new Intent(this, DisplayActivity.class);
        i.putExtra("DISPLAY_TEXT", x);
        startActivity(i);
    }
}
