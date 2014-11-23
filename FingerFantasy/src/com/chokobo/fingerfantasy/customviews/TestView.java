package com.chokobo.fingerfantasy.customviews;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chokobo.fingerfantasy.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestView extends View {

	private static final int TARGETS_NUM = 3;
	private Paint paint;
	private Path path;
	private List<PointF> pointList = new ArrayList<PointF>();
	private float EPS = (float) 1e-9;
	private List<List<PointF>> polygons;
	private List<RoundingTarget> targets;

	public TestView(Context context) {
		super(context);
		init();
	}

	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setupPaint();  //手書き処理
		makeTargets(); 
		polygons = new ArrayList<List<PointF>>();
	}

	private void makeTargets() {
		targets = new ArrayList<RoundingTarget>();
		Bitmap target = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		while (targets.size() < TARGETS_NUM) {
			float targetX = new Random(System.currentTimeMillis()).nextFloat() * 400 + 100; //100-500
			float targetY = new Random(System.currentTimeMillis()).nextFloat() * 800 + 100; //100-900
			if (!isNearAnyOtherTargets(targetX, targetY)) { //近くに居るかの判定
				targets.add(new RoundingTarget(targetX, targetY, target));
			}
		}
	}

	private boolean isNearAnyOtherTargets(float targetX, float targetY) {
		if (targets == null || targets.size() == 0)
			return false;
		for (RoundingTarget t : targets) {
			if (t.isNearBy(targetX, targetY))
				return true;
		}
		return false;
	}

	private void setupPaint() {
		path = new Path();

		paint = new Paint();
		paint.setColor(0xFF008800);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(path, paint);
		for (RoundingTarget t : targets)
			canvas.drawBitmap(t.image, t.x, t.y, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(x, y);
			pointList.add(new PointF(x, y));
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(x, y);
			pointList.add(new PointF(x, y));
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			checkCrossing();
			checkInside();
			path.reset();
			invalidate();
			break;
		}
		return true;
	}

	private void checkCrossing() {
		Log.d("up", Integer.toString(pointList.size()));
		for (int i = 0; i < pointList.size() - 1; i++) {
			for (int j = i + 1; j < pointList.size() - 1; j++) {
				PointF A = pointList.get(i);
				PointF B = pointList.get(i + 1);
				PointF C = pointList.get(j);
				PointF D = pointList.get(j + 1);
				float acx = C.x - A.x;
				float acy = C.y - A.y;
				float BUNBO = (B.x - A.x) * (D.y - C.y) - (B.y - A.y)
						* (D.x - C.x);
				if (Math.abs(BUNBO) < EPS)
					continue;
				float r = ((D.y - C.y) * acx - (D.x - C.x) * acy) / BUNBO;
				float s = ((B.y - A.y) * acx - (B.x - A.x) * acy) / BUNBO;
				if (r >= EPS && r <= 1 && s >= EPS && s <= 1)
					addPolygon(i, j);
			}
		}
		pointList.clear();
	}

	private void addPolygon(int s, int g) {
		List<PointF> list = new ArrayList<PointF>();
		for (int i = s; i <= g; i++)
			list.add(pointList.get(i));
		polygons.add(list);
	}

	private void checkInside() {
		if (targets.size() == 0)
			return;
		for (int i = 0; i < targets.size(); i++) {
			RoundingTarget t = targets.get(i);
			PointF targetPoint = new PointF(t.centerX, t.centerY);
			int count = 0;
			for (List<PointF> list : polygons) {
				for (int j = 0; j < list.size(); j++) {
					PointF a = list.get(j);
					PointF b = list.get((j + 1) % list.size());
					if ((targetPoint.x < a.x && targetPoint.x < b.x)
							&& (targetPoint.y < a.y && targetPoint.y > b.y || targetPoint.y > a.y
									&& targetPoint.y < b.y)) {
						count++;
					}
				}
				if (count % 2 == 1) {
					targets.remove(t);
					i--;
					break;
				}
			}
		}
		polygons.clear();
	}

	private class RoundingTarget {
		float x, y;
		float centerX, centerY;
		Bitmap image;

		public RoundingTarget(float x, float y, Bitmap image) {
			this.x = x;
			this.y = y;
			this.image = image;
			this.centerX = x + image.getWidth() / 2;
			this.centerY = y + image.getHeight() / 2;
		}

		public boolean isNearBy(float targetX, float targetY) {
			if (Math.abs(x - targetX) <= image.getWidth() + 30
					&& Math.abs(y - targetY) <= image.getHeight() + 30)
				return true;
			return false;
		}
	}
}
