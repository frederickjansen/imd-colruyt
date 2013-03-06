package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Launch the built-in camera app and save the image.
 */
public class CameraActivity extends Activity
{
    private static final String TAG = "CameraActivity";
    private static final int ACTION_TAKE_PHOTO = 100;
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String EXTRA_MESSAGE = "be.alfredo.colruyt.MESSAGE";

    private String mCurrentPhotoPath;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startCameraIntent();
    }

    /**
     * Start the built-in camera app.
     */
    private void startCameraIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try
        {
            f = setUpPhotoFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            mCurrentPhotoPath = null;
            f = null;
        }
    }

    /**
     * Create a temporary file to which the image will be written.
     *
     * @return Temporary file to which the image will be written
     * @throws java.io.IOException
     */
    private File setUpPhotoFile() throws IOException
    {
        File imageF = File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, getAlbumDir());;
        mCurrentPhotoPath = imageF.getAbsolutePath();

        return imageF;
    }

    /**
     * Get the directory where the image will be temporarily stored in.
     *
     * @return Location of directory to store picture in
     */
    private File getAlbumDir()
    {
        File albumDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            albumDir = new File(Environment.getExternalStorageDirectory() + StartupActivity.TARGET_BASE_PATH);

            if (albumDir != null)
            {
                // This album should already been created by StartupActivity, but we'll check just in case
                if (!albumDir.mkdirs())
                {
                    if (!albumDir.exists())
                    {
                        Log.v(TAG, "Can't create external dir " + StartupActivity.TARGET_BASE_PATH);
                        finish();
                    }
                }
            }
        }
        else
        {
            Log.v(TAG, "External storage is not mounted");
            finish();
        }

        return albumDir;
    }

    /**
     * Intercepting callback from the camera intent
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ACTION_TAKE_PHOTO)
        {
            if (resultCode == RESULT_OK)
            {
                Intent ocrIntent = new Intent(this, OCRActivity.class);
                ocrIntent.putExtra(EXTRA_MESSAGE, mCurrentPhotoPath);
                startActivity(ocrIntent);
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // User cancelled the image capture
            }
            else
            {
                // Image capture failed, advise user
            }
        }
    }
}