package com.example.map0802;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.R;
import com.example.base.BaseHandler;
import com.example.base.BaseMessage;
import com.example.base.BaseTask;
import com.example.base.BaseUi;
import com.example.base.C;
import com.example.list.CommentAdapter;
import com.example.list.ReplyAdapter;
import com.example.model.Camera;
import com.example.model.Comment;
import com.example.model.Reply;
import com.example.util.AppCache;

import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ScaleImageActivity extends BaseUi{
		
		private String url;
		private ImageView image;
@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.scale_image);
		image = (ImageView)findViewById(R.id.image_scale);
		url = getIntent().getExtras().get("url").toString();
                Bitmap face = AppCache.getImageAsScreen(ScaleImageActivity.this,url);
                image.setImageBitmap(face);
                image.setOnTouchListener(new TouchListener());
	}
private final class TouchListener implements OnTouchListener {

        /** 记录是拖拉照片模式还是放大缩小照片模式 */
        private int mode = 0;// 初始状态    
        /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;

        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        /** 用于记录拖拉图片移动的坐标位置 */
        private Matrix matrix = new Matrix();
        /** 用于记录图片要进行拖拉时候的坐标位置 */
        private Matrix currentMatrix = new Matrix();

        /** 两个手指的开始距离 */
        private float startDis;
        /** 两个手指的中间点 */
        private PointF midPoint;

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (arg1.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕  
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DRAG;
                // 记录ImageView当前的移动位置  
                currentMatrix.set(image.getImageMatrix());
                startPoint.set(arg1.getX(), arg1.getY());
                break;
            // 手指在屏幕上移动，改事件会被不断触发  
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片  
                if (mode == MODE_DRAG) {
                    float dx = arg1.getX() - startPoint.x; // 得到x轴的移动距离  
                    float dy = arg1.getY() - startPoint.y; // 得到x轴的移动距离  
                    // 在没有移动之前的位置上进行移动  
                    matrix.set(currentMatrix);
                    matrix.postTranslate(dx, dy);
                }
               // 放大缩小图片  
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(arg1);// 结束距离  
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10  
                        float scale = endDis / startDis;// 得到缩放倍数  
                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                    }

                }
                break;
            // 手指离开屏幕  
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)  
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕  
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = distance(arg1);
                /** 计算两个手指间的中间点 */
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10  
                    midPoint = mid(arg1);
                    //记录当前ImageView的缩放倍数  
                    currentMatrix.set(image.getImageMatrix());
                }
                break;
            }
            image.setImageMatrix(matrix);
            return true;
        }

        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return FloatMath.sqrt(dx * dx + dy * dy);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }



    }

}
