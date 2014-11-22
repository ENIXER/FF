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

	private Paint paint;
	private Path path;
	private List<PointF> pointList = new ArrayList<PointF>();
	private float EPS = (float) 1e-9;
	private List<List<PointF>> polygons;
	private Bitmap target;

	private float targetX, targetY;

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
		path = new Path();

		paint = new Paint();
		paint.setColor(0xFF008800);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(10);

		targetX = new Random(System.currentTimeMillis()).nextFloat() * 400 + 100;
		targetY = new Random(System.currentTimeMillis()).nextFloat() * 800 + 100;
		target = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		polygons = new ArrayList<List<PointF>>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(path, paint);
		if (target != null)
			canvas.drawBitmap(target, targetX, targetY, paint);
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
		if (target == null)
			return;
		PointF targetPoint = new PointF(targetX + target.getWidth() / 2,
				targetY + target.getHeight() / 2);
		int count = 0;
		for (List<PointF> list : polygons) {
			for (int i = 0; i < list.size(); i++) {
				PointF a = list.get(i);
				PointF b = list.get((i + 1) % list.size());
				if ((targetPoint.x < a.x && targetPoint.x < b.x)
						&& (targetPoint.y < a.y && targetPoint.y > b.y || targetPoint.y > a.y
								&& targetPoint.y < b.y)) {
					count++;
				}
			}
			if (count % 2 == 1) {
				reset();
			}
		}
	}

	private void reset() {
		target = null;
		init();
		polygons.clear();
	}
}
