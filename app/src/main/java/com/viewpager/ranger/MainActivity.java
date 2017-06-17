package com.viewpager.ranger;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.viewpager.ranger.pageTransformer.AlphaPageTransformer;

/**
 * viewpager一屏显示多个页面
 * Android:clipChildren="false"--子view在绘制时不要裁切它的显示范围
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager vp;

    private int[] imgResources = {R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R.mipmap.a5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp = (ViewPager) findViewById(R.id.vp);

        //设置page间距
        vp.setPageMargin(20);
        vp.setOffscreenPageLimit(3);
        vp.setPageTransformer(true, new AlphaPageTransformer());

        vp.setAdapter(new MyAdapter());
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(MainActivity.this);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageResource(imgResources[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
