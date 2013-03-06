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
 */
public class StartupActivity extends Activity
{
    public static final String TARGET_BASE_PATH = "/Colruyt";
    public static final String TESSDATA_PATH = "/Colruyt/tessdata";
    private static final String TAG = "StartupActivity";
    private File extStorageDirectory = null;
    private AssetManager assetManager = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        firstRun();
    }

    private void firstRun()
    {
        SharedPreferences settings = this.getSharedPreferences("Colruyt", 0);
        boolean firstrun = settings.getBoolean("firstrun", true);
        // TODO: Remove this for deploy
        if (true)
        { // Checks to see if we've ran the application before
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("firstrun", false);
            e.commit();
            // If not, run these methods:
            extStorageDirectory = Environment.getExternalStorageDirectory();
            createDirectories("");
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
    private void createDirectories(String path)
    {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            assetManager = this.getAssets();
            String assets[] = null;
            try
            {
                Log.e(TAG, "createDirectories() " + path);
                assets = assetManager.list(path);

                // If length is 0, it's a file or an empty directory
                if (assets.length == 0)
                {
                    copyFile(path);
                }
                else
                {
                    String fullPath = extStorageDirectory + TARGET_BASE_PATH + File.separator + path;
                    File dir = new File(fullPath);

                    if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                    {
                        if (!dir.mkdirs())
                        {
                            Log.e(TAG, "Could not create dir " + fullPath);
                        }
                    }

                    for (int i = 0; i < assets.length; ++i)
                    {
                        String p;
                        if (path.equals(""))
                        {
                            p = "";
                        }
                        else
                        {
                            p = path + "/";
                        }

                        if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                        {
                            createDirectories(p + assets[i]);
                        }
                    }
                }
            }
            catch (IOException ex)
            {
                Log.e(TAG, "I/O Exception ", ex);
            }
        }
        else
        {
            Context context = getApplicationContext();
            String message = getString(R.string.sd_read_only);
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, message, duration);
        }

    }

    /**
     * Copy the file from the assets folder to the sdCard
     */
    private void copyFile(String path)
    {
        Log.e(TAG, "Copying file " + path);

        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = assetManager.open(path);
            out = new FileOutputStream(extStorageDirectory + TARGET_BASE_PATH + File.separator + path);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }

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