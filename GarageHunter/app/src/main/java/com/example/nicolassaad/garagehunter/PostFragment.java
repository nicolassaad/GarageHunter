package com.example.nicolassaad.garagehunter;

/**
 * Created by nicolassaad on 4/30/16.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostFragment extends Fragment {
    private EditText editTitle;
    private EditText editDesc;
    private EditText editAddress;
    private Button addPic;
    private Spinner spinnerDay;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private Uri takenPhotoUri;
    private Bitmap takenImage;
    private Uri takenPhoto1;
    private Uri takenPhoto2;
    private Uri takenPhoto3;
    private Bitmap bMapScaled;
    private Bitmap bMapScaled2;
    private Bitmap bMapScaled3;
    public static final String PREVIEW_KEY = "PrevKey";
    public static final String LAT_KEY = "LatKey";
    public static final String LNG_KEY = "LonKey";
    public static final String TITLE_KEY = "TitleKey";

    GarageSale garageSale;

    private String title;
    private String desc;
    private String address;
    private String dayOfWeek;

    ArrayList<String> previewItems;

    Firebase mFirebaseRef;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int PERMISSION_REQUEST_CODE = 12345;
    public String photoFileName = "photo.jpg";
    private int counter = 0;

    private String imageFile1;
    private String imageFile2;
    private String imageFile3;

    public PostFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Button postButton = (Button) view.findViewById(R.id.post);
        Button previewButton = (Button) view.findViewById(R.id.preview_post);
        addPic = (Button) view.findViewById(R.id.add_pic_button);
        editAddress = (EditText) view.findViewById(R.id.post_address);
        editTitle = (EditText) view.findViewById(R.id.post_title);
        editDesc = (EditText) view.findViewById(R.id.post_desc);
        spinnerDay = (Spinner) view.findViewById(R.id.post_spinner);
        previewItems = new ArrayList<>();

        mFirebaseRef = new Firebase("https://garagesalehunter.firebaseio.com");

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);

            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = 0.0, lng = 0.0;
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                address = editAddress.getText().toString();

                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses.size() == 0 && editTitle.getText().toString().isEmpty()) {
                        editAddress.setError("Please enter valid address");
                        editTitle.setError("Please enter a title");
                    } else if (addresses.size() == 0) {
                        editAddress.setError("Please enter a valid address");
                    }else if (editTitle.getText().toString().isEmpty()) {
                        editTitle.setError("Please enter a title");
                    } else {
                        Log.d("PostFragment", addresses.get(0).getLatitude() + "");
                        Log.d("PostFragment", addresses.get(0).getLongitude() + "");
                        lat = addresses.get(0).getLatitude();
                        lng = addresses.get(0).getLongitude();

                    if (settingPreviewIntent() == -1) {
                        Toast.makeText(getContext(), "Please add three pictures", Toast.LENGTH_LONG).show();
                    } else {
                        picsToString();
                        picsToString2();
                        picsToString3();
                    }

                        garageSale = new GarageSale(editTitle.getText().toString(), editDesc.getText().toString(),
                                editAddress.getText().toString(), lat, lng, spinnerDay.getSelectedItem().toString(),
                                imageFile1, imageFile2, imageFile3);
                        mFirebaseRef.push().setValue(garageSale);

                        settingPreviewIntent();

//                        if (image1 == null || image2 == null || image3 == null) {
//                            Toast.makeText(getContext(), "Please add all three pictures", Toast.LENGTH_SHORT).show();
//                        }
                        if (settingPreviewIntent() == 1) {
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.putExtra(LAT_KEY, lat);
                            intent.putExtra(LNG_KEY, lng);
                            intent.putExtra(TITLE_KEY, title);
                            startActivity(intent);

                        } else if (settingPreviewIntent() == -2) {
                                editTitle.setError("Please enter a title");
                            Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                        } else if (settingPreviewIntent() == -3) {
                            Toast.makeText(getContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
                            editAddress.setError("Please enter an address");
                        } else if (settingPreviewIntent() == -1) {
                            Toast.makeText(getContext(), "Please add all three pictures", Toast.LENGTH_SHORT).show();
                        } else {
                            image1.setBackground(getResources().getDrawable(R.drawable.image_holder));
                            image2.setBackground(getResources().getDrawable(R.drawable.image_holder));
                            image3.setBackground(getResources().getDrawable(R.drawable.image_holder));
                            clearEditTexts();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // TODO: 5/16/16 REFACTOR NULL CHECKS INTO THEIR OWN METHOD THAT PREVIEW AND POST BUTTONS CAN BOTH USE 
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPreviewIntent();
                if (settingPreviewIntent() == 1) {
                    Intent toPreviewIntent = new Intent(v.getContext(), PreviewActivity.class);
                    toPreviewIntent.putStringArrayListExtra(PREVIEW_KEY, previewItems);
                    startActivity(toPreviewIntent);
                } else if (settingPreviewIntent() == -1) {
                    Toast.makeText(getContext(), "Please add three pictures", Toast.LENGTH_LONG).show();
                } else if (settingPreviewIntent() == -2) {
                    editTitle.setError("Please enter a title");
//                    editAddress.setError("Please enter an address");
                }
            }
        });

        return view;
    }

    private void picsToString() {
        Bitmap bmp = Bitmap.createBitmap(bMapScaled); //image1
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        imageFile1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void picsToString2() {
        Bitmap bmp2 = Bitmap.createBitmap(bMapScaled2); //image2
        ByteArrayOutputStream bYtE2 = new ByteArrayOutputStream();
        bmp2.compress(Bitmap.CompressFormat.PNG, 100, bYtE2);
        bmp2.recycle();
        byte[] byteArray2 = bYtE2.toByteArray();
        imageFile2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
    }

    private void picsToString3() {
        Bitmap bmp3 = Bitmap.createBitmap(bMapScaled3); //image3
        ByteArrayOutputStream bYtE3 = new ByteArrayOutputStream();
        bmp3.compress(Bitmap.CompressFormat.PNG, 100, bYtE3);
        bmp3.recycle();
        byte[] byteArray3 = bYtE3.toByteArray();
        imageFile3 = Base64.encodeToString(byteArray3, Base64.DEFAULT);
    }

    /**
     * This method stores the user's data into variables that will be passed to the PreviewActivity
     * in an intent. Here, several checks are made to make sure that the user has taken 3 photos as well
     * as entered a title and address
     * @return
     */
    private int settingPreviewIntent() {
        title = editTitle.getText().toString();
        desc = editDesc.getText().toString();
        address = editAddress.getText().toString();
        dayOfWeek = (String) spinnerDay.getSelectedItem();

        previewItems.add(0, title);
        previewItems.add(1, desc);
        previewItems.add(2, address);
        previewItems.add(3, dayOfWeek);
// TODO: 5/16/16 REFECTOR NULL CHECKS INTO THEIR OWN METHOD THAT BOTH POST AND PREVIEW BUTTON CAN CALL 
        if (takenPhoto1 == null || takenPhoto2 == null || takenPhoto3 == null) {
            return -1;
        } else {
            previewItems.add(4, takenPhoto1.toString());
            previewItems.add(5, takenPhoto2.toString());
            previewItems.add(6, takenPhoto3.toString());
        }

        title = editTitle.getText().toString();
        address = editAddress.getText().toString();
        if (editTitle.getText().toString().isEmpty() && editAddress.getText().toString().isEmpty()) {
            editTitle.setError("Please enter a title");
            editAddress.setError("Please enter an address");
            return -2;
        }

        if (editTitle.getText().toString().isEmpty()) {
            editTitle.setError("Please enter a title");
            return -2;
        }

        if (editAddress.getText().toString().isEmpty()) {
            editAddress.setError("Please enter an address");
            return -3;
        }

        return 1;
    }

    /**
     * This method is run when the user presses the post button. It clears all the views so another
     * post can be entered.
     */
    private void clearEditTexts() {
        editTitle.setText("");
        editDesc.setText("");
        editAddress.setText("");
        spinnerDay.dispatchDisplayHint(View.VISIBLE);
    }

    /**
     * Method that launches the camera allowing the user to take a photo. Each gets stored separately
     * but adding the counter to the photoFileName.
     * @param view
     */
    public void onLaunchCamera(View view) {
        requestUserForPermission();
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(counter + photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * Method that takes the image that the user took with the camera and assigns it to a certain
     * view in the placeholder images based on a counter
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                takenPhotoUri = getPhotoFileUri(counter + photoFileName);
                Log.d("PostFragment", takenPhotoUri.toString());
                // by this point we have the camera photo on disk
                takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // Load the taken image into a preview
                if (counter == 0) {
                    takenPhoto1 = getPhotoFileUri(counter + photoFileName);
                    image1 = (ImageView) getActivity().findViewById(R.id.post_image_holder1);
                    bMapScaled = Bitmap.createScaledBitmap(takenImage, 380, 494, true);
                    image1.setImageBitmap(bMapScaled);
                    counter++;
                } else if (counter == 1) {
                    takenPhoto2 = getPhotoFileUri(counter + photoFileName);
                    image2 = (ImageView) getActivity().findViewById(R.id.post_image_holder2);
                    bMapScaled2 = Bitmap.createScaledBitmap(takenImage, 380, 494, true);
                    image2.setImageBitmap(bMapScaled2);
                    counter++;
                } else if (counter == 2) {
                    takenPhoto3 = getPhotoFileUri(counter + photoFileName);
                    image3 = (ImageView) getActivity().findViewById(R.id.post_image_holder3);
                    bMapScaled3 = Bitmap.createScaledBitmap(takenImage, 380, 494, true);
                    image3.setImageBitmap(bMapScaled3);
                    addPic.setClickable(false);
                    addPic.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    counter++;
                }
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * This method will request the user for camera permission
     *
     * If a phone is running older OS then Android M, we simply return because
     * those phone are using the OLD permission model and permissions are granted at
     * INSTALL time.
     */
    @TargetApi(23)
    private void requestUserForPermission(){
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.M){
            // This OS version is lower then Android M, therefore we have old permission model and should not ask for permission
            return;
        }
        String[] permissions = new String[]{CAMERA_PERMISSION};
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);

    }

}