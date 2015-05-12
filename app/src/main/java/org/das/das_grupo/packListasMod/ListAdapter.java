package org.das.das_grupo.packListasMod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.das.das_grupo.R;

import java.util.ArrayList;

/**
 * Created by Alberto on 12/05/2015.
 */
public class ListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<historia> objects;

    public ListAdapter(ArrayList<historia> pHistoria, Context context){
        ctx = context;
        objects = pHistoria;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objects.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        historia hist = objects.get(position);

        ((TextView)view.findViewById(R.id.titItem)).setText(hist.titulo);
        ((TextView)view.findViewById(R.id.autorItem)).setText(hist.autor);
        ((TextView)view.findViewById(R.id.fechaItem)).setText(hist.fecha.toString());

        return view;
    }
}
