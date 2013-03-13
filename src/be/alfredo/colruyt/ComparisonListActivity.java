package be.alfredo.colruyt;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 *
 */
public class ComparisonListActivity extends ListActivity
{
    private static final String TAG = "ComparisonListActivity";
    private String ocrText;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ocrText = intent.getStringExtra(OCRActivity.JSON_DATA);

        ArrayList<ComparisonResults> comparisonResults = parseJson();

        View footer = getLayoutInflater().inflate(R.layout.comparison_list_footer, null);
        ListView listView = getListView();
        listView.addFooterView(footer);

        setListAdapter(new ComparisonBaseAdapter(this, comparisonResults));
    }

    private ArrayList<ComparisonResults> parseJson()
    {
        return null;
    }
}
