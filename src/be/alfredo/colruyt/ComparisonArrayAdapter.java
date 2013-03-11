package be.alfredo.colruyt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 */
public class ComparisonArrayAdapter extends BaseAdapter
{
    private final Context context;
    private final ArrayList<ComparisonResults> comparisonArrayList;

    public ComparisonArrayAdapter(Context context, ArrayList<ComparisonResults> results)
    {
        this.context = context;
        this.comparisonArrayList = results;
    }

    @Override
    public int getCount()
    {
        return comparisonArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return comparisonArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.comparison_list, parent, false);
        //TextView



        return rowView;
    }
}
