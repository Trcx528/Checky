package tech.jdp.checky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import tech.jdp.checky.db.checklist_item;

/**
 * Created by JPiquette on 5/11/2016.
 */
public class ChecklistAdapter extends BaseAdapter {
    private ArrayList<checklist_item> data;
    private LayoutInflater inflater;

    public ChecklistAdapter(Context ctx, ArrayList<checklist_item> data) {
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
        CheckBox cb;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.checklist_row, null);
            cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(cb);
        } else {
            cb = (CheckBox) convertView.getTag();
        }

        cb.setText(data.get(position).text);
        cb.setChecked(data.get(position).checked);
        return convertView;
    }
}
