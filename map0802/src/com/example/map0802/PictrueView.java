package com.example.map0802;
import java.util.HashMap;
import java.util.List;

import com.example.R;
import com.example.util.SDUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class PictrueView extends Activity implements OnItemSelectedListener, ViewFactory, OnItemClickListener
{
	private Gallery gallery;
	private ImageView iv;
	private ImageAdapter imageAdapter;
	private int mCurrentPos = -1;// 褰撳墠鐨刬tem
	private HashMap<Integer, ImageView> mViewMap;
	private List<Bitmap> bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gallery = (Gallery) findViewById(R.id.gallery);
		bitmap = SDUtil.getImagePathFromSD(); 
		imageAdapter = new ImageAdapter(this, bitmap.size());
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemSelectedListener(this);
		gallery.setSelection(1);// 璁剧疆涓�鍔犺浇Activity灏辨樉绀虹殑鍥剧墖涓虹浜屽紶

		gallery.setOnItemClickListener(this);

		iv = (ImageView) findViewById(R.id.iv);
		//imageSwitcher.setFactory(this);

		// 璁剧疆鍔ㄧ敾鏁堟灉 娣″叆娣″嚭
		//imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		//imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
	}

	public class ImageAdapter extends BaseAdapter
	{
		int mGalleryItemBackground;
		private Context mContext;
		private int mCount;// 涓�鍏卞灏戜釜item

		public ImageAdapter(Context context, int count)
		{
			mContext = context;
			mCount = count;
			mViewMap = new HashMap<Integer, ImageView>(count);
			//TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
			// 璁剧疆杈规鐨勬牱寮�
			//mGalleryItemBackground = typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
		}

		public int getCount()
		{
			return mCount;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageview = mViewMap.get(position % mCount);
			if (imageview == null)
			{
				imageview = new ImageView(mContext);
				imageview.setImageBitmap(bitmap.get(position % bitmap.size()));
				imageview.setScaleType(ImageView.ScaleType.FIT_XY);
				imageview.setLayoutParams(new Gallery.LayoutParams(300,200));
				//imageview.setBackgroundResource(mGalleryItemBackground);
				mViewMap.put((position % mCount), imageview);
			}
			return imageview;
		}
	}

	// 婊戝姩Gallery鐨勬椂鍊欙紝ImageView涓嶆柇鏄剧ず褰撳墠鐨刬tem
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		// imageSwitcher.setImageResource(resIds[position % resIds.length]);
	}

	// 璁剧疆鐐瑰嚮Gallery鐨勬椂鍊欐墠鍒囨崲鍒拌鍥剧墖
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		/*if (mCurrentPos == position)
		{
			// 濡傛灉鍦ㄦ樉绀哄綋鍓嶅浘鐗囷紝鍐嶇偣鍑伙紝灏变笉鍐嶅姞杞姐��
			return;
		}*/
		//mCurrentPos = position;
		iv.setImageBitmap(bitmap.get(position % bitmap.size()));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
	}

	@Override
	public View makeView()
	{
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFF000000);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		return imageView;
	}

}
