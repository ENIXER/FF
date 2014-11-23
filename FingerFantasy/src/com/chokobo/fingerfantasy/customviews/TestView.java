package com.chokobo.fingerfantasy.customviews;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.chokobo.fingerfantasy.ActivityManager;
import com.chokobo.fingerfantasy.R;
import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
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
	/** アニメーション中かどうかのフラグ */
	private boolean isAnimated = false;
	/** アニメーション用のフレーム変数 */
	private int frame = 0;
	/** アニメーション用タイマー */
	private Timer timer;
	/** アニメーション種別 */
	private int type = 0;
	private Bitmap anim;
	public int aniCount = 0;
	/** アニメーション用の画像IDリスト */
	private int[][] resources = {
			{ R.drawable.wind_001, R.drawable.wind_002, R.drawable.wind_003,
					R.drawable.wind_004, R.drawable.wind_005,
					R.drawable.wind_006, R.drawable.wind_007,
					R.drawable.wind_008, R.drawable.wind_009,
					R.drawable.wind_010, R.drawable.wind_011,
					R.drawable.wind_012, R.drawable.wind_013,
					R.drawable.wind_014, R.drawable.wind_015,
					R.drawable.wind_016, R.drawable.wind_017,
					R.drawable.wind_018, R.drawable.wind_019,
					R.drawable.wind_020, R.drawable.wind_021,
					R.drawable.wind_022, R.drawable.wind_023,
					R.drawable.wind_024, R.drawable.wind_025,
					R.drawable.wind_026, R.drawable.wind_027,
					R.drawable.wind_028, R.drawable.wind_029,
					R.drawable.wind_030, R.drawable.wind_031,
					R.drawable.wind_032, R.drawable.wind_033,
					R.drawable.wind_034, R.drawable.wind_035,
					R.drawable.wind_036, R.drawable.wind_037,
					R.drawable.wind_038, R.drawable.wind_039,
					R.drawable.wind_040, R.drawable.wind_041,
					R.drawable.wind_042, R.drawable.wind_043,
					R.drawable.wind_044, R.drawable.wind_045,
					R.drawable.wind_046, R.drawable.wind_047,
					R.drawable.wind_048, R.drawable.wind_049,
					R.drawable.wind_050, R.drawable.wind_051,
					R.drawable.wind_052, R.drawable.wind_053,
					R.drawable.wind_054, R.drawable.wind_055,
					R.drawable.wind_056, R.drawable.wind_057,
					R.drawable.wind_058, R.drawable.wind_059,
					R.drawable.wind_060, R.drawable.wind_061,
					R.drawable.wind_062, R.drawable.wind_063,
					R.drawable.wind_064, R.drawable.wind_065,
					R.drawable.wind_066, R.drawable.wind_067,
					R.drawable.wind_068, R.drawable.wind_069,
					R.drawable.wind_070, R.drawable.wind_071,
					R.drawable.wind_072, R.drawable.wind_073,
					R.drawable.wind_074, R.drawable.wind_075,
					R.drawable.wind_076, R.drawable.wind_077,
					R.drawable.wind_078, R.drawable.wind_079,
					R.drawable.wind_080, R.drawable.wind_081, },
			{ R.drawable.ice_001, R.drawable.ice_002, R.drawable.ice_003,
					R.drawable.ice_004, R.drawable.ice_005, R.drawable.ice_006,
					R.drawable.ice_007, R.drawable.ice_008, R.drawable.ice_009,
					R.drawable.ice_010, R.drawable.ice_011, R.drawable.ice_012,
					R.drawable.ice_013, R.drawable.ice_014, R.drawable.ice_015,
					R.drawable.ice_016, R.drawable.ice_017, R.drawable.ice_018,
					R.drawable.ice_019, R.drawable.ice_020, R.drawable.ice_021,
					R.drawable.ice_022, R.drawable.ice_023, R.drawable.ice_024,
					R.drawable.ice_025, R.drawable.ice_026, R.drawable.ice_027,
					R.drawable.ice_028, R.drawable.ice_029, R.drawable.ice_030,
					R.drawable.ice_031, R.drawable.ice_032, R.drawable.ice_033,
					R.drawable.ice_034, R.drawable.ice_035, R.drawable.ice_036,
					R.drawable.ice_037, R.drawable.ice_038, R.drawable.ice_039,
					R.drawable.ice_040, R.drawable.ice_041, R.drawable.ice_042,
					R.drawable.ice_043, R.drawable.ice_044, R.drawable.ice_045,
					R.drawable.ice_046, R.drawable.ice_047, R.drawable.ice_048,
					R.drawable.ice_049, R.drawable.ice_050, R.drawable.ice_051,
					R.drawable.ice_052, R.drawable.ice_053, R.drawable.ice_054,
					R.drawable.ice_055, R.drawable.ice_056, R.drawable.ice_057,
					R.drawable.ice_058, R.drawable.ice_059, R.drawable.ice_060,
					R.drawable.ice_061, R.drawable.ice_062, R.drawable.ice_063,
					R.drawable.ice_064, R.drawable.ice_065, R.drawable.ice_066,
					R.drawable.ice_067, R.drawable.ice_068, R.drawable.ice_069,
					R.drawable.ice_070, R.drawable.ice_071, R.drawable.ice_072,
					R.drawable.ice_073, R.drawable.ice_074, R.drawable.ice_075,
					R.drawable.ice_076, R.drawable.ice_077, R.drawable.ice_078,
					R.drawable.ice_079, R.drawable.ice_080, R.drawable.ice_081, } };

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
		if (targets == null)
			targets = new ArrayList<RoundingTarget>();

		Bitmap origin_image = BitmapFactory.decodeResource(getResources(),
				R.drawable.crystal2);
		Bitmap target = Bitmap.createScaledBitmap(origin_image, 70, 70, false);
		Random rand = new Random(System.currentTimeMillis());
		Set<Integer> places = new HashSet<Integer>();
		if (!targets.isEmpty())
			for (int i = 0; i < targets.size(); i++) {
				places.add((int) (targets.get(i).x / 100 - 1 + (targets.get(i).y / 100 - 1) * 8));
			}
		for (int i = targets.size(); i < TARGETS_NUM; i++) {
			int p = rand.nextInt(40);
			while (!places.add(p))
				p = rand.nextInt(40);
			int targetX = p % 5 * 100 + 100;
			int targetY = p / 5 * 100 + 100;
			targets.add(new RoundingTarget(p, targetX, targetY, target));
		}
	}

	/** 指で描く線の設定 */
	private void setupPaint() {
		path = new Path();

		paint = new Paint();
		paint.setColor(0xFF008800);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(path, paint);
		for (RoundingTarget t : targets)
			canvas.drawBitmap(t.image, t.x, t.y, paint);
		if (isAnimated && frame < resources[type].length) {
			anim = BitmapFactory.decodeResource(getResources(),
					resources[type][frame]);
			canvas.drawBitmap(anim, 0, 0, paint);
			isAnimated = false;
			anim.recycle();
			frame++;
		}
		isAnimated = false;
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
			detectPolygon();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			path.reset();
			detectPolygon();
			deleteTargets();
			makeTargets();
			invalidate();
			CharacterManager.damage(CharacterManager.getEnemy(), totalDamage);
			CharacterManager.decEnemyturn();
			if (CharacterManager.isEnemyDead())
				ActivityManager.intentActivity(); // 敵を討伐判定
			if (CharacterManager.isEnemyturn())
				CharacterManager.damage(CharacterManager.getPlayer(),
						CharacterManager.getEnemyAtk()); // 敵の攻撃
			if (CharacterManager.isPlayerDead())
				ActivityManager.intentActivity(); // プレイヤー死亡判定
			makeTargets();
			totalDamage = 0;
			break;
		}
		return true;
	}

	/** 指で描かれた線を見て、交差部分がある場合は多角形を追加する */
	private void detectPolygon() {
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
		for (int i = 0; i < polygons.size(); i++)
			for (int j = 0; j < polygons.size(); j++)
				if (i != j) {
					polygons.get(i).removeAll(polygons.get(j));
				}
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

	private void deleteTargets() {
		List<Integer> idList = new ArrayList<Integer>();
		if (targets.size() == 0)
			return;
		for (List<PointF> list : polygons) {
			int pointsOfDeletion = 0;
			int deletionId = -1;
			int count = 0;
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
					//animation();
					targets.remove(t);
					i--;
					pointList.clear();
					totalDamage += CharacterManager.getPlayerAtk();
					break;
				}
			}
			if (pointsOfDeletion == 1) {
				idList.add(deletionId);
			}
		}
		if (idList.size() > 0) {
			animation();
			for (int deletionId : idList)
				targets.remove(deletionId);
			totalDamage += CharacterManager.getPlayerAtk();
		}
		pointList.clear();
		polygons.clear();
	}

	private void animation() {
		Random rand = new Random(System.currentTimeMillis());
		type = rand.nextInt(2);
		frame = 0;
		final Handler handler = new Handler();
		timer = new Timer(false);
		timer.schedule(new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						isAnimated = true;
						invalidate();
					}
				});
			}
		}, 0, 33);
	}

	/**
	 * 囲う対象となるターゲット
	 * 
	 * @author ENIXER
	 * 
	 */
	private class RoundingTarget {
		int id;
		float x, y;
		float centerX, centerY;
		Bitmap image;

		public RoundingTarget(int id, float x, float y, Bitmap image) {
			this.x = x;
			this.y = y;
			this.image = image;
			this.centerX = x + image.getWidth() / 2;
			this.centerY = y + image.getHeight() / 2;
			this.id = id;
		}

	}
}
