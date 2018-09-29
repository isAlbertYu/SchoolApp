package com.example.albert.quertfunction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.albert.schoolapp.R;

import java.util.List;

/**
 * Created by Albert on 2018/8/9.
 */

public class QueryAdapter extends ArrayAdapter {
    private int resourceId;//每个gird要装载的布局资源

    public QueryAdapter(@NonNull Context context,
                        int resourceId, @NonNull List objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Query query = (Query) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,
                parent, false);
        TextView queryText = (TextView) view.findViewById(R.id.textview1);
        queryText.setText(query.getName());



        return view;
    }
}
