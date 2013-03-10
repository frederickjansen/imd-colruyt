package be.alfredo.colruyt;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Alfredo
 * Date: 09/03/13
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class ComparisonArrayAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final String[] values;

    public ComparisonArrayAdapter(Context context, String[] values)
    {
        super(context, R.layout.comparison_list, values);
        this.context = context;
        this.values = values;
    }
}
