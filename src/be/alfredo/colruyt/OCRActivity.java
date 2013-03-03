package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Prepare the taken picture for OCR and interpret it.
 *
 * @see <a href="https://github.com/GautamGupta/Simple-Android-OCR/">https://github.com/GautamGupta/Simple-Android-OCR/</a>
 */
public class OCRActivity extends Activity
{
    private static final String TAG = "OCRActivity";
    private static final String lang = "nld";

    private String mCurrentPhotoPath;
    private Bitmap image;
    private String recognizedText;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_wait);

        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra(CameraActivity.EXTRA_MESSAGE);

        rotateImage();
        ocrImage();
    }

    /**
     * Delete the photo taken by the user
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        deleteFile(mCurrentPhotoPath);
    }

    /**
     * Rotate the image upright and convert it to ARGB 8888 for Tesseract
     */
    private void rotateImage()
    {
        try
        {
            ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
            int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            // Load the image taken earlier
            image = BitmapFactory.decodeFile(mCurrentPhotoPath, null);

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

            if (rotate != 0)
            {
                // Getting width & height of the given image.
                int w = image.getWidth();
                int h = image.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false);
            }

            // Convert to ARGB_8888, required by tess
            image = image.copy(Bitmap.Config.ARGB_8888, true);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    private void ocrImage()
    {
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(Environment.getExternalStorageDirectory() + StartupActivity.ALBUM_NAME, lang);
        baseApi.setImage(image);

        recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        Log.e(TAG, recognizedText);
        //Log.v(TAG, "OCR Started!");
    }
}