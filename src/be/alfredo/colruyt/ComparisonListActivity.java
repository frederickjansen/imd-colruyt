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

        View header = getLayoutInflater().inflate(R.layout.comparison_list_header, null);
        View footer = getLayoutInflater().inflate(R.layout.comparison_list_footer, null);
        ListView listView = getListView();
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        setListAdapter(new ComparisonBaseAdapter(this, comparisonResults));
    }

    public void toMainHandler(View view)
    {
        startActivity(new Intent(ComparisonListActivity.this, MainActivity.class));
    }

    private ArrayList<ComparisonResults> parseJson()
    {
        ArrayList<ComparisonResults> results = new ArrayList<ComparisonResults>();

        ComparisonResults cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        cr = new ComparisonResults();
        cr.setProduct("FORE 12X50GR");
        cr.setPrice(1.89);
        cr.setDifference(0.10);
        results.add(cr);

        return results;
    }
}
