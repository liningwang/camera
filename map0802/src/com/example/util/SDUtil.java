package com.example.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class SDUtil {

	public static final String faces = "/sdcard/demos";
	private static String TAG = SDUtil.class.getSimpleName();
	private static double MB = 1024;
	private static double FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static double IMAGE_EXPIRE_TIME = 10;
	public static int displayh;
	public static int displayw;

	public static Bitmap getImage(String fileName) {
		// check image file exists
		String realFileName = faces + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		// get original image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(realFileName, options);
	}
	
	public static Bitmap getSample(String fileName) {
		// check image file exists
		String realFileName = faces + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		// get original size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(realFileName, options);
		int zoom = (int) (options.outHeight / (float) 50);
		if (zoom < 0) zoom = 1;
		// get resized image
		options.inSampleSize = zoom;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(realFileName, options);
		return bitmap;
	}

	public static void computeScreenSize(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
                displayw = display.getWidth();
                displayh = display.getHeight();
	}	
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        	InputStream is = new ByteArrayInputStream(baos.toByteArray());
        	return is;
    }

	public static InputStream getImageStreamfromPath(String path) {

                // check image file exists
		InputStream is;
                File file = new File(path);
                if (!file.exists()) {
                        return null;
               	}
                /*File file = new File(realFileName);
                if (!file.exists()) {
                        return null;
                }*/
                int dw = displayw;
                int dh = displayh;
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                Bitmap pic = BitmapFactory.decodeFile(path, op);
                //Bitmap pic = BitmapFactory.decodeStream(this
                //        .getContentResolver().openInputStream(imageFilePath),
                //       null, op);
		int wRatio = (int) Math.round(op.outWidth / (float) dw);
                int hRatio = (int) Math.round(op.outHeight / (float) dh);
                Log.d("wang", "getImageStreamfromPath dw = " + dw + " wRatio = " + wRatio + " outWidth = " + op.outWidth);
                Log.d("wang", "getImageStreamfromPath dh = " + dh + "hRatio = " + hRatio + " outHeight = " + op.outHeight);
                if (wRatio > 1 && hRatio > 1) {
                    if (wRatio > hRatio) {
                        op.inSampleSize = wRatio;
                    } else {
                        op.inSampleSize = hRatio;
                    }
                }
                op.inJustDecodeBounds = false;
                Bitmap pic1 = BitmapFactory.decodeFile(path, op);
		is = Bitmap2InputStream(pic1,100);
                //pic = BitmapFactory.decodeStream(this.getContentResolver()
                 //       .openInputStream(imageFilePath), null, op);
                return is;
        }

	public static Bitmap getImagefromPath(Activity activity,String path) {
		
		// check image file exists
		/*File file = new File(path);
		if (!file.exists()) {
			return null;
		}*/
		String realFileName = faces + "/" + path;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		Display display = activity.getWindowManager().getDefaultDisplay();
                int dw = display.getWidth();
                int dh = display.getHeight();
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                Bitmap pic = BitmapFactory.decodeFile(realFileName, op);                                                     
                //Bitmap pic = BitmapFactory.decodeStream(this
                //        .getContentResolver().openInputStream(imageFilePath),
                //       null, op);
                int wRatio = op.outWidth / dw;               
                int hRatio = op.outHeight / dh;               
                Log.d("wang", "dw = " + dw + " wRatio = " + wRatio + " outWidth = " + op.outWidth);        
                Log.d("wang", "dh = " + dh + "hRatio = " + hRatio + " outHeight = " + op.outHeight);         
                if (wRatio > 1 && hRatio > 1) {
                    if (wRatio > hRatio) {
                        op.inSampleSize = wRatio;
                    } else {
                        op.inSampleSize = hRatio;
                    }
                }
                op.inJustDecodeBounds = false;                                                                          
                Bitmap pic1 = BitmapFactory.decodeFile(realFileName, op);
                //pic = BitmapFactory.decodeStream(this.getContentResolver()
                 //       .openInputStream(imageFilePath), null, op);
                
		return pic1;
	}
	 public static Bitmap getImageforSize(String path,int dw,int dh) {

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
                if (wRatio > 1 && hRatio > 1) {
                    if (wRatio > hRatio) {
                        op.inSampleSize = wRatio;
                    } else {
                        op.inSampleSize = wRatio;
                    }
                }
                op.inJustDecodeBounds = false;
                Bitmap pic1 = BitmapFactory.decodeFile(path, op);
                //pic = BitmapFactory.decodeStream(this.getContentResolver()
                 //       .openInputStream(imageFilePath), null, op);

                return pic1;
        }
