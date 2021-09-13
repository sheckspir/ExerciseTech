package at.lukle.clickableareasimage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Lukas on 10/22/2015.
 */
@SuppressWarnings("rawtypes")
public class ClickableAreasImage implements PhotoViewAttacher.OnPhotoTapListener{

    private PhotoViewAttacher attacher;
    private OnClickableAreaClickedListener listener;

    private List<? extends AbstractArea> clickableAreas;

    private int imageWidthInPx;
    private int imageHeightInPx;

    public ClickableAreasImage(PhotoViewAttacher attacher, OnClickableAreaClickedListener listener){
        this.attacher = attacher;
        init(listener);
    }

    private void init(OnClickableAreaClickedListener listener) {
        this.listener = listener;
        getImageDimensions(attacher.getImageView());
        attacher.setOnPhotoTapListener(this);
    }


    private void getImageDimensions(ImageView imageView){
        BitmapDrawable drawable2 = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable2.getBitmap();
        //After SDK 28 (Android Pie), getBitmap() returns the actual size of the image on the screen
        if (Build.VERSION.SDK_INT > 27) {
            imageWidthInPx = (int) (bitmap.getWidth());
            imageHeightInPx = (int) (bitmap.getHeight());
        } else {
            imageWidthInPx = (int) (bitmap.getWidth() / Resources.getSystem().getDisplayMetrics().density);
            imageHeightInPx = (int) (bitmap.getHeight() / Resources.getSystem().getDisplayMetrics().density);
        }
    }
        

    @Override
    public void onPhotoTap(View view, float x, float y) {
        PixelPosition pixel = ImageUtils.getPixelPosition(x, y, imageWidthInPx, imageHeightInPx);
        List<AbstractArea> areasUnderTap = getClickAbleAreas(pixel.getX(), pixel.getY());
        Log.d("TAG", "x = " + pixel.getX() + " y = " + pixel.getY() + " finded " + areasUnderTap.size());
        for(AbstractArea ca : areasUnderTap){
            listener.onClickableAreaTouched(ca.getItem());
        }
    }

    private List<AbstractArea> getClickAbleAreas(int x, int y){
        List<AbstractArea> resultt= new ArrayList<>();
        for(AbstractArea ca : getClickableAreas()){
            if (ca.isInside(x, y)) {
                resultt.add(ca);
            }
        }
        return resultt;
    }

    public void setClickableAreas(List<? extends AbstractArea> clickableAreas) {
        this.clickableAreas = clickableAreas;
    }

    public List<? extends AbstractArea> getClickableAreas() {
        return clickableAreas;
    }
}
