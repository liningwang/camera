package com.wangln.list;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.wangln.list.HorizontalScrollViewAdapter.ViewHolder;
import com.wangln.util.AppUtil;
import com.wangln.util.SDUtil;
import com.wangln.util.SDUtil.ViewSize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MyHorizontalScrollView extends HorizontalScrollView implements
		OnClickListener,OnCheckedChangeListener
{

	/**
	 * 图片滚动时的回调接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface CurrentImageChangeListener
	{
		void onCurrentImgChanged(int position, View viewIndicator);
	}

	/**
	 * 条目点击时的回调
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnItemClickListener
	{
		void onClick(View view, int pos);
	}

	public interface OnItemCheckListener
	{
		void onClick(View view, int pos);
	}
	private CurrentImageChangeListener mListener;

	private OnItemClickListener mOnClickListener;
	private OnItemCheckListener mOnCheckListener;

	private static final String TAG = "MyHorizontalScrollView";

	private CheckBox check;
	/**
	 * HorizontalListView中的LinearLayout
	 */
	private LinearLayout mContainer;

	/**
	 * 子元素的宽度
	 */
	private int mChildWidth;
	/**
	 * 子元素的高度
	 */
	private int mChildHeight;
	/**
	 * 当前最后一张图片的index
	 */
	private int mCurrentIndex;
	/**
	 * 当前第一张图片的下标
	 */
	private int mFristIndex;
	/**
	 * 当前第一个View
	 */
	private View mFirstView;
	/**
	 * 数据适配器
	 */
	private HorizontalScrollViewAdapter mAdapter;
	/**
	 * 每屏幕最多显示的个数
	 */
	private int mCountOneScreen;
	/**
	 * 屏幕的宽度
	 */
	private int mScreenWitdh;
	private LruCache<String, Bitmap> mMemoryCache;
	private int mNewSroll;
	private int mOldSroll;
	private Set<BitmapWorkerTask> taskCollection;

	/**
	 * 保存View与位置的键值对
	 */
	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();
	private Map<View, Integer> mViewCheck = new HashMap<View, Integer>();
	private Map<String, View> mViewUrl = new HashMap<String, View>();

	public MyHorizontalScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// 获得屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWitdh = outMetrics.widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}

	/**
	 * 加载下一张图片
	 */
	protected void loadNextImg()
	{
		// 数组边界值计算
		if (mCurrentIndex == mAdapter.getCount() - 1)
		{
			Log.d("wang","loadNextImg finished!");
			return;
		}
		Log.d("wang","loadNextImg mCurrentIndex  is " + mCurrentIndex + "mAdapter.getCount is " + mAdapter.getCount());
		loadBitmap(++mCurrentIndex,false);
		/*	//移除第一张图片，且将水平滚动位置置0
		scrollTo(0, 0);
		//mViewPos.remove(mContainer.getChildAt(0));
		//mContainer.removeViewAt(0);
		
		//获取下一张图片，并且设置onclick事件，且加入容器中
		View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
		view.setOnClickListener(this);
		mContainer.addView(view);
		mViewPos.put(view, mCurrentIndex);*/
		
		//当前第一张图片小标
		mFristIndex++;
		//如果设置了滚动监听则触发
		if (mListener != null)
		{
			notifyCurrentImgChanged();
		}

	}
	/**
	 * 加载前一张图片
	 */
	protected void loadPreImg()
	{
		//如果当前已经是第一张，则返回
		if (mFristIndex == 0)
			return;
		//获得当前应该显示为第一张图片的下标
		int index = mCurrentIndex - mCountOneScreen;
		if (index >= 0)
		{
		Log.d("wang","loadPreImg Index  is " + index + " mCurrentIndex " + mCurrentIndex + "mCountOneScreen " + mCountOneScreen);
			loadBitmap(index,true);
//			mContainer = (LinearLayout) getChildAt(0);
			//移除最后一张
			//int oldViewPos = mContainer.getChildCount() - 1;
			//mViewPos.remove(mContainer.getChildAt(oldViewPos));
			//mContainer.removeViewAt(oldViewPos);
			
			//将此View放入第一个位置
			/*View view = mAdapter.getView(index, null, mContainer);
			mViewPos.put(view, index);
			mContainer.addView(view, 0);
			view.setOnClickListener(this);
			//水平滚动位置向左移动view的宽度个像素
			scrollTo(mChildWidth, 0);*/
			//当前位置--，当前第一个显示的下标--
			mCurrentIndex--;
			mFristIndex--;
			//回调
			if (mListener != null)
			{
				notifyCurrentImgChanged();

			}
		}
	}

	/**
	 * 滑动时的回调
	 */
	public void notifyCurrentImgChanged()
	{
		//先清除所有的背景色，点击时会设置为蓝色
		for (int i = 0; i < mContainer.getChildCount(); i++)
		{
			mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
		}
		
		mListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));

	}

	/**
	 * 初始化数据，设置数据适配器
	 * 
	 * @param mAdapter
	 */
	public void initDatas(HorizontalScrollViewAdapter mAdapter)
	{
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		// 获得适配器中第一个View
		final View view = mAdapter.getView(0, null, mContainer);
		mContainer.addView(view);

		// 强制计算当前View的宽和高
		if (mChildWidth == 0 && mChildHeight == 0)
		{
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			view.measure(w, h);
			mChildHeight = view.getMeasuredHeight();
			mChildWidth = view.getMeasuredWidth();
			//Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
			mChildHeight = view.getMeasuredHeight();
			// 计算每次加载多少个View
			mCountOneScreen = (mScreenWitdh / mChildWidth == 0)?mScreenWitdh / mChildWidth + 1:mScreenWitdh / mChildWidth + 3;

			//Log.e(TAG, "mCountOneScreen = " + mCountOneScreen
			//		+ " ,mChildWidth = " + mChildWidth);
			

		}
		taskCollection = new HashSet<BitmapWorkerTask>();
		initLruCache();
		//初始化第一屏幕的元素
		initFirstScreenChildren(mCountOneScreen);
	}

	/**
	 * 加载第一屏的View
	 * 
	 * @param mCountOneScreen
	 */
	public void initFirstScreenChildren(int mCountOneScreen)
	{
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();
		mViewCheck.clear();

		/*for (int i = 0; i < mCountOneScreen; i++)
		{
			View view = mAdapter.getView(i, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, i);
			mCurrentIndex = i;
		}*/
		loadBitmaps(0,mCountOneScreen);

		if (mListener != null)
		{
			notifyCurrentImgChanged();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_MOVE:
			//Log.d("wang", "getScrollX is " + getScrollX() + " mChildWidth " + mChildWidth);

			int scrollX = getScrollX();
			mNewSroll = scrollX / mChildWidth;
			//Log.d("wang", "mNewSroll is " + mNewSroll + " mOldSroll " + mOldSroll);
			// 如果当前scrollX为view的宽度，加载下一张，移除第一张
			if ((mNewSroll > mOldSroll) )
			{
				mOldSroll = mNewSroll;
				loadNextImg();
			}
			// 如果当前scrollX = 0， 往前设置一张，移除最后一张
			if (mOldSroll > mNewSroll)
			{
				mOldSroll = mNewSroll;
				loadPreImg();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
@SuppressLint("NewApi")
private void initLruCache() {
	int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
        int cacheSize = maxMemory / 8;  
        // 设置图片缓存大小为程序最大可用内存的1/8  
	Log.d("wang","cacheSize is " + cacheSize);
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {  
            @Override  
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getByteCount() / 1024;  
            }  
        };  
}
private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {  
        try {  
            for(int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
		String imageUrl = (String) mAdapter.getItem(i);
                Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);  
		View view = mAdapter.getView(i, null, mContainer);
                view.setOnClickListener(this);
        	ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.mCheck.setOnClickListener(this);
		 mAdapter.setView(view,bitmap);
               	mContainer.addView(view);
                mViewPos.put(view, i);
                mViewCheck.put(viewHolder.mCheck, i);
		mViewUrl.put(imageUrl,view);
                mCurrentIndex = i;

                if (bitmap == null) {  
                    BitmapWorkerTask task = new BitmapWorkerTask();  
                    taskCollection.add(task);  
                    task.execute(imageUrl);  
                } else {  
		  //  mAdapter.setView(view,bitmap);
                   //mContainer.addView(view);
                    /*ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);  
                    if (imageView != null && bitmap != null) {  
                        imageView.setImageBitmap(bitmap);  
                    } */ 
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
private void loadBitmap(int position,boolean first) {
        try {
	Log.d("wang","position is " + position);
        String imageUrl = (String) mAdapter.getItem(position);
        Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
	Log.d("wang","loadBitmap imageUrl " + imageUrl + "bitmap " + bitmap);
        View view = mAdapter.getView(position, null, mContainer);
        view.setOnClickListener(this);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
	viewHolder.mCheck.setOnClickListener(this);
         mAdapter.setView(view,bitmap);
	View v = mViewUrl.get(imageUrl);
	//Log.d("wang","loadBitmap view is " + v);
	if(first && (v == null)) {
        	mContainer.addView(view,0);
	} else if(v == null){
        	mContainer.addView(view);
	}
	/*if(first) {
		scrollTo(mChildWidth, 0);
	} else {
		scrollTo(0, 0);
	}*/
	//Log.d("wang", " after getScrollX is " + getScrollX() + " mChildWidth " + mChildWidth);
        mViewPos.put(view, position);
        mViewCheck.put(viewHolder.mCheck, position);
        mViewUrl.put(imageUrl,view);

        if (bitmap == null) {
            BitmapWorkerTask task = new BitmapWorkerTask();
            taskCollection.add(task);  
            task.execute(imageUrl);
        } else {
          //  mAdapter.setView(view,bitmap);
           //mContainer.addView(view);
            /*ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);  
            if (imageView != null && bitmap != null) {  
                imageView.setImageBitmap(bitmap);  
            } */
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@SuppressLint("NewApi")
public void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
	String cacheKey = AppUtil.md5(key);
	Log.d("wang","addBitmapToMemoryCache key is " + cacheKey + " bitmap is " + bitmap);
        if (mMemoryCache.get(cacheKey) == null) {  
	Log.d("wang","getBitmapFromMemoryCache is not null");
            mMemoryCache.put(cacheKey, bitmap);  
        }  
    }  
@SuppressLint("NewApi")
public Bitmap getBitmapFromMemoryCache(String key) {  
	String cacheKey = AppUtil.md5(key);
	Bitmap b = mMemoryCache.get(cacheKey);
	Log.d("wang","getBitmapFromMemoryCache key is " + cacheKey + " bitmap is " + b);
        return b;  
    } 
 class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {  
  
        /** 
         * 图片的URL地址 
         */  
        private String imageUrl;  
  
        @SuppressLint("NewApi")
		@Override  
        protected Bitmap doInBackground(String... params) {  
            imageUrl = params[0];  
            // 在后台开始下载图片  
	   Log.d("wang","doInBackground imagUrl " + imageUrl);
            Bitmap bitmap = downloadBitmap(imageUrl);  
            if (bitmap != null) {  
                // 图片下载完成后缓存到LrcCache中  
                Log.d("wang","before mMemoryCache  is " + mMemoryCache.size());
                addBitmapToMemoryCache(params[0], bitmap);  
                Log.d("wang","mMemoryCache  is " + mMemoryCache.size());
		//Bitmap b = getBitmapFromMemoryCache(params[0]);
                //Log.d("wang"," doInBackground bitmap is " + b);
            }  
            return bitmap;  
        }  
	        @Override  
        protected void onPostExecute(Bitmap bitmap) {  
            super.onPostExecute(bitmap);  
	    View v = mViewUrl.get(imageUrl);
            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。  
            /*ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);  
            if (imageView != null && bitmap != null) {  
                imageView.setImageBitmap(bitmap);  
            } */ 
	   //Log.d("wang","onPostExecute setView");
	    mAdapter.setView(v,bitmap);
            //mContainer.addView(v);
            taskCollection.remove(this);  
        } 
	private Bitmap downloadBitmap(String path) {
		return getImageforSize(path,mChildWidth,mChildHeight);
	}
}
	@Override
	public void onClick(View v)
	{
		if(v instanceof CheckBox) {
			Log.d("wang","is CheckBox");
			if(check != null) {
				check.setChecked(false);
			}
			if (mOnCheckListener != null)
			{
				mOnCheckListener.onClick(v, mViewCheck.get(v));
			}
			check = (CheckBox) v;
		} else {
			Log.d("wang","is not CheckBox");
			if (mOnClickListener != null)
			{
				for (int i = 0; i < mContainer.getChildCount(); i++)
				{
					mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
				}
				mOnClickListener.onClick(v, mViewPos.get(v));
			}
		}
	}

	public void setOnItemClickListener(OnItemClickListener mOnClickListener)
	{
		this.mOnClickListener = mOnClickListener;
	}

	public void setCurrentImageChangeListener(
			CurrentImageChangeListener mListener)
	{
		this.mListener = mListener;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		Log.d("wang","onCheckedChanged");
		if(check != null) {
			check.setChecked(false);
		}
		if(mOnCheckListener != null)
		{
			mOnCheckListener.onClick(arg0, mViewPos.get(arg0));
		}
		check = (CheckBox) arg0;
		
	}
	public void setOnItemCheckedListener(OnItemCheckListener mOnCheckListener)
	{
		this.mOnCheckListener = mOnCheckListener;
	}
         public Bitmap getImageforSize(String path,int dw,int dh) {

                // check image file exists
                /*File file = new File(path);
                if (!file.exists()) {
                        return null;
                }*/
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                Bitmap pic = BitmapFactory.decodeFile(path, op);
                //Bitmap pic = BitmapFactory.decodeStream(this
                //        .getContentResolver().openInputStream(imageFilePath),
                //       null, op);
                int wRatio = (int) Math.floor(op.outWidth / (float) dw);
                int hRatio = (int) Math.floor(op.outHeight / (float) dh);
                Log.d("wang", "dw = " + dw + " wRatio = " + wRatio + " outWidth = " + op.outWidth);
                Log.d("wang", "dh = " + dh + "hRatio = " + hRatio + " outHeight = " + op.outHeight);
                if (wRatio > 1 || hRatio > 1) {
                    if (wRatio > hRatio) {
                        op.inSampleSize = wRatio + 5;
                    } else {
                        op.inSampleSize = wRatio + 5;
                    }
                }
		Log.d("wang","getImageforSize op.inSampleSize is " + op.inSampleSize);
                op.inJustDecodeBounds = false;
                Bitmap pic1 = BitmapFactory.decodeFile(path, op);
                Log.d("wang", "getImageforSize return bimap pic1 " + pic1);
                //pic = BitmapFactory.decodeStream(this.getContentResolver()
                 //       .openInputStream(imageFilePath), null, op);

                return pic1;
        }


}
