package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

/**
 * Copy assets to external storage on first run
 *
 * @see <a href="http://stackoverflow.com/questions/11128260/android-how-to-load-a-file-from-assets-to-sd-at-first-run">http://stackoverflow.com/questions/11128260/android-how-to-load-a-file-from-assets-to-sd-at-first-run</a>
 */
public class StartupActivity extends Activity
{
    private final static String TESSDATA_PATH = "/Colruyt/tessdata/";
    private final static String TAG = "StartupActivity";
    private String extStorageDirectory;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        firstRun();
    }

    private void firstRun()
    {
        SharedPreferences settings = this.getSharedPreferences("Colruyt", 0);
        boolean firstrun = settings.getBoolean("firstrun", true);
        if (firstrun)
        { // Checks to see if we've ran the application before
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("firstrun", false);
            e.commit();
            // If not, run these methods:
            setDirectory();
            Intent home = new Intent(StartupActivity.this, MainActivity.class);
            startActivity(home);

        }
        else
        { // Otherwise start the application here:
            Intent home = new Intent(StartupActivity.this, MainActivity.class);
            startActivity(home);
        }
    }

    /**
     * Check to see if the sdCard is mounted and create a directory w/in it
     */
    private void setDirectory()
    {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            extStorageDirectory = Environment.getExternalStorageDirectory().toString();

            File tessdata = new File(extStorageDirectory + TESSDATA_PATH); // Create a file object for the parent directory
            tessdata.mkdirs();// Have the object build the directory
            // structure, if needed.
            copyAssets(); // Then run the method to copy the file.

        }
        else if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            Context context = getApplicationContext();
            //TODO Move to strings.xml
            String message = "SD card mounted read only";
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, message, duration);
        }

    }

    /**
     * Copy the file from the assets folder to the sdCard
     */
    private void copyAssets()
    {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try
        {
            files = assetManager.list("");
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }

        for (int i = 0; i < files.length; i++)
        {
            InputStream in = null;
            OutputStream out = null;
            try
            {
                in = assetManager.open(files[i]);
                out = new FileOutputStream(extStorageDirectory + TESSDATA_PATH + files[i]);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }
}