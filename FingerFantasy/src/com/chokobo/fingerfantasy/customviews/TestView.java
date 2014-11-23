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
	private Timer timer;
	public int aniCount = 0;
	/** アニメーション用の画像IDリスト */
	private int[] resources = { R.drawable.wind_001, R.drawable.wind_002,
			R.drawable.wind_003, R.drawable.wind_004, R.drawable.wind_005,
			R.drawable.wind_006, R.drawable.wind_007, R.drawable.wind_008,
			R.drawable.wind_009, R.drawable.wind_010, R.drawable.wind_011,
			R.drawable.wind_012, R.drawable.wind_013, R.drawable.wind_014,
			R.drawable.wind_015, R.drawable.wind_016, R.drawable.wind_017,
			R.drawable.wind_018, R.drawable.wind_019, R.drawable.wind_020,
			R.drawable.wind_021, R.drawable.wind_022, R.drawable.wind_023,
			R.drawable.wind_024, R.drawable.wind_025, R.drawable.wind_026,
			R.drawable.wind_027, R.drawable.wind_028, R.drawable.wind_029,
			R.drawable.wind_030, R.drawable.wind_031, R.drawable.wind_032,
			R.drawable.wind_033, R.drawable.wind_034, R.drawable.wind_035,
			R.drawable.wind_036, R.drawable.wind_037, R.drawable.wind_038,
			R.drawable.wind_039, R.drawable.wind_040, R.drawable.wind_041,
			R.drawable.wind_042, R.drawable.wind_043, R.drawable.wind_044,
			R.drawable.wind_045, R.drawable.wind_046, R.drawable.wind_047,
			R.drawable.wind_048, R.drawable.wind_049, R.drawable.wind_050,
			R.drawable.wind_051, R.drawable.wind_052, R.drawable.wind_053,
			R.drawable.wind_054, R.drawable.wind_055, R.drawable.wind_056,
			R.drawable.wind_057, R.drawable.wind_058, R.drawable.wind_059,
			R.drawable.wind_060, R.drawable.wind_061, R.drawable.wind_062,
			R.drawable.wind_063, R.drawable.wind_064, R.drawable.wind_065,
			R.drawable.wind_066, R.drawable.wind_067, R.drawable.wind_068,
			R.drawable.wind_069, R.drawable.wind_070, R.drawable.wind_071,
			R.drawable.wind_072, R.drawable.wind_073, R.drawable.wind_074,
			R.drawable.wind_075, R.drawable.wind_076, R.drawable.wind_077,
			R.drawable.wind_078, R.drawable.wind_079, R.drawable.wind_080,
			R.drawable.wind_081, };
	List<Bitmap> bitmapResources;

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
		if (isAnimated && frame < resources.length) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					resources[frame]);
			canvas.drawBitmap(bm, 0, 0, paint);
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
			checkCrossing();
			checkInside();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			path.reset();
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
		// pointList.clear();
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
					//animation();
					targets.remove(t);
					i--;
					pointList.clear();
					totalDamage += CharacterManager.getPlayerAtk();
					break;
				}
			}
		}
		polygons.clear();
	}

	private void animation() {
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

	}
}
