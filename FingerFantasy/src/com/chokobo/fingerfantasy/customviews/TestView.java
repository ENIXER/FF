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
	private int TARGETS_NUM = 0;
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
	/** 消したターゲットの個数 */
	private int totalDeleted;
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
					R.drawable.wind_080, R.drawable.wind_081,
					R.drawable.wind_082, R.drawable.wind_083,
					R.drawable.wind_084, R.drawable.wind_085,
					R.drawable.wind_086, R.drawable.wind_087,
					R.drawable.wind_088, R.drawable.wind_089,
					R.drawable.wind_090, R.drawable.wind_091,
					R.drawable.wind_092, R.drawable.wind_093,
					R.drawable.wind_094, R.drawable.wind_095,
					R.drawable.wind_096, R.drawable.wind_097,
					R.drawable.wind_098, R.drawable.wind_099,
					R.drawable.wind_100, R.drawable.wind_101,
					R.drawable.wind_102, R.drawable.wind_103,
					R.drawable.wind_104, R.drawable.wind_105,
					R.drawable.wind_106, R.drawable.wind_107,
					R.drawable.wind_108, R.drawable.wind_109,
					R.drawable.wind_110, R.drawable.wind_111,
					R.drawable.wind_112, R.drawable.wind_113,
					R.drawable.wind_114, R.drawable.wind_115,
					R.drawable.wind_116, R.drawable.wind_117,
					R.drawable.wind_118, R.drawable.wind_119,
					R.drawable.wind_120, },
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
					R.drawable.ice_079, R.drawable.ice_080, R.drawable.ice_081,
					R.drawable.ice_082, R.drawable.ice_083, R.drawable.ice_084,
					R.drawable.ice_085, R.drawable.ice_086, R.drawable.ice_087,
					R.drawable.ice_088, R.drawable.ice_089, R.drawable.ice_090,
					R.drawable.ice_091, R.drawable.ice_092, R.drawable.ice_093,
					R.drawable.ice_094, R.drawable.ice_095, R.drawable.ice_096,
					R.drawable.ice_097, R.drawable.ice_098, R.drawable.ice_099,
					R.drawable.ice_100, R.drawable.ice_101, R.drawable.ice_102,
					R.drawable.ice_103, R.drawable.ice_104, R.drawable.ice_105,
					R.drawable.ice_106, R.drawable.ice_107, R.drawable.ice_108,
					R.drawable.ice_109, R.drawable.ice_110, R.drawable.ice_111,
					R.drawable.ice_112, R.drawable.ice_113, R.drawable.ice_114,
					R.drawable.ice_115, R.drawable.ice_116, R.drawable.ice_117,
					R.drawable.ice_118, R.drawable.ice_119, R.drawable.ice_120, },
			{ R.drawable.fire_001, R.drawable.fire_002, R.drawable.fire_003,
					R.drawable.fire_004, R.drawable.fire_005,
					R.drawable.fire_006, R.drawable.fire_007,
					R.drawable.fire_008, R.drawable.fire_009,
					R.drawable.fire_010, R.drawable.fire_011,
					R.drawable.fire_012, R.drawable.fire_013,
					R.drawable.fire_014, R.drawable.fire_015,
					R.drawable.fire_016, R.drawable.fire_017,
					R.drawable.fire_018, R.drawable.fire_019,
					R.drawable.fire_020, R.drawable.fire_021,
					R.drawable.fire_022, R.drawable.fire_023,
					R.drawable.fire_024, R.drawable.fire_025,
					R.drawable.fire_026, R.drawable.fire_027,
					R.drawable.fire_028, R.drawable.fire_029,
					R.drawable.fire_030, R.drawable.fire_031,
					R.drawable.fire_032, R.drawable.fire_033,
					R.drawable.fire_034, R.drawable.fire_035,
					R.drawable.fire_036, R.drawable.fire_037,
					R.drawable.fire_038, R.drawable.fire_039,
					R.drawable.fire_040, R.drawable.fire_041,
					R.drawable.fire_042, R.drawable.fire_043,
					R.drawable.fire_044, R.drawable.fire_045,
					R.drawable.fire_046, R.drawable.fire_047,
					R.drawable.fire_048, R.drawable.fire_049,
					R.drawable.fire_050, R.drawable.fire_051,
					R.drawable.fire_052, R.drawable.fire_053,
					R.drawable.fire_054, R.drawable.fire_055,
					R.drawable.fire_056, R.drawable.fire_057,
					R.drawable.fire_058, R.drawable.fire_059,
					R.drawable.fire_060, R.drawable.fire_061,
					R.drawable.fire_062, R.drawable.fire_063,
					R.drawable.fire_064, R.drawable.fire_065,
					R.drawable.fire_066, R.drawable.fire_067,
					R.drawable.fire_068, R.drawable.fire_069,
					R.drawable.fire_070, R.drawable.fire_071,
					R.drawable.fire_072, R.drawable.fire_073,
					R.drawable.fire_074, R.drawable.fire_075,
					R.drawable.fire_076, R.drawable.fire_077,
					R.drawable.fire_078, R.drawable.fire_079,
					R.drawable.fire_080, R.drawable.fire_081,
					R.drawable.fire_082, R.drawable.fire_083,
					R.drawable.fire_084, R.drawable.fire_085,
					R.drawable.fire_086, R.drawable.fire_087,
					R.drawable.fire_088, R.drawable.fire_089,
					R.drawable.fire_090, R.drawable.fire_091,
					R.drawable.fire_092, R.drawable.fire_093,
					R.drawable.fire_094, R.drawable.fire_095,
					R.drawable.fire_096, R.drawable.fire_097,
					R.drawable.fire_098, R.drawable.fire_099,
					R.drawable.fire_100, R.drawable.fire_101,
					R.drawable.fire_102, R.drawable.fire_103,
					R.drawable.fire_104, R.drawable.fire_105,
					R.drawable.fire_106, R.drawable.fire_107,
					R.drawable.fire_108, R.drawable.fire_109,
					R.drawable.fire_110, R.drawable.fire_111,
					R.drawable.fire_112, R.drawable.fire_113,
					R.drawable.fire_114, R.drawable.fire_115,
					R.drawable.fire_116, R.drawable.fire_117,
					R.drawable.fire_118, R.drawable.fire_119,
					R.drawable.fire_120, },
			{ R.drawable.holy_001, R.drawable.holy_002, R.drawable.holy_003,
					R.drawable.holy_004, R.drawable.holy_005,
					R.drawable.holy_006, R.drawable.holy_007,
					R.drawable.holy_008, R.drawable.holy_009,
					R.drawable.holy_010, R.drawable.holy_011,
					R.drawable.holy_012, R.drawable.holy_013,
					R.drawable.holy_014, R.drawable.holy_015,
					R.drawable.holy_016, R.drawable.holy_017,
					R.drawable.holy_018, R.drawable.holy_019,
					R.drawable.holy_020, R.drawable.holy_021,
					R.drawable.holy_022, R.drawable.holy_023,
					R.drawable.holy_024, R.drawable.holy_025,
					R.drawable.holy_026, R.drawable.holy_027,
					R.drawable.holy_028, R.drawable.holy_029,
					R.drawable.holy_030, R.drawable.holy_031,
					R.drawable.holy_032, R.drawable.holy_033,
					R.drawable.holy_034, R.drawable.holy_035,
					R.drawable.holy_036, R.drawable.holy_037,
					R.drawable.holy_038, R.drawable.holy_039,
					R.drawable.holy_040, R.drawable.holy_041,
					R.drawable.holy_042, R.drawable.holy_043,
					R.drawable.holy_044, R.drawable.holy_045,
					R.drawable.holy_046, R.drawable.holy_047,
					R.drawable.holy_048, R.drawable.holy_049,
					R.drawable.holy_050, R.drawable.holy_051,
					R.drawable.holy_052, R.drawable.holy_053,
					R.drawable.holy_054, R.drawable.holy_055,
					R.drawable.holy_056, R.drawable.holy_057,
					R.drawable.holy_058, R.drawable.holy_059,
					R.drawable.holy_060, R.drawable.holy_061,
					R.drawable.holy_062, R.drawable.holy_063,
					R.drawable.holy_064, R.drawable.holy_065,
					R.drawable.holy_066, R.drawable.holy_067,
					R.drawable.holy_068, R.drawable.holy_069,
					R.drawable.holy_070, R.drawable.holy_071,
					R.drawable.holy_072, R.drawable.holy_073,
					R.drawable.holy_074, R.drawable.holy_075,
					R.drawable.holy_076, R.drawable.holy_077,
					R.drawable.holy_078, R.drawable.holy_079,
					R.drawable.holy_080, R.drawable.holy_081,
					R.drawable.holy_082, R.drawable.holy_083,
					R.drawable.holy_084, R.drawable.holy_085,
					R.drawable.holy_086, R.drawable.holy_087,
					R.drawable.holy_088, R.drawable.holy_089,
					R.drawable.holy_090, R.drawable.holy_091,
					R.drawable.holy_092, R.drawable.holy_093,
					R.drawable.holy_094, R.drawable.holy_095,
					R.drawable.holy_096, R.drawable.holy_097,
					R.drawable.holy_098, R.drawable.holy_099,
					R.drawable.holy_100, R.drawable.holy_101,
					R.drawable.holy_102, R.drawable.holy_103,
					R.drawable.holy_104, R.drawable.holy_105,
					R.drawable.holy_106, R.drawable.holy_107,
					R.drawable.holy_108, R.drawable.holy_109,
					R.drawable.holy_110, R.drawable.holy_111,
					R.drawable.holy_112, R.drawable.holy_113,
					R.drawable.holy_114, R.drawable.holy_115,
					R.drawable.holy_116, R.drawable.holy_117,
					R.drawable.holy_118, R.drawable.holy_119,
					R.drawable.holy_120, }, };

	public TestView(Context context) {
		super(context);
	}

	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/** 初期設定を行う */
	public void init(int cryNum) {
		TARGETS_NUM = cryNum;
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
		places.add(29);
		places.add(34);
		if (!targets.isEmpty())
			for (int i = 0; i < targets.size(); i++) {
				places.add((int) (targets.get(i).x / 100 - 1 + (targets.get(i).y / 100 - 2) * 7));
			}
		for (int i = targets.size(); i < TARGETS_NUM; i++) {
			int p = rand.nextInt(35);
			while (!places.add(p))
				p = rand.nextInt(35);
			int targetX = p % 5 * 100 + 100;
			int targetY = p / 5 * 100 + 200;
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
		if (isAnimated && frame * 2 < resources[type].length) {
			anim = BitmapFactory.decodeResource(getResources(),
					resources[type][frame * 2]);
			canvas.drawBitmap(anim, 0, 0, paint);
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
			changeCrystal();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			path.reset();
			detectPolygon();
			deleteTargets();
			makeTargets();
			invalidate();
			int damage = calcDamage();
			CharacterManager.damage(CharacterManager.getEnemy(), damage);
			ActivityManager.showDamage(damage);
			CharacterManager.decEnemyturn();
			if (CharacterManager.isEnemyDead()) {
				ActivityManager.intentActivity(); // 敵を討伐判定
			}
			if (CharacterManager.isEnemyturn()) {
				CharacterManager.damage(CharacterManager.getPlayer(),
						CharacterManager.getEnemyAtk()); // 敵の攻撃
				CharacterManager.resetEnemyTurn();
				ActivityManager.setPlayerHp();
			}
			if (CharacterManager.isPlayerDead()) {
				ActivityManager.showContinue();
			}
			makeTargets();
			totalDeleted = 0;
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
				if (r > EPS && r < 1 && s > EPS && s < 1)
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
					animation();
					targets.remove(t);
					i--;
					pointList.clear();
					totalDeleted++;
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
			totalDeleted++;
		}
		pointList.clear();
		polygons.clear();
	}

	private void changeCrystal() {
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
					Bitmap origin_image = BitmapFactory.decodeResource(
							getResources(), R.drawable.crystal_select);
					Bitmap target = Bitmap.createScaledBitmap(origin_image, 70,
							70, false);
					t.setImage(target);
					break;
				}
			}
		}
	}

	private void animation() {
		Random rand = new Random(System.currentTimeMillis());
		type = rand.nextInt(4);
		frame = 0;
		final Handler handler = new Handler();
		timer = new Timer(false);
		Log.d("current", Long.toString(System.currentTimeMillis()));
		timer.schedule(new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						isAnimated = true;
						invalidate();
					}
				});
			}
		}, 0, 1);
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

		public void setImage(Bitmap image) {
			this.image = image;
		}

	}

	/**
	 * ダメージ計算
	 */
	private int calcDamage() {
		int damage = (int) Math.pow(CharacterManager.getPlayerAtk(),
				totalDeleted);
		damage /= (int) Math.pow(10, totalDeleted - 1);
		return (int) (damage * CharacterManager.getRate());
	}
}
