package be.alfredo.colruyt;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Show the list of products along with a price comparison.
 */
public class ComparisonListActivity extends Activity
{
    private static final String TAG = "ComparisonListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparison_list);

        // Get the Intent's data
        Intent intent = getIntent();
        String ocrText = intent.getStringExtra(OCRActivity.JSON_DATA);

        // Create list of products
        ArrayList<ComparisonResults> comparisonResults = parseJson(ocrText);

        // Get List View
        final ListView lv = (ListView) findViewById(R.id.crListView);
        // Set the data on the List View by linking it with an adapter
        lv.setAdapter(new ComparisonBaseAdapter(this, comparisonResults));
    }

    public void toMainHandler(View view)
    {
        startActivity(new Intent(ComparisonListActivity.this, MainActivity.class));
    }

    /**
     * Take JSON string and turn it into an ArrayList of products with its price and
     * price difference, using the ComparisonResults DTO.
     *
     * @return List of products with their price and price difference
     */
    private ArrayList<ComparisonResults> parseJson(String data)
    {
        ArrayList<ComparisonResults> results = new ArrayList<ComparisonResults>();
        JSONArray ocrJson = null;

        try
        {
            ocrJson = new JSONArray(data);
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }

        try
        {
            for (int i = 0, l = ocrJson.length(); i < l; i++)
            {
                ComparisonResults cr = new ComparisonResults();

                JSONObject json_data = ocrJson.getJSONObject(i);
                cr.setProduct(json_data.getString("product"));
                cr.setPrice(json_data.getDouble("price"));
                cr.setDifference(json_data.getDouble("difference"));
                results.add(cr);
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return results;
    }
}
