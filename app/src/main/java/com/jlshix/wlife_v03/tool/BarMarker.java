package com.jlshix.wlife_v03.tool;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jlshix.wlife_v03.R;

/**
 * Created by Leo on 2016/8/13.
 */
public class BarMarker extends MarkerView {
    private TextView tv;
    private String unit;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public BarMarker(Context context, int layoutResource, String unit) {
        super(context, layoutResource);
        tv = (TextView)findViewById(R.id.tvContent);
        this.unit = unit;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv.setText(e.getY() + unit);
    }

    @Override
    public int getXOffset(float xpos) {
        return -getWidth()/2;
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
