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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_wait);
        firstRun();
    }

    /**
     * When first running the application, copy the needed files from the assets folder
     * to the SD card. On the next run, skip this step and boot the application.
     */
    private void firstRun()
    {
        SharedPreferences settings = this.getSharedPreferences("Colruyt", 0);
        boolean firstrun = settings.getBoolean("firstrun", true);

        if (firstrun) // Checks to see if we've ran the application before
        {
            // Add flag to app settings
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("firstrun", false);
            e.commit();

            extStorageDirectory = Environment.getExternalStorageDirectory();
            createDirectories("");

            Intent home = new Intent(StartupActivity.this, MainActivity.class);
            startActivity(home);
        }
        else
        {
            // Otherwise simply start the app
            Intent home = new Intent(StartupActivity.this, MainActivity.class);
            startActivity(home);
        }
    }

    /**
     * Copy all files and folders from the assets folder to the SD card. This method loops through
     * all of the files/folders in assets, recreates the directories and finally copies over the
     * files. To detect whether we're dealing with a file or a folder, the length of the path in
     * assets is checked. This means that empty folders should be avoided, otherwise they'll be
     * confused with files.
     *
     * TODO: Move this into a separate thread to make the UI responsive
     * TODO: Research if empty folders are really a problem and what the best solution is
     *
     * @param path The subdirectory of the assets folder in which to look.
     */
    private void createDirectories(String path)
    {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            assetManager = this.getAssets();
            String assets[] = null;
            try
            {
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

                        // Don't copy the default apk folders
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
     *
     * @param path The location of the file to copy.
     */
    private void copyFile(String path)
    {
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