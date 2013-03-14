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
 *
 */
public class ComparisonListActivity extends Activity
{
    private static final String TAG = "ComparisonListActivity";
    private String ocrText;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparison_list);

        Intent intent = getIntent();
        ocrText = intent.getStringExtra(OCRActivity.JSON_DATA);

        ArrayList<ComparisonResults> comparisonResults = parseJson();

        final ListView lv = (ListView) findViewById(R.id.crListView);
        lv.setAdapter(new ComparisonBaseAdapter(this, comparisonResults));
    }

    public void toMainHandler(View view)
    {
        startActivity(new Intent(ComparisonListActivity.this, MainActivity.class));
    }

    private ArrayList<ComparisonResults> parseJson()
    {
        ArrayList<ComparisonResults> results = new ArrayList<ComparisonResults>();
        JSONArray ocrJson = null;

        try
        {
            ocrJson = new JSONArray(ocrText);
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }

        try
        {
            for (int i = 0; i < ocrJson.length(); i++)
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
