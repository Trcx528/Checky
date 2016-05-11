package tech.jdp.checky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JPiquette on 5/11/2016.
 */
public class MainListAdapter extends BaseAdapter {
    private ArrayList<RowItem> data;
    private LayoutInflater inflater;

    public MainListAdapter(Context ctx, ArrayList<RowItem> data) {
        this.data = data;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewRow row = new ViewRow();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_row, null);
            row.title = (TextView) convertView.findViewById(R.id.title);
            row.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(row);
        } else {
            row = (ViewRow) convertView.getTag();
        }

        row.title.setText(data.get(position).title);
        row.date.setText(data.get(position).updated);
        return convertView;
    }

    static class ViewRow {
        TextView title;
        TextView date;
    }
}
