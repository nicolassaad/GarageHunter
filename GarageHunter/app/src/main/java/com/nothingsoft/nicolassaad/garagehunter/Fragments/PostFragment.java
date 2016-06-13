package com.nothingsoft.nicolassaad.garagehunter.Fragments;

/**
 * Created by nicolassaad on 4/30/16.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.nothingsoft.nicolassaad.garagehunter.MainActivity;
import com.nothingsoft.nicolassaad.garagehunter.Models.GarageSale;
import com.nothingsoft.nicolassaad.garagehunter.PreviewActivity;
import com.nothingsoft.nicolassaad.garagehunter.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostFragment extends Fragment {
    private EditText editTitle;
    private EditText editDesc;
    private EditText editAddress;
    private Button postButton;
    private Button previewButton;
    private Spinner spinnerDay;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView editIcon1;
    private ImageView editIcon2;
    private ImageView editIcon3;
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

    private GarageSale garageSale;

    private String title;
    private String desc;
    private String address;
    private String dayOfWeek;

    private ArrayList<String> previewItems;

    private Firebase mFireBaseRef;
    private int counter;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int PERMISSION_REQUEST_CODE = 12345;
    public String photoFileName = "photo.jpg";
    private final String TAG = "PostFragment";
    private String imageFile1;
    private String imageFile2;
    private String imageFile3;
    int hasPermission;
//    private Intent toPreviewIntent;

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        setViews(view);
        mFireBaseRef = new Firebase(getString(R.string.firebase_address));

        setImageClickListeners(image1, 0);
        setImageClickListeners(image2, 1);
        setImageClickListeners(image3, 2);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = 0.0, lng = 0.0;
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                address = editAddress.getText().toString();

                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    checkForNulls();
                    if (checkForNulls() == 1) {
                        Log.d(TAG, addresses.get(0).getLatitude() + "");
                        Log.d(TAG, addresses.get(0).getLongitude() + "");
                        lat = addresses.get(0).getLatitude();
                        lng = addresses.get(0).getLongitude();
                        picsToString();
                        picsToString2();
                        picsToString3();
                    }

                    if (checkForNulls() == 1) {
                        garageSale = new GarageSale(editTitle.getText().toString(), editDesc.getText().toString(),
                                editAddress.getText().toString(), lat, lng, spinnerDay.getSelectedItem().toString(),
                                imageFile1, imageFile2, imageFile3);
                        mFireBaseRef.push().setValue(garageSale);

                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra(LAT_KEY, lat);
                        intent.putExtra(LNG_KEY, lng);
                        intent.putExtra(TITLE_KEY, title);
                        startActivity(intent);

                        //Clean up the PostFragment by clearing the edit text boxes and resetting the images
                        clearImages();
                        clearEditTexts();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPreviewIntent();
                if (checkForNulls() == 1) {
                    Intent toPreviewIntent = new Intent(v.getContext(), PreviewActivity.class);
                    toPreviewIntent.putStringArrayListExtra(PREVIEW_KEY, previewItems);
                    startActivity(toPreviewIntent);
                }
            }
        });
        return view;
    }

    private void setViews(View view) {
        image1 = (ImageView) view.findViewById(R.id.post_image_holder1);
        image2 = (ImageView) view.findViewById(R.id.post_image_holder2);
        image3 = (ImageView) view.findViewById(R.id.post_image_holder3);
        editIcon1 = (ImageView) view.findViewById(R.id.edit_icon1);
        editIcon2 = (ImageView) view.findViewById(R.id.edit_icon2);
        editIcon3 = (ImageView) view.findViewById(R.id.edit_icon3);
        postButton = (Button) view.findViewById(R.id.post);
        previewButton = (Button) view.findViewById(R.id.preview_post);
        editAddress = (EditText) view.findViewById(R.id.post_address);
        editTitle = (EditText) view.findViewById(R.id.post_title);
        editDesc = (EditText) view.findViewById(R.id.post_desc);
        spinnerDay = (Spinner) view.findViewById(R.id.post_spinner);
    }

    private void setImageClickListeners(ImageView image, final int position) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                    counter = position;
                    Log.d(TAG, counter + "photo.jpg is being taken");
                    onLaunchCamera(v);
                }
                if (hasPermission == PackageManager.PERMISSION_DENIED) {
                    requestUserForPermission();
                    Toast.makeText(getContext(), R.string.camera_permission, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void picsToString() {
        Bitmap bmp = Bitmap.createBitmap(bMapScaled); //image1
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, bYtE);
//        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        imageFile1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void picsToString2() {
        Bitmap bmp2 = Bitmap.createBitmap(bMapScaled2); //image2
        ByteArrayOutputStream bYtE2 = new ByteArrayOutputStream();
        bmp2.compress(Bitmap.CompressFormat.JPEG, 25, bYtE2);
//        bmp2.recycle();
        byte[] byteArray2 = bYtE2.toByteArray();
        imageFile2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
    }

    private void picsToString3() {
        Bitmap bmp3 = Bitmap.createBitmap(bMapScaled3); //image3
        ByteArrayOutputStream bYtE3 = new ByteArrayOutputStream();
        bmp3.compress(Bitmap.CompressFormat.JPEG, 25, bYtE3);
//        bmp3.recycle();
        byte[] byteArray3 = bYtE3.toByteArray();
        imageFile3 = Base64.encodeToString(byteArray3, Base64.DEFAULT);
    }

    /**
     * This method stores the user's data into variables that will be passed to the PreviewActivity
     * in an intent. We then call checkForNull() which makes sure that all required fields have been filled
     *
     * @return
     */
    private void settingPreviewIntent() {
        previewItems = new ArrayList<>();
        title = editTitle.getText().toString();
        desc = editDesc.getText().toString();
        address = editAddress.getText().toString();
        dayOfWeek = (String) spinnerDay.getSelectedItem();
        previewItems.add(0, title);
        previewItems.add(1, desc);
        previewItems.add(2, address);
        previewItems.add(3, dayOfWeek);
        if (takenPhoto1 == null || takenPhoto2 == null || takenPhoto3 == null) {
            //User is asked to enter three pictures
            Toast.makeText(getContext(), R.string.please_add_pics, Toast.LENGTH_SHORT).show();
        } else {

            previewItems.add(4, takenPhoto1.toString());
            previewItems.add(5, takenPhoto2.toString());
            previewItems.add(6, takenPhoto3.toString());
        }

        checkForNulls();
    }

    private int checkForNulls() {
        title = editTitle.getText().toString();
        address = editAddress.getText().toString();
        if (editTitle.getText().toString().isEmpty()) {
            //User is asked to enter a title
            editTitle.setError(getString(R.string.please_enter_title));
            return -1;
        }
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        address = editAddress.getText().toString();

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() == 0) {
                //User is asked to enter a proper address
                editAddress.setError(getString(R.string.enter_valid_address));
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (takenPhoto1 == null || takenPhoto2 == null || takenPhoto3 == null) {
            //User is asked to enter three pictures
            Toast.makeText(getContext(), R.string.please_add_pics, Toast.LENGTH_SHORT).show();
            return -1;
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
        editIcon1.setVisibility(View.INVISIBLE);
        editIcon2.setVisibility(View.INVISIBLE);
        editIcon3.setVisibility(View.INVISIBLE);
    }

    private void clearImages() {
        image1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        image2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        image3.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    /**
     * Method that launches the camera allowing the user to take a photo. Each gets stored separately
     * but adding the counter to the photoFileName.
     *
     * @param view
     */
    public void onLaunchCamera(View view) {
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
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(counter + photoFileName);
                Log.d(TAG, takenPhotoUri.toString());
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // Load the taken image into a preview
                Log.d(TAG, counter + "photo.jpg is being processed");
                if (counter == 0) {
                    takenPhoto1 = getPhotoFileUri(counter + photoFileName);
                    try {
                        bMapScaled = handleSamplingAndRotationBitmap(getContext(), takenPhoto1);
                        bMapScaled = rotateImage(bMapScaled, takenPhoto1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image1.setImageBitmap(bMapScaled);
                    editIcon1.setVisibility(View.VISIBLE);
                } else if (counter == 1) {
                    takenPhoto2 = getPhotoFileUri(counter + photoFileName);
                    try {
                        bMapScaled2 = handleSamplingAndRotationBitmap(getContext(), takenPhoto2);
                        bMapScaled2 = rotateImage(bMapScaled2, takenPhoto2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image2.setImageBitmap(bMapScaled2);
                    editIcon2.setVisibility(View.VISIBLE);
                } else if (counter == 2) {
                    takenPhoto3 = getPhotoFileUri(counter + photoFileName);
                    try {
                        bMapScaled3 = handleSamplingAndRotationBitmap(getContext(), takenPhoto3);
                        bMapScaled3 = rotateImage(bMapScaled3, takenPhoto3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image3.setImageBitmap(bMapScaled3);
                    editIcon3.setVisibility(View.VISIBLE);
                }
            } else { // Result was a failure
                Toast.makeText(getContext(), R.string.pic_wasnt_taken, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        return img;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * rotateImage method takes in a bitmap and uri and returns a correctly rotated bitmap.
     * This method checks the orientation of the taken image and rotates it correctly for it to display
     * the right way
     *
     * @param bMapScaled
     * @param takenPhoto
     * @return
     */
    public Bitmap rotateImage(Bitmap bMapScaled, Uri takenPhoto) {
        try {
            ExifInterface exif = new ExifInterface(takenPhoto.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                bMapScaled = Bitmap.createScaledBitmap(bMapScaled, 586, 450, true);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                bMapScaled = Bitmap.createScaledBitmap(bMapScaled, 586, 450, true);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                bMapScaled = Bitmap.createScaledBitmap(bMapScaled, 586, 450, true);
            } else {
                bMapScaled = Bitmap.createScaledBitmap(bMapScaled, 450, 586, true);
            }
            bMapScaled = Bitmap.createBitmap(bMapScaled, 0, 0, bMapScaled.getWidth(), bMapScaled.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {

        }
        return bMapScaled;
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
                Log.d(APP_TAG, getString(R.string.failed_create_dir));
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
     * <p/>
     * If a phone is running older OS then Android M, we simply return because
     * those phone are using the OLD permission model and permissions are granted at
     * INSTALL time.
     */
    @TargetApi(23)
    private void requestUserForPermission() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.M) {
            // This OS version is lower then Android M, therefore we have old permission model and should not ask for permission
            return;
        }
        String[] permissions = new String[]{CAMERA_PERMISSION};
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);

    }

}