package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /**
     * Called when the user clicks on the button to take a picture.
     * Starts the intent for the internal camera app and saves the
     * picture in the application's cache dir.
     *
     * @param view
     */
    public void takePictureHandler(View view)
    {
        if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE))
        {
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        }
    }

    /**
     * Called when the user clicks on the history button.
     * TODO: Save ticket data and show all previous purchases here
     *
     * @param view
     */
    public void historyHandler(View view)
    {
        // Show history
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @see <a href="http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html">http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html</a>
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action)
    {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
