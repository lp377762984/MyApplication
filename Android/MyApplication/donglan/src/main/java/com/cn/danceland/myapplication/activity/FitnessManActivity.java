package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.PictureUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2017/11/3.
 */

public class FitnessManActivity extends Activity {

    RecyclerView fitness_recycle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        initView();
    }

    private void initView() {
        fitness_recycle = findViewById(R.id.fitness_recycle);
        fitness_recycle.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> arr = new ArrayList<>();
        fitness_recycle.setAdapter(new MyRecycleAdapter(arr));
    }

    public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyRecycleViewHolder>{

        public ArrayList<String> arrayList;

        public MyRecycleAdapter(ArrayList<String> arrayList){
            this.arrayList = arrayList;
        }

        @Override
        public MyRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fitness_recyle_item,parent,false);
            MyRecycleViewHolder myRecycleViewHolder = new MyRecycleViewHolder(v);
            return myRecycleViewHolder;
        }

        @Override
        public void onBindViewHolder(MyRecycleViewHolder holder, int position) {
            holder.fitness_circleimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zhongdian_icon));
            holder.fitness_fans.setText("100w粉丝");
            holder.fitness_name.setText("小明同学");
            holder.im1.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zhongdian_icon));
            holder.im2.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zhongdian_icon));
            holder.im3.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zhongdian_icon));
        }


        @Override
        public int getItemCount() {
            return 20;
        }

        class MyRecycleViewHolder extends RecyclerView.ViewHolder{
            CircleImageView fitness_circleimage;
            TextView fitness_fans;
            TextView fitness_name;
            ImageView im1;
            ImageView im2;
            ImageView im3;

            public MyRecycleViewHolder(View itemView) {
                super(itemView);
                fitness_circleimage = itemView.findViewById(R.id.fitness_circleimage);
                fitness_fans = itemView.findViewById(R.id.fitness_fans);
                fitness_name = itemView.findViewById(R.id.fitness_name);
                im1 = itemView.findViewById(R.id.im1);
                im2 = itemView.findViewById(R.id.im2);
                im3 = itemView.findViewById(R.id.im3);
            }
        }
    }



}
