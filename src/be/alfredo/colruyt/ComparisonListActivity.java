package be.alfredo.colruyt;

import android.app.ListActivity;
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
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        View footer = getLayoutInflater().inflate(R.layout.comparison_list_footer, null);
        ListView listView = getListView();
        listView.addFooterView(footer);

        //setListAdapter();
    }

    private ArrayList<ComparisonResults> parseJson()
    {
        return null;
    }
}
