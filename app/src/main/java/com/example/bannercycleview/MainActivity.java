package com.example.bannercycleview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<View> views = new ArrayList<>();
        String[] urls = {"http://www.gaikuo.com.cn/uploads/allimg/160428/1250122130-0.jpg","http://jiangsu.china.com.cn/uploadfile/2016/0619/1466347395693776.jpg","http://img1.gtimg.com/fashion/pics/hv1/240/177/2043/132891450.jpg"};
        for (int i = 0; i <urls.length; i++) {
            if (!TextUtils.isEmpty(urls[i])){
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load(urls[i]).into(imageView);
                views.add(imageView);
            }
        }

        BannerCycleView bannerCycleView = (BannerCycleView) findViewById(R.id.banner_cycle_view);
        bannerCycleView.setAutoCycle(true,true);
        bannerCycleView.setmCycleDelayed(4000);
        bannerCycleView.addAllView(views);
        bannerCycleView.setOnPageClickListener(new BannerCycleView.OnPageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this,"当前位置："+position,Toast.LENGTH_LONG).show();
            }
        });
    }
}
