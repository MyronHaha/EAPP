package com.hyhscm.myron.eapp.activity.Home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.adapter.ProductCategoryAdapter;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.ProductCategoryBean;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Jason on 2017/12/13.
 */

public class ProductCategory extends BaseActivity {
    @BindView(R.id.iv_category)
    RecyclerView rv_category;
    LinearLayoutManager manager;
    ProductCategoryAdapter adapter;
    List<Integer> pTypeList = new ArrayList<>();
    List<List<ProductCategoryBean>>  list = new ArrayList<>();
    public ProductCategory() {
        super(R.layout.layout_product_category);
    }

    protected void initView() {
        super.initView();
        changeTitle("产品招商");

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    protected void initData() {
        super.initData();

        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_category.setLayoutManager(manager);

//        adapter.setOnItemClickListener(new ProductCategoryAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                MyToast.makeText(ProductCategoryBean.this,""+position,1000).show();
//            }
//        });
    }


    public List<List<ProductCategoryBean>> getData(){


        HttpCore.getProductCategory(new IListResultHandler<ProductCategoryBean>() {
            @Override
            public void onSuccess(ListResult<ProductCategoryBean> rs) {
                if(rs.getSuccess()){
                    L.e("total:"+rs.getBiz().size());
                       list =   handleData(rs.getBiz());

                    L.e("handledataresult---"+list.size());
                    adapter = new ProductCategoryAdapter(ProductCategory.this,list);
                    rv_category.setAdapter( adapter);
                }
            }
        });

//
//        List<List<Product>> list = new ArrayList<>();
//        for(int i = 0;i<20;i++){
//            List p = new ArrayList();
//                int size = 6;
//                if(i==3){
//                    size = 4;
//                }else if(i==1){
//                    size = 5;
//                }else if(i == 4){
//                    size = 6;
//                } else if (i==2) {
//                size = 2;
//                }
//                for(int j = 0;j<size;j++){
//                    Product product = new Product();
//                    product.setName("product"+j);
//                    product.setContent("product--content"+j);
//                    p.add(product);
//
//            }
//            list.add(p);
//        }
        return list;
    }

    public List<List<ProductCategoryBean>> handleData(List<ProductCategoryBean> data){
        pTypeList.clear();
        for(ProductCategoryBean pc:data){
            if(!pTypeList.contains(pc.getType())){
                pTypeList.add(pc.getType());
            }
        }
        L.e("typeCount:"+pTypeList.size());
        for(Integer type:pTypeList){
            List<ProductCategoryBean> templist = new ArrayList<>();
            templist.clear();
          for(ProductCategoryBean pc:data){
            if(type==pc.getType()){
                templist.add(pc);
              }
          }
          L.e("templsitsize:"+templist.size());
            if(templist.size()>1){
                list.add(templist);
            }
        }

        return list;
    }


}
