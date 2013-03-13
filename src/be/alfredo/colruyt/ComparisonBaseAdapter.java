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
public class ComparisonBaseAdapter extends BaseAdapter
{
    private final Context context;
    private final LayoutInflater inflater;
    private final ArrayList<ComparisonResults> comparisonArrayList;

    public ComparisonBaseAdapter(Context context, ArrayList<ComparisonResults> results)
    {
        this.context = context;
        this.comparisonArrayList = results;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.comparison_list, null);
            holder = new ViewHolder();
            holder.product = (TextView) convertView.findViewById(R.id.product);
            holder.price = (TextView) convertView.findViewById(R.id.priceColruyt);
            holder.difference = (TextView) convertView.findViewById(R.id.difference);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.product.setText(comparisonArrayList.get(position).getProduct());
        holder.price.setText("€ " + comparisonArrayList.get(position).getPrice());
        holder.difference.setText("-€ " + comparisonArrayList.get(position).getDifference());

        return convertView;
    }

    static class ViewHolder
    {
        public TextView product;
        public TextView price;
        public TextView difference;
    }
}
