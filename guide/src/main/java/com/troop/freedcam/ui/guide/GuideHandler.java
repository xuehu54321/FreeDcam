package com.troop.freedcam.ui.guide;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.troop.freedcam.i_camera.AbstractCameraUiWrapper;
import com.troop.freedcam.i_camera.parameters.AbstractModeParameter;
import com.troop.freedcam.i_camera.parameters.I_ParametersLoaded;
import com.troop.freedcam.ui.I_Activity;
import com.troop.freedcam.ui.I_PreviewSizeEvent;

/**
 * Created by George on 1/19/2015.
 */
public class GuideHandler extends Fragment implements AbstractModeParameter.I_ModeParameterEvent, I_PreviewSizeEvent {
    View view;
    ImageView img;
    Context contextt;
    AbstractCameraUiWrapper cameraUiWrapper;
    I_Activity i_activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.guides, container,false);
        img = (ImageView) view.findViewById(R.id.imageViewGyide);
        return view;
    }

    public void setCameraUiWrapper(AbstractCameraUiWrapper cameraUiWrapper, I_Activity i_activity)
    {
        this.cameraUiWrapper = cameraUiWrapper;
        this.i_activity = i_activity;
        i_activity.SetPreviewSizeChangedListner(this);
        cameraUiWrapper.camParametersHandler.GuideList.addEventListner(this);
    }

    public int[] GetScreenSize() {
        int width = 0;
        int height = 0;

        if (Build.VERSION.SDK_INT >= 17)
        {
            WindowManager wm = (WindowManager)view.getContext().getSystemService(Context.WINDOW_SERVICE);
            Point size =  new Point();
            wm.getDefaultDisplay().getRealSize(size);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                width = size.x;
                height = size.y;
            }
            else
            {
                height = size.x;
                width = size.y;
            }
        }
        else
        {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                width = metrics.widthPixels;
                height = metrics.heightPixels;
            }
            else
            {
                width = metrics.heightPixels;
                height = metrics.widthPixels;
            }

        }
        return new int[]{width,height};
    }


    public void SetViewG(final String str)
    {
        double quckRationMath = GetScreenSize()[0]/GetScreenSize()[1];
        if(quckRationMath < 1.44) {

            img.post(new Runnable() {
                @Override
                public void run() {


                    if (str.equals("Golden Spiral")) {

                        img.setImageResource(R.drawable.ic_guide_golden_spiral_4_3);
                    } else if (str.equals("Rule Of Thirds")) {
                        //ImageView img = (ImageView) findViewById(R.id.imageViewGyide);
                        img.setImageResource(R.drawable.ic_guide_rule_3rd_4_3);
                    } else if (str.equals("Square 1:1")) {
                        //ImageView img = (ImageView) findViewById(R.id.imageViewGyide);
                        img.setImageResource(R.drawable.ic_guide_insta_1_1);
                    } else if (str.equals("Square 4:3"))
                        img.setImageResource(R.drawable.ic_guide_insta_4_3);
                    else if (str.equals("Square 16:9"))
                        img.setImageResource(R.drawable.ic_guide_insta_16_9);
                    else if (str.equals("Diagonal Type 1"))
                        img.setImageResource(R.drawable.ic_guide_diagonal_type_1_4_3);
                    else if (str.equals("Diagonal Type 2"))
                        img.setImageResource(R.drawable.ic_guide_diagonal_type_2_4_3);
                    else if (str.equals("Diagonal Type 3"))
                        img.setImageResource(R.drawable.ic_guide_diagonal_type_3);
                    else if (str.equals("Diagonal Type 4"))
                        img.setImageResource(R.drawable.ic_guide_diagonal_type_4);
                    else if (str.equals("Diagonal Type 5"))
                        img.setImageResource(R.drawable.ic_guide_diagonal_type_5);
                    else if (str.equals("Golden Ratio"))
                        img.setImageResource(R.drawable.ic_guide_golden_ratio_type_1_4_3);
                    else if (str.equals("Golden Hybrid"))
                        img.setImageResource(R.drawable.ic_guide_golden_spriral_ratio_4_3);
                    else if (str.equals("Golden R/S 1"))
                        img.setImageResource(R.drawable.ic_guide_golden_fuse1_4_3);
                    else if (str.equals("Golden R/S 2"))
                        img.setImageResource(R.drawable.ic_guide_golden_fusion2_4_3);
                    else if (str.equals("Golden Triangle"))
                        img.setImageResource(R.drawable.ic_guide_golden_triangle_4_3);
                    else if (str.equals("Group POV Five"))
                        img.setImageResource(R.drawable.ic_guide_groufie_five);
                    else if (str.equals("Group POV Three"))
                        img.setImageResource(R.drawable.ic_guide_groufie_three);
                    else if (str.equals("Group POV Potrait"))
                        img.setImageResource(R.drawable.ic_guide_groupshot_potrait);
                    else if (str.equals("Group POV Full"))
                        img.setImageResource(R.drawable.ic_guide_groupshot_fullbody);
                    else if (str.equals("Group POV Elvated"))
                        img.setImageResource(R.drawable.ic_guide_groupshot_elevated_pov);
                    else if (str.equals("Group by Depth"))
                        img.setImageResource(R.drawable.ic_guide_groupshot_outfocusing);
                    else if (str.equals("Group Center Lead"))
                        img.setImageResource(R.drawable.ic_guide_groupshot_center_leader);
                    else if (str.equals("Center Type x"))
                        img.setImageResource(R.drawable.ic_guide_center_type_1_4_3);
                    else if (str.equals("Center Type +"))
                        img.setImageResource(R.drawable.ic_guide_center_type_2_4_3);
                    else if (str.equals("None"))
                        img.setImageBitmap(null);
                }
            });
        }
        else
        {
            if (str.equals("Golden Spiral")) {

                img.setImageResource(R.drawable.ic_guide_golden_spiral_16_9);
            }
            else if (str.equals("Golden Triangle"))
                img.setImageResource(R.drawable.ic_golden_triangle_16_9);
            else if (str.equals("Rule Of Thirds"))
                img.setImageResource(R.drawable.ic_guide_rule_3rd_16_9);
            else if (str.equals("Center Type x"))
                img.setImageResource(R.drawable.ic_guide_center_type_1_4_3);
            else if (str.equals("Center Type +"))
                img.setImageResource(R.drawable.ic_guide_center_type_2_4_3);
            else if (str.equals("Square 1:1")) {
                img.setImageResource(R.drawable.ic_guide_insta_1_1);
            } else if (str.equals("Square 4:3"))
                img.setImageResource(R.drawable.ic_guide_insta_4_3);
            else if (str.equals("Square 16:9"))
                img.setImageResource(R.drawable.ic_guide_insta_16_9);
            else if (str.equals("None"))
                img.setImageBitmap(null);

        }

    }

    @Override
    public void onValueChanged(String val) {
        SetViewG(val);
    }

    @Override
    public void onIsSupportedChanged(boolean isSupported) {

    }

    @Override
    public void onIsSetSupportedChanged(boolean isSupported) {

    }

    @Override
    public void onValuesChanged(String[] values) {

    }

    @Override
    public void OnPreviewSizeChanged(int w, int h)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i_activity.GetPreviewWidth(), i_activity.GetPreviewHeight());
        layoutParams.gravity = Gravity.CENTER;
        img.setLayoutParams(layoutParams);
    }
}