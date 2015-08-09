package com.example.view;

import com.example.news.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ToggleButton
 * 
 * @author YuQR
 * 
 */
public class ToggleButton extends View {

	private Bitmap switch_bg_on;
	private Bitmap switch_bg_off;
	private Bitmap slipper_bg;
	private ToggleState state = ToggleState.Close; // 开关默认状态为关
	private boolean sliding = false; // 是否滑动
	private float x; // 触摸x坐标
	private OnToggleChangeListener listener; // 开关监听器

	public enum ToggleState {
		Open, Close
	}

	public void setSwitchOnBackground(int switch_bg_on) {
		this.switch_bg_on = BitmapFactory.decodeResource(getResources(),
				switch_bg_on);
	}

	public void setSwitchOffBackground(int switch_bg_off) {
		this.switch_bg_off = BitmapFactory.decodeResource(getResources(),
				switch_bg_off);
	}

	public void setSlipperBackground(int slipper) {
		this.slipper_bg = BitmapFactory.decodeResource(getResources(), slipper);
	}

	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		switch_bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.switch_btn_off);
		switch_bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.switch_btn_on);
		slipper_bg = BitmapFactory.decodeResource(getResources(), R.drawable.switch_btn_slipper);
	}

	public ToggleButton(Context context) {
		super(context);
		initView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(switch_bg_on.getWidth(), switch_bg_on.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (sliding) {
			float flag = x - slipper_bg.getWidth() / 2;
			if (flag < 0)
				flag = 0;
			else if (flag > switch_bg_on.getWidth() - slipper_bg.getWidth())
				flag = switch_bg_on.getWidth() - slipper_bg.getWidth();
			if (state == ToggleState.Close) {
				canvas.drawBitmap(switch_bg_off, 0, 0, null);
			} else {
				canvas.drawBitmap(switch_bg_on, 0, 0, null);
			}
			canvas.drawBitmap(slipper_bg, flag, 0, null);
		} else {
			ToggleState temp = state;
			if (x < switch_bg_off.getWidth() / 2) {
				canvas.drawBitmap(switch_bg_off, 0, 0, null);
				canvas.drawBitmap(slipper_bg, 0, 0, null);
				state = ToggleState.Close;
			} else {
				canvas.drawBitmap(switch_bg_on, 0, 0, null);
				canvas.drawBitmap(slipper_bg, switch_bg_on.getWidth()
						- slipper_bg.getWidth(), 0, null);
				state = ToggleState.Open;
			}
			if (temp != state) {
				if (listener != null)
					listener.toggleChange(state);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			sliding = true;
			break;
		case MotionEvent.ACTION_MOVE:
			x = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			x = event.getX();
			sliding = false;
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}

	public ToggleState getState() {
		return state;
	}

	public void setState(ToggleState state) {
		this.state = state;
	}

	public void setOnToggleChangeListener(OnToggleChangeListener listener) {
		this.listener = listener;
	}

	public interface OnToggleChangeListener {
		public void toggleChange(ToggleState state);
	}

}
