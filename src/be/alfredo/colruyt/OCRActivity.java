package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Prepare the taken picture for OCR and interpret it.
 *
 * @see <a href="https://github.com/GautamGupta/Simple-Android-OCR/">https://github.com/GautamGupta/Simple-Android-OCR/</a>
 */
public class OCRActivity extends Activity
{
    private static final String TAG = "OCRActivity";
    private static final String lang = "eng";

    public static final String JSON_DATA = "json_data";

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
     * TODO: Delete the image from the gallery as well
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

            // Scale the image down
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            // Load the image taken earlier
            image = BitmapFactory.decodeFile(mCurrentPhotoPath, options);

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

    /**
     * Start async task to decode the image.
     */
    private void ocrImage()
    {
        OCRTask ocrTask = new OCRTask(this);
        ocrTask.execute(image);
    }

    /**
     * Async task which starts Tesseract and decodes the image.
     */
    private class OCRTask extends AsyncTask<Bitmap, Void, String>
    {
        private TessBaseAPI baseAPI;
        private final Context context;

        public OCRTask(Context context)
        {
            this.context = context;
        }

        /**
         * Start Tesseract with earlier taken image. Cube combined is the slowest, but provides the best results.
         *
         * @param images
         * @return Decoded text
         */
        @Override
        protected String doInBackground(Bitmap... images)
        {
            baseAPI = new TessBaseAPI();
            baseAPI.setDebug(true);
            baseAPI.init(
                    Environment.getExternalStorageDirectory() + StartupActivity.TARGET_BASE_PATH,
                    lang);
            baseAPI.setImage(image);

            return baseAPI.getUTF8Text();
        }

        /**
         * When the task is done, set the recognized text and destroy Tesseract.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result)
        {
            recognizedText = result;
            Log.e(TAG, recognizedText);
            baseAPI.end();
            baseAPI = null;

            ComparisonTask comparisonTask = new ComparisonTask(context);
            comparisonTask.execute(recognizedText);
        }

    }

    private class ComparisonTask extends AsyncTask<String, Void, String>
    {
        private static final String TAG = "ComparisonTask";
        private final Context context;
        private URL url;

        public ComparisonTask(Context context)
        {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... ocrData)
        {
            if (getNetworkStatus())
            {
                try
                {
                    String response;
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("data",recognizedText);

                    response = ServerUtilities.postAndGet("http://www.janara.be/stuff/colruyt/comparison.php", map);

                    return response;
                }
                catch (IOException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
            else
            {
                // TODO: Display some error
            }
            return null;
        }

        /**
         * Get the network status
         * @return True if the network connection is OK, false otherwise.
         */
        private boolean getNetworkStatus()
        {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
            {
                return true;
            }
            else
            {
                return false;
            }

        }

        @Override
        protected void onPostExecute(String result)
        {
            Intent intent = new Intent(context, ComparisonListActivity.class);
            intent.putExtra(JSON_DATA, result);
            context.startActivity(intent);
        }
    }
}