public static Bitmap getImageCacheforSize(String path,int dw,int dh) {

                // check image file exists
                /*File file = new File(path);
                if (!file.exists()) {
                        return null;
                }*/
		String realFileName = faces + "/" + path;
                File file = new File(realFileName);
                if (!file.exists()) {
                        return null;
                }

                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                Bitmap pic = BitmapFactory.decodeFile(realFileName, op);
                //Bitmap pic = BitmapFactory.decodeStream(this
                //        .getContentResolver().openInputStream(imageFilePath),
                //       null, op);
                int wRatio = (int) Math.floor(op.outWidth / (float) dw);
                int hRatio = (int) Math.floor(op.outHeight / (float) dh);
                Log.d("wang", "dw = " + dw + " wRatio = " + wRatio + " outWidth = " + op.outWidth);
                Log.d("wang", "dh = " + dh + "hRatio = " + hRatio + " outHeight = " + op.outHeight);
                if (wRatio > 1 && hRatio > 1) {
                    if (wRatio > hRatio) {
                        op.inSampleSize = wRatio;
                    } else {
                        op.inSampleSize = wRatio;
                    }
                }
                op.inJustDecodeBounds = false;
                Bitmap pic1 = BitmapFactory.decodeFile(realFileName, op);
                //pic = BitmapFactory.decodeStream(this.getContentResolver()
                 //       .openInputStream(imageFilePath), null, op);

                return pic1;
        }	
	public static void measureMyView(View view, ViewSize op) {
		int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		view.measure(width,height);
		int height1=view.getMeasuredHeight();
		int width1=view.getMeasuredWidth();
		op.height = height1;
		op.width = width1;
	} 
	public static class ViewSize {
		public int height;
		public int width;
	}
	public static void saveImage(Bitmap bitmap, String fileName) {
		if (bitmap == null) {
			Log.w(TAG, " trying to save null bitmap");
			return;
		}
		// 鍒ゆ柇sdcard涓婄殑绌洪棿
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			Log.w(TAG, "Low free space onsd, do not cache");
			return;
		}
		// 涓嶅瓨鍦ㄥ垯鍒涘缓鐩綍
		File dir = new File(faces);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 淇濆瓨鍥剧墖
		try {
			String realFileName = faces + "/" + fileName;
			File file = new File(realFileName);
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			Log.i(TAG, "Image saved tosd");
		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFoundException");
		} catch (IOException e) {
			Log.w(TAG, "IOException");
		}
	}

	protected static void updateTime(String fileName) {
		File file = new File(fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}
	// 浠巗d鍗¤幏鍙栧浘鐗囪祫婧�
	public static List<Bitmap> getImagePathFromSD() {
	
	 	// 鍥剧墖鍒楄〃
		 List<Bitmap> picList = new ArrayList<Bitmap>();
		 
		 // 寰楀埌sd鍗″唴璺緞
		  String imagePath = faces + "/";
		
		 // 寰楀埌璇ヨ矾寰勬枃浠跺す涓嬫墍鏈夌殑鏂囦欢
		Log.d("wang","entry getImagePathFromSD");
		  File mfile = new File(imagePath);
		  if(!mfile.exists()){
			  mfile.mkdirs();
		  }
		Log.d("wang","dir is " + mfile.getPath());
		  File[] files = mfile.listFiles();
		
		 // 灏嗘墍鏈夌殑鏂囦欢瀛樺叆ArrayList涓�,骞惰繃婊ゆ墍鏈夊浘鐗囨牸寮忕殑鏂囦欢
		 for (int i = 0; i < files.length; i++) {
		  File file = files[i];
		  if (checkIsImageFile(file.getPath())) {
			Log.d("wang","the file is " + file.getPath());
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			Bitmap image = BitmapFactory.decodeFile(file.getPath(), options);
		   	picList.add(image);
		  }
		
		 }
		
		 // 杩斿洖寰楀埌鐨勫浘鐗囧垪琛�
		 return picList;
	
	}
	
	// 妫�鏌ユ墿灞曞悕锛屽緱鍒板浘鐗囨牸寮忕殑鏂囦欢
	public static boolean checkIsImageFile(String fName) {
		 boolean isImageFile = false;
		
		 // 鑾峰彇鎵╁睍鍚�
		 String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
		   fName.length()).toLowerCase();
		 if (FileEnd.equals("jpg") || FileEnd.equals("gif")
		   || FileEnd.equals("png") || FileEnd.equals("jpeg")
		   || FileEnd.equals("bmp")) {
		  isImageFile = true;
		 } else {
		  isImageFile = false;
		 }
		
		 return isImageFile;
	
	} 
	/**
	 * 璁＄畻sdcard涓婄殑鍓╀綑绌洪棿
	 * 
	 * @return
	 */
	public static int getFreeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	public static void removeExpiredCache(String dirPath, String filename) {
		File file = new File(dirPath, filename);
		if (System.currentTimeMillis() - file.lastModified() > IMAGE_EXPIRE_TIME) {
			Log.i(TAG, "Clear some expiredcache files ");
			file.delete();
		}
	}

	public static void removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.i(TAG, "Clear some expiredcache files ");
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}

		}

	}

	private static class FileLastModifSort implements Comparator<File> {
		@Override
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
