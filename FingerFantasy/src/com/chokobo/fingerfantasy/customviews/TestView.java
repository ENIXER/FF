package com.chokobo.fingerfantasy.customviews;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.chokobo.fingerfantasy.R;
import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 一筆書きの描画領域
 * 
 * @author ENIXER
 *
 */
public class TestView extends View {

	/** ターゲットの個数 */
	private static int TARGETS_NUM = 3;
	/** 指で描いた軌跡を表示するために使用する */
	private Paint paint;
	/** 指で描いた軌跡を保持するために使用する */
	private Path path;
	/** 指で描いた軌跡の座標のリスト */
	private List<PointF> pointList = new ArrayList<PointF>();
	/** float同士の大小比較で誤差による誤動作を防ぐための値 */
	private float EPS = (float) 1e-9;
	/** 指で描いた軌跡のうち、囲まれた部分を保持しておくために使用する */
	private List<List<PointF>> polygons;
	/** 囲むべきターゲットのリスト */
	private List<RoundingTarget> targets;
	/** 与える総ダメージ */
	private int totalDamage;
	
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

	/** 初期設定を行う */
	private void init() {
		setupPaint(); // 手書き処理
		makeTargets();
		polygons = new ArrayList<List<PointF>>();
	}

	/** ターゲットを{@link TARGETS_NUM}個だけ互いに重なり合わないように生成する */
	private void makeTargets() {
		targets = new ArrayList<RoundingTarget>();
	
		
		
		Bitmap origin_image = BitmapFactory.decodeResource(getResources(),
				R.drawable.crystal2);
		Bitmap target = Bitmap.createScaledBitmap(origin_image, 70, 70, false);
		Random rand = new Random(System.currentTimeMillis());
		Set<Integer> places = new HashSet<Integer>();
		for (int i = 0; i < TARGETS_NUM; i++) {
			int p = rand.nextInt(40);
			while (!places.add(p))
				p = rand.nextInt(40);
			int targetX = p % 5 * 100 + 100;
			int targetY = p / 8 * 100 + 100;
			targets.add(new RoundingTarget(targetX, targetY, target));
		}
	}

	/** 指で描く線の設定 */
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
			checkCrossing();
			checkInside();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			path.reset();
			invalidate();
			CharacterManager.damage(totalDamage);
			totalDamage = 0;
			break;
		}
		return true;
	}

	/** 指で描かれた線を見て、交差部分がある場合は多角形を追加する */
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
		//pointList.clear();
	}

	/**
	 * 多角形を追加する
	 * 
	 * @param s
	 *            スタートとなる頂点のインデックス
	 * @param g
	 *            ゴールとなる頂点のインデックス
	 */
	private void addPolygon(int s, int g) {
		List<PointF> list = new ArrayList<PointF>();
		for (int i = s; i <= g; i++)
			list.add(pointList.get(i));
		polygons.add(list);
	}

	private void checkInside() {
		if (targets.size() == 0)
			return;
		int count = 0;
		for (List<PointF> list : polygons) {
			for (int i = 0; i < targets.size(); i++) {
				RoundingTarget t = targets.get(i);
				PointF targetPoint = new PointF(t.centerX, t.centerY);
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
					pointList.clear();
					totalDamage += CharacterManager.getAtk();
					break;
				}
			}
		}
		polygons.clear();
	}

	/**
	 * 囲う対象となるターゲット
	 * 
	 * @author ENIXER
	 *
	 */
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
