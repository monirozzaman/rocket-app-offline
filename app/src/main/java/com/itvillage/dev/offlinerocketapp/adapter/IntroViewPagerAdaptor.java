package com.itvillage.dev.offlinerocketapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvillage.dev.offlinerocketapp.R;
import com.itvillage.dev.offlinerocketapp.model.ScreenItems;

import java.util.List;

/**
 * Created by monirozzamanroni on 7/18/2019.
 */

public class IntroViewPagerAdaptor extends PagerAdapter {
    Context context;
    List<ScreenItems> screenItems;
    public IntroViewPagerAdaptor(Context context, List<ScreenItems> screenItems) {
        this.context = context;
        this.screenItems = screenItems;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/siyam_rupali.ttf");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_screen, null);

        ImageView imgSlide = view.findViewById(R.id.intro_icon);
        TextView title = view.findViewById(R.id.intro_title);
        TextView description = view.findViewById(R.id.intro_decription);

        title.setTypeface(typeface);
        description.setTypeface(typeface);
        title.setText(screenItems.get(position).getTitle());
        description.setText(screenItems.get(position).getDescription());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imgSlide.setImageDrawable(ContextCompat.getDrawable(context, screenItems.get(position).getImage()));
        }
        container.addView(view);

        return view;

    }

    @Override
    public int getCount() {
        return screenItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
}
