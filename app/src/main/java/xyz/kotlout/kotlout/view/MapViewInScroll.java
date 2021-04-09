package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.views.MapView;

/**
 * A map view for situations the map is in a scrollable menu
 *
 * answered Apr 15 '20 at 11:55 by Oleg Bozhko
 * https://stackoverflow.com/questions/61023565/google-map-viewpager2-swiping-conflict
 * How to have the map take scrolling priority over the interface
 */
public class MapViewInScroll extends MapView {

  /**
   * Instantiates a new Map view in scroll.
   *
   * @param context                    the context
   * @param tileProvider               the tile provider
   * @param tileRequestCompleteHandler the tile request complete handler
   * @param attrs                      the attrs
   */
  public MapViewInScroll(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler,
      AttributeSet attrs) {
    super(context, tileProvider, tileRequestCompleteHandler, attrs);
  }

  /**
   * @param context                    the context
   * @param tileProvider               a tile provider
   * @param tileRequestCompleteHandler a tile request complete handler
   * @param attrs                      an attribute set
   * @param hardwareAccelerated        is hardware accelerated
   */
  public MapViewInScroll(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler,
      AttributeSet attrs, boolean hardwareAccelerated) {
    super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
  }

  /**
   * @param context the context
   * @param attrs   an attribute set
   */
  public MapViewInScroll(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * @param context the context
   */
  public MapViewInScroll(Context context) {
    super(context);
  }

  /**
   * @param context       the context
   * @param aTileProvider a tile provider
   */
  public MapViewInScroll(Context context, MapTileProviderBase aTileProvider) {
    super(context, aTileProvider);
  }

  /**
   * @param context                    the context
   * @param aTileProvider              a tile provider
   * @param tileRequestCompleteHandler tile request complete handler
   */
  public MapViewInScroll(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
    super(context, aTileProvider, tileRequestCompleteHandler);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    getParent().requestDisallowInterceptTouchEvent(true);
    return super.dispatchTouchEvent(ev);
  }
}
