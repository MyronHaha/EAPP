package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Advert;
import com.hyhscm.myron.eapp.data.Advnotify;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.taobao.library.BaseBannerAdapter;
import com.taobao.library.VerticalBannerView;


import java.util.List;

/**
 * Created byson on 2018/3/26.
 */
public class NotifyAdapter extends BaseBannerAdapter<Advert> {

    private List<String> mDatas;
    private Context mcontext;
    private NotifyListener listener;
    public NotifyAdapter(List<Advert> datas, Context context, NotifyListener listener) {
        super(datas);
        this.listener = listener;
        this.mcontext = context;
    }

    @Override
    public View getView(VerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, null);
    }

    @Override
    public void setItem(final View view, final Advert data) {
        if(data!=null){
            TextView tv = (TextView) view.findViewById(R.id.tv_content);
            TextView type = (TextView) view.findViewById(R.id.tv_type);
            tv.setText(data.getContent());
            type.setText(((BaseActivity)mcontext).matchSourceType(data.getSourceType()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNotifyClick(view,data);
                }
            });
        }

    }

    public void notifyChanged(){

    }

   public interface  NotifyListener{

       void onNotifyClick(View view,Advert data);
   }
}
