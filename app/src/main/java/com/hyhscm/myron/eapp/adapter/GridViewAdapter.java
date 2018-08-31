package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.TopNum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/11/23.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    int[] images;
    String[] names;
    private List<TopNum> nums = new ArrayList<>();

    public GridViewAdapter(Context context, int[] re, String[] names,List num) {
        this.context = context;
        this.images = re;
        this.names = names;
        this.nums = num;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names[position];
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tb, null);
            vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_tb);
            vh.tv_mame = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            switch(position){
                case 0:
                    vh.tv_mame.setTextColor(Color.parseColor("#ef8cb7"));
                    break;
                case 1:
                    vh.tv_mame.setTextColor(Color.parseColor("#81a9da"));
                    break;
                case 2:
                    vh.tv_mame.setTextColor(Color.parseColor("#f18f4d"));
                    break;
                case 3:
                    vh.tv_mame.setTextColor(Color.parseColor("#61b5dd"));
                    break;
            }
            convertView.setTag(vh);
        } else {
            vh = (ViewHoler) convertView.getTag();
        }
        vh.updateViews(position);
        return convertView;
    }

    class ViewHoler {
        ImageView iv_icon;
        TextView tv_mame;
        TextView tv_count;

        public void updateViews(int position) {
            iv_icon.setImageResource(images[position]);
            tv_mame.setText(names[position]);
            if(nums.size()>0){
            for (TopNum num:nums){
            if(match(position)==num.getType()){
                tv_count.setText(num.getState()+"");
                if(num.getType()==2){
                    MainActivity.allneedCount = num.getState();
                }else if(num.getType()==3){
                    MainActivity.allproductCount = num.getState();
                }
            }
        }
            }
        }
    }


    public void refreshNum(List num){
     this.nums = num;
    }


    public int match(int i){
        int type = -1;
        if(i == 0){
            type = 1; // 头条
        }else if(i==1){
            type=2;   //商机
        }else if(i==2){
            type = 3; // 招商
        }else if(i==3){
            type = 5; // 代理；
        }
        return type;
    }
}
