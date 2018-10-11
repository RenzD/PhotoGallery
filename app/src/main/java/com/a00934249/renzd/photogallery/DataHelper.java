package com.a00934249.renzd.photogallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataHelper extends AppCompatActivity{

    public ArrayList<String> getGallerySearchList() {
        return gallerySearchList;
    }

    public void setGallerySearchList(ArrayList<String> gallerySearchList) {
        this.gallerySearchList = gallerySearchList;
    }

    private ArrayList<String> gallerySearchList;

    final static int REQUEST_IMAGE_CAPTURE = 1;

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    private String currentPhotoPath = null;

    public int getCurrentPhotoIndex() {
        return currentPhotoIndex;
    }

    public void setCurrentPhotoIndex(int currentPhotoIndex) {
        this.currentPhotoIndex = currentPhotoIndex;
    }

    private int currentPhotoIndex = 0;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String caption;
    public String sFromDate;
    public String sToDate;
    public String sLat;
    public String sLon;
    public String sFromTime;
    public String sToTime;

    public boolean isFilterEmpty;
    public boolean filtered;

    public Boolean getOnMainGallery() {
        return onMainGallery;
    }

    public void setOnMainGallery(Boolean onMainGallery) {
        this.onMainGallery = onMainGallery;
    }

    public Boolean onMainGallery = true;

    public DataHelper(){

    }

    public String latitude;

    public String longitude;

    /**
     * Sets the gallery search array list and display
     * Calls the populateSearchGallery() to get searched photos
     * then displays the photo
     */
    public void searchGallery(String[] searchData, ImageView imageView) throws ParseException {
        caption = searchData[0];
        sFromDate = searchData[1];
        sToDate = searchData[2];
        sFromTime = searchData[3];
        sToTime = searchData[4];
        sLat= searchData[5];
        sLon = searchData[6];

        Log.d("Before", "populateSearchGallery");
        gallerySearchList = populateSearchGallery(new Date(), new Date());
        Log.d("Passed: ", "populateSearchGallery");

        if (gallerySearchList.size() != 0) {
            currentPhotoIndex = 0;
            Log.d("Passed", Integer.toString(gallerySearchList.size()) + " SIZE");
            currentPhotoPath = gallerySearchList.get(currentPhotoIndex);
            Log.d("Passed: ", "gallerySearchList");
            displayPhoto(currentPhotoPath, imageView);
            Log.d("Passed: ", "displayPhoto");
            onMainGallery = false;
        } else {
            onMainGallery = true;
        }
        setCurrentPhotoIndex(0);
    }

    /**
     * Gets photos from the external storage
     * filters the searched photos
     * and stores them in an arraylist
     * @return gallerySearchList
     */
    public ArrayList<String> populateSearchGallery(Date minDate, Date maxDate) throws ParseException {
        isFilterEmpty = false;
        filtered = false;


        ArrayList<String> filteredGallery = new ArrayList<String>();

        //Get the file list
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.a00934249.renzd.photogallery/files/Pictures");
        //Initialize a new gallery search list START HERE
        gallerySearchList = new ArrayList<String>();  //SET A NEW GALLERY FOR THE SEARCH LIST
        //List all the files into an array
        File[] fList = file.listFiles();            //GET THE FILE LIST FROM THE PHONE
        //Null check
        if (fList != null) {

            //If there is a caption search, set to filtered
            if (!caption.equals("")) {          //GET YOUR INPUT CAPTION
                filtered = true;
                String fileName = ""; //End string from the filepath will be inserted here
                //SCAN THROUGH ALL FILE LIST
                for (File f : fList) {      //LOOP THROUGH THE FILE
                    //CHECK FILENAME AND SPLIT
                    // D/Filename:
                    String imageName[] = f.getPath().split("/");//CHECK FILENAME    D/Filename: JPEG_topleft_20180926_172446_200779324.jpg
                    //GET LAST SPLIT(imageName.length-1) STRING = JPEG_topleft_20180926_172446_200779324.jpg
                    fileName = imageName[imageName.length-1];
                    Log.d("Filename", fileName);

                    //SPLIT THE FILE NAME INTO findCaption[]
                    //RESULT:
                    //JPEG          0
                    //Caption       1
                    //20180926      2
                    //173351        3
                    //49.00000000   4
                    //122.00000000  5
                    //-71506678.jpg
                    String findCaption[] = fileName.split("_");
                    for (int xy = 0; xy < findCaption.length; xy++) {
                        Log.d("fileName: ", findCaption[xy]);
                    }
                    //IF CAPTION MATCHES, ADD THIS FILE(f.getPath()) to gallerySearchList
                    if (findCaption[1].equals(caption)) {
                        filteredGallery.add(f.getPath());
                        //Log.d("Path: ", f.getPath());
                    }
                }
                //If its filteredGallery is empty, return
                if (filteredGallery.size() == 0) {
                    isFilterEmpty = true;
                } else {
                    isFilterEmpty = false;
                    gallerySearchList = filteredGallery;
                }
            }

            Log.d("DateFilter", String.valueOf(isFilterEmpty));
            if (isFilterEmpty == false && !sFromDate.equals("") && !sToDate.equals("")) {
                if (filtered == false) {
                    filtered = true;
                    filteredGallery = new ArrayList<String>();
                    if (sFromDate.length() == 8 && sToDate.length() == 8) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        Date fromDate = sdf.parse(sFromDate + "_" + sFromTime);
                        Date toDate = sdf.parse(sToDate + "_" + sToTime);

                        String fileName = "";
                        for (File f : fList) {
                            String imageName[] = f.getPath().split("/");
                            fileName = imageName[imageName.length - 1];

                            String findDate[] = fileName.split("_");
                            Date date = sdf.parse(findDate[2] + "_" + findDate[3]);

                            Log.d("Date1: ", String.valueOf(date));
                            Log.d("Date2: ", String.valueOf(fromDate));

                            if (fromDate.before(date) && toDate.after(date)) {
                                filteredGallery.add(f.getPath());
                            }
                        }
                        Log.d("before set gallery: ", "1");
                        gallerySearchList = filteredGallery;
                        if (gallerySearchList.size() == 0) {
                            isFilterEmpty = true;
                            Log.d("before set gallery: ", "2");
                        } else {
                            isFilterEmpty = false;
                            gallerySearchList = filteredGallery;
                            Log.d("before set gallery: ", "3");
                        }

                    } else {
                        isFilterEmpty = true;
                    }
                } else {
                    filteredGallery = new ArrayList<String>();
                    if (sFromDate.length() == 8 && sToDate.length() == 8) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        Date fromDate = sdf.parse(sFromDate + "_" + sFromTime);
                        Date toDate = sdf.parse(sToDate + "_" + sToTime);

                        String fileName = "";
                        for (String f : gallerySearchList) {
                            String imageName[] = f.split("/");
                            fileName = imageName[imageName.length - 1];

                            String findDate[] = fileName.split("_");
                            Date date = sdf.parse(findDate[2] + "_" + findDate[3]);

                            Log.d("Date1: ", String.valueOf(date));
                            Log.d("Date2: ", String.valueOf(fromDate));
                            Log.d("Date3: ", String.valueOf(toDate));


                            if (fromDate.before(date) && toDate.after(date)) {
                                filteredGallery.add(f);
                            }
                        }
                        Log.d("before set gallery: ", "1");
                        gallerySearchList = filteredGallery;
                        if (gallerySearchList.size() == 0) {
                            isFilterEmpty = true;
                            Log.d("before set gallery: ", "2");
                        } else {
                            isFilterEmpty = false;
                            gallerySearchList = filteredGallery;
                            Log.d("before set gallery: ", "3");
                        }

                    } else {
                        isFilterEmpty = true;
                    }
                }
            }
            Log.d("isFilterEmpty: ", String.valueOf(isFilterEmpty));
            if (isFilterEmpty == false && !sLat.equals("") && !sLon.equals("")) {
                if (filtered == false) {
                    filtered = true;

                    filteredGallery = new ArrayList<String>();
                    if (sLat.length() != 0 && sLon.length() != 0) {
                        double numLat = Double.parseDouble(sLat);
                        double numLon = Double.parseDouble(sLon);
                        double imageLat;
                        double imageLon;

                        String fileName = "";
                        for (File f : fList) {
                            String imageName[] = f.getPath().split("/");
                            fileName = imageName[imageName.length - 1];

                            String loc[] = fileName.split("_");

                            try {
                                imageLat = Double.parseDouble(loc[4]);
                                imageLon = Double.parseDouble(loc[5]);

                                if (imageLat >= numLat - 0.25 && imageLat <= numLat + 0.25) {
                                    if (imageLat >= numLon - 0.25 && imageLon <= numLon + 0.25) {
                                        filteredGallery.add(f.getPath());
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        gallerySearchList = filteredGallery;
                        if (gallerySearchList.size() == 0) {
                            isFilterEmpty = true;
                        } else {
                            isFilterEmpty = false;
                            gallerySearchList = filteredGallery;
                        }

                    } else {
                        isFilterEmpty = true;
                    }
                } else {
                    filteredGallery = new ArrayList<String>();
                    if (sLat.length() != 0 && sLon.length() != 0) {
                        double numLat = Double.parseDouble(sLat);
                        double numLon = Double.parseDouble(sLon);
                        double imageLat;
                        double imageLon;

                        String fileName = "";
                        for (String f : gallerySearchList) {
                            String imageName[] = f.split("/");
                            fileName = imageName[imageName.length - 1];

                            String loc[] = fileName.split("_");

                            try {
                                imageLat = Double.parseDouble(loc[4]);
                                imageLon = Double.parseDouble(loc[5]);
                                if (imageLat >= numLat - 0.25 && imageLat <= numLat + 0.25) {
                                    if (imageLat >= numLon - 0.25 && imageLon <= numLon + 0.25) {
                                        filteredGallery.add(f);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }
                        gallerySearchList = filteredGallery;
                        if (gallerySearchList.size() == 0) {
                            isFilterEmpty = true;
                        } else {
                            isFilterEmpty = false;
                            gallerySearchList = filteredGallery;
                        }

                    } else {
                        isFilterEmpty = true;
                    }
                }
            }
        }
        //RETURNS 0
        Log.d("RETURN", Integer.toString(gallerySearchList.size()));
        return gallerySearchList;
    }

    private void checkDate() {

    }

    /**
     * Gets photos from the external storage
     * and stores them in an arraylist
     * @param minDate
     * @param maxDate
     * @return photoGallery
     */
    public ArrayList<String> populateGallery(Date minDate, Date maxDate, ArrayList<String> photoGallery, String lat, String lon) {
        latitude = lat;
        longitude = lon;
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

    public void displayPhoto(String path, ImageView imageView) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    /**
     * Calls camera intent
     * attempts to create an image/photo file
     * then start activity
     * @param v
     */
    public void takePicture(View v, Activity a, EditText et) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(a.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(a, et);
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
                a.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Gets an image file .jpg
     * formatted with a caption and timestamp
     * @return image
     * @throws IOException
     */
    public File createImageFile(Activity a, EditText et) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String caption = et.getText().toString();
        String imageFileName = "JPEG_" + caption + "_" + timeStamp + "_" + latitude + "_" + longitude + "_";
        File dir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        //Log.d("createImageFile", currentPhotoPath + " DONE");
        return image;
    }
}
