package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.views.MapView;

public class MapViewInScroll extends MapView {

  /* answered Apr 15 '20 at 11:55 by Oleg Bozhko
   * https://stackoverflow.com/questions/61023565/google-map-viewpager2-swiping-conflict
   * */
  public MapViewInScroll(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler,
      AttributeSet attrs) {
    super(context, tileProvider, tileRequestCompleteHandler, attrs);
  }

  public MapViewInScroll(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler,
      AttributeSet attrs, boolean hardwareAccelerated) {
    super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
  }

  public MapViewInScroll(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MapViewInScroll(Context context) {
    super(context);
  }

  public MapViewInScroll(Context context, MapTileProviderBase aTileProvider) {
    super(context, aTileProvider);
  }

  public MapViewInScroll(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
    super(context, aTileProvider, tileRequestCompleteHandler);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    getParent().requestDisallowInterceptTouchEvent(true);
    return super.dispatchTouchEvent(ev);
  }
}
