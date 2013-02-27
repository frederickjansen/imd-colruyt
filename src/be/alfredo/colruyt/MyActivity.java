package be.alfredo.colruyt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MyActivity extends Activity
{
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /**
     * Called when the user clicks on the button to take a picture
     * @param view
     */
    public void takePictureHandler(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Intercepting callback from the camera intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                // Image captured and saved to fileUri specified in the Intent
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

    public void historyHandler(View view)
    {
        // Show history
    }
}
