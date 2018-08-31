package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/11/23.
 */
// 标签的adpter
public class GridViewAdapter_Tag extends BaseAdapter {

    private Context context;
    List data = new ArrayList();

    public GridViewAdapter_Tag(Context context, List list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        L.e("tagsize",data.size()+"");
        return 3;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler vh;
        if (convertView == null) {
            vh = new ViewHoler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ptag, null);
             vh.tag = (TextView) convertView.findViewWithTag(R.id.tv_tag);
            if (data.size()>0) {
                vh.tag.setText(data.get(position)+"tag");
            }
            convertView.setTag(vh);
        } else {
            vh = (ViewHoler) convertView.getTag();
        }
        return convertView;
    }

    class ViewHoler {
        TextView tag;
    }


}
