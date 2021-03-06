package com.troop.freedcam.ui.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.troop.freedcam.R;
import com.troop.freedcam.i_camera.modules.I_WorkEvent;
import com.troop.freedcam.ui.MainActivity_v2;
import com.troop.freedcam.utils.StringUtils;

import java.io.File;
import java.io.IOException;

import troop.com.imageviewer.ImageViewerFragment;


/**
 * Created by troop on 25.08.2014.
 */
public class ThumbnailHandler implements View.OnClickListener, I_WorkEvent
{
    final MainActivity_v2 activity;
    ImageView thumbView;
    Bitmap bitmap;
    File lastFile;
    boolean working = false;
    ViewGroup.LayoutParams params;
    boolean showThumb = false;
    private static String TAG = StringUtils.TAG +ThumbnailHandler.class.getSimpleName();

    public ThumbnailHandler(final MainActivity_v2 activity)
    {
        this.activity = activity;
        thumbView = (ImageView)activity.findViewById(R.id.imageView_Thumbnail);
        thumbView.setOnClickListener(this);
        params = (ViewGroup.LayoutParams) thumbView.getLayoutParams();

    }

    @Override
    public void onClick(View v)
    {
        ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
        android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.MainLayout, imageViewerFragment, "ImageViewer");
        transaction.commit();
    }

    @Override
    public String WorkHasFinished(final File filePath)
    {
        activity.runOnUiThread
                (new Runnable() {
                    @Override
                    public void run() {
                        lastFile = filePath;
                        if (!working) {
                            working = true;
                    /*if (thumbView.getAlpha() == 1f)
                        hideThumb(filePath);
                    else*/
                            Log.d(TAG, "Load Thumb " + filePath.getName());
                            showThumb(filePath);
                            working = false;
                        }

                    }
                });
        return null;
    }

    private Bitmap loadThumbViewImage(File file)
    {
        if(file.getAbsolutePath().endsWith("jpg"))
        {
            byte[] thum = null;
            try {
                thum = new ExifInterface(file.getAbsolutePath()).getThumbnail();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (thum != null)
                return BitmapFactory.decodeByteArray(thum, 0, thum.length);

        }
        else if (file.getAbsolutePath().endsWith("mp4"))
        {
            return ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    private void hideThumb(final File filePath)
    {
        /*if (thumbView.getAlpha() == 1f)
        {
            thumbView.animate().alpha(0f).setDuration(200).setListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //thumbView.setVisibility(View.GONE);
                    //thumbView.setImageBitmap(loadThumbViewImage(filePath));


                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }*/
        showThumb(filePath);
    }

    private void showThumb(File filePath)
    {
        if(filePath != null && !filePath.getAbsolutePath().endsWith(".dng") && !filePath.getAbsolutePath().endsWith(".raw") && filePath.exists())
        {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
                System.gc();
            }
            bitmap = loadThumbViewImage(filePath);
            thumbView.setImageBitmap(bitmap);
            //LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(activity.getWindowManager().getDefaultDisplay().getWidth(),activity.getWindowManager().getDefaultDisplay().getHeight());
            //thumbView.setLayoutParams(layoutParams);
            showThumb = true;
            thumbView.setAlpha(1F);
            //thumbView.bringToFront();
            //thumbView.postDelayed(restoreThumbSize, 2000);
        }

    }

    Runnable restoreThumbSize =new Runnable() {
        @Override
        public void run() {
            thumbView.setLayoutParams(params);
            showThumb = false;
        }
    };
}
