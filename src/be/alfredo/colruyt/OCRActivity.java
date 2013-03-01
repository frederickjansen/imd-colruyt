package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

/**
 * Ready the taken picture for OCR and interpret it.
 *
 * @see https://github.com/GautamGupta/Simple-Android-OCR/
 */
public class OCRActivity extends Activity
{
    private static final String TAG = "OCRActivity";
    private String mCurrentPhotoPath;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_wait);

        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if (setupTrainedData())
        {

        }
        else
        {
            Log.d(TAG, "Can't setup trained data");
        }
    }

    private Boolean setupTrainedData()
    {
        return false;
    }

    private void rotateImage()
    {
        try
        {
            ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
            int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;

            switch (exifRotation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                {
                    rotate = 90;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180:
                {
                    rotate = 180;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_270:
                {
                    rotate = 270;
                    break;
                }
                default:
                    break;
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }
}