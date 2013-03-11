package be.alfredo.colruyt;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 *
 */
public class ComparisonListActivity extends ListActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        View footer = getLayoutInflater().inflate(R.layout.comparison_list_footer, null);
        ListView listView = getListView();
        listView.addFooterView(footer);

        //setListAdapter();
    }
}
