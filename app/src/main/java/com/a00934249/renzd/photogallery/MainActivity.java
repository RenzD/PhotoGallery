package com.a00934249.renzd.photogallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener  {
    final static int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayList<String> photoGallery;
    //private ArrayList<String> gallerySearchList;
    public String caption;
    //public Boolean onMainGallery = true;
    public DataHelper helperActivity;

    ImageView imageView;
    Button buttonLeft;
    Button buttonRight;
    EditText et;
    Button buttonCamera;
    private LocationManager locationManager;

    TextView tvLat;
    TextView tvLong;

    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tvLat = (TextView)findViewById(R.id.tvLat);
        tvLong = (TextView)findViewById(R.id.tvLong);
        helperActivity = new DataHelper();

        buttonLeft = (Button) findViewById(R.id.btnLeft);
        buttonRight = (Button) findViewById(R.id.btnRight);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        buttonCamera = (Button) findViewById(R.id.btnCamera);
        buttonCamera.setOnClickListener(takePhoto);

        et = (EditText) findViewById(R.id.captionText);

        imageView = (ImageView) findViewById(R.id.ivMain);
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        photoGallery = helperActivity.populateGallery(minDate, maxDate, photoGallery, latitude, longitude);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0) {
            helperActivity.setCurrentPhotoPath(photoGallery.get(helperActivity.getCurrentPhotoIndex()));
            //currentPhotoPath = photoGallery.get(currentPhotoIndex);
        }
    }


            //unused
    /*
    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };
    */


    public View.OnClickListener takePhoto = new View.OnClickListener() {
        public void onClick(View v) {
            helperActivity.takePicture(v, MainActivity.this, et);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //NOT USED
        /*
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));

                photoGallery = helperActivity.populateGallery(new Date(), new Date(), gallerySearchList);
                Log.d("onCreate, size", Integer.toString(photoGallery.size()));
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);

                helperActivity.displayPhoto(currentPhotoPath, imageView);
            }
        }*/
        //On image request, populate the gallery
        //then, display current index photo from the gallery array
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {


                Log.d("createImageFile", "Picture Taken");
                photoGallery = helperActivity.populateGallery(new Date(), new Date(), photoGallery, latitude, longitude);

                helperActivity.setCurrentPhotoIndex(photoGallery.size() - 1);
                helperActivity.setCurrentPhotoPath(photoGallery.get(helperActivity.getCurrentPhotoIndex()));

                //currentPhotoIndex = photoGallery.size() - 1;
                //currentPhotoPath = photoGallery.get(currentPhotoIndex);

                helperActivity.displayPhoto(helperActivity.getCurrentPhotoPath(), imageView);

                //onMainGallery = true;
                helperActivity.setOnMainGallery(true);
            }
        }
        //Call search gallery
        if (requestCode == 999 && resultCode == RESULT_OK) {
            String[] searchData = {
                    data.getStringExtra("GET_CAPTION_TEXT"),
                    data.getStringExtra("GET_FROM_DATE"),
                    data.getStringExtra("GET_TO_DATE"),
                    data.getStringExtra("GET_FROM_TIME"),
                    data.getStringExtra("GET_TO_TIME"),
                    data.getStringExtra("GET_LAT"),
                    data.getStringExtra("GET_LON")
            };
            /*
            for (int xy = 0; xy < searchData.length; xy++) {
                Log.d("Strings: ", searchData[xy]);
            }*/

            try {

                helperActivity.searchGallery(searchData, imageView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
                //--currentPhotoIndex;
                helperActivity.setCurrentPhotoIndex(helperActivity.getCurrentPhotoIndex()-1);
                break;
            case R.id.btnRight:
                //++currentPhotoIndex;
                helperActivity.setCurrentPhotoIndex(helperActivity.getCurrentPhotoIndex()+1);
                break;
            default:
                break;
        }
        Log.d("onClick", "before index 0 check");
        if (helperActivity.getCurrentPhotoIndex() < 0)
            helperActivity.setCurrentPhotoIndex(0);
        Log.d("onClick", "before index 0 check");
        if (helperActivity.getOnMainGallery() == true) {
            if (photoGallery.size() != 0) {
                Log.d("onClick", "before photogallery size check");
                if (helperActivity.getCurrentPhotoIndex() >= photoGallery.size())
                    helperActivity.setCurrentPhotoIndex(photoGallery.size() - 1);

                helperActivity.setCurrentPhotoPath(photoGallery.get(helperActivity.getCurrentPhotoIndex()));
                Log.d("photoleft, size", Integer.toString(photoGallery.size()));
            }
        } else {
            if (helperActivity.getGallerySearchList().size() != 0) {
                Log.d("onClick", "before gallerySearchList size check");
                if (helperActivity.getCurrentPhotoIndex() >= helperActivity.getGallerySearchList().size()) {
                    Log.d("onClick", "after gallerySearchList size check");
                    helperActivity.setCurrentPhotoIndex(helperActivity.getGallerySearchList().size() - 1);
                }

                helperActivity.setCurrentPhotoPath(helperActivity.getGallerySearchList().get(helperActivity.getCurrentPhotoIndex()));
                //helperActivity.setCurrentPhotoPath(gallerySearchList.get(helperActivity.getCurrentPhotoIndex()));
                Log.d("photoleft, size", Integer.toString(helperActivity.getGallerySearchList().size()));
            }
        }
        //Log.d("photoleft, index", Integer.toString(currentPhotoIndex));
        helperActivity.displayPhoto(helperActivity.getCurrentPhotoPath(), imageView);

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("inside", "onResume()");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        TextView tvLat = (TextView) findViewById(R.id.tvLat);
        TextView tvLng = (TextView) findViewById(R.id.tvLong);
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        tvLat.setText(latitude);
        tvLng.setText(longitude);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return;
    }


    /**
     * Sets the gallery search array list and display
     * Calls the populateSearchGallery() to get searched photos
     * then displays the photo
     */
    /*
    public void searchGallery(String cpt) {
        caption = cpt;
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
    */

    /**
     * Gets photos from the external storage
     * and stores them in an arraylist
     * @param minDate
     * @param maxDate
     * @return photoGallery
     */
    /*
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
    */

    /**
     * Gets photos from the external storage
     * filters the searched photos
     * and stores them in an arraylist
     * @return gallerySearchList
     */
    /*
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
    */

    //Sets the photo's bitmap
    /*
    private void displayPhoto(String path) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }
    */

    /**
     * Calls camera intent
     * attempts to create an image/photo file
     * then start activity
     * @param v
     */
    /*
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
    */

    /**
     * Gets an image file .jpg
     * formatted with a caption and timestamp
     * @return image
     * @throws IOException
     */
    /*
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
    */
}
