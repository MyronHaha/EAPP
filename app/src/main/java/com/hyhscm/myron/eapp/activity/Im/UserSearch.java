package com.hyhscm.myron.eapp.activity.Im;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.data.Contract;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyphenate.easeui.widget.EaseTitleBar;

import butterknife.BindView;

/**
 * Created by Myron on 2018/1/16.
 */
public class UserSearch extends ListBaseBeanActivity<Contract> {
@BindView(R.id.query)
    EditText search;
@BindView(R.id.rv)
    LRecyclerView rv;
@BindView(R.id.title_bar)
EaseTitleBar titleBar;
@BindView(R.id.search_clear)
    ImageButton clearText;
    public UserSearch() {
        super(R.layout.layout_usersearch);
    }

    @Override
    protected void initView() {
        super.initView();
        initListView(R.layout.item_contact);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSearch.this.finish();
            }
        });
        titleBar.setTitle("添加朋友");
       search.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               if(!"".equals(editable.toString())){
                   rv.setVisibility(View.VISIBLE);
                   clearText.setVisibility(View.VISIBLE);
                   params.put("k",editable.toString());
                   loadData();
               }else{
                   rv.setVisibility(View.INVISIBLE);
                   clearText.setVisibility(View.INVISIBLE);
               }

           }
       });
       clearText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               search.getText().clear();
               clearText.setVisibility(View.INVISIBLE);
           }
       });
    }

    @Override
    protected void initData() {
        super.initData(Url.CONTACT_SEARCH,50);
    }

    @Override
    protected void bindView(BaseViewHolder holder, Contract item) {
//        super.bindView(holder, item);
          holder.setText(R.id.name,item.getNname());
          holder.setRoundImageView(R.id.iv,Url.IMAGEPATH+item.getPic());

    }

    @Override
    protected void itemClick(View v, int i, Contract item) {
        super.itemClick(v, i, item);
        Intent intent = new Intent(this,UserAddDetail.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }


}