package com.ty.landmine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.TextView;
 
/**
 * @author  swufe041
 * @date    2015-4-8
 * @QQ	    84689229
 * @QQGroup 134339070
 */
public class LandMineView extends GridLayout {

	private int lenSide = 6;
	private int landMineCnt = 1;
	private int landMineLeft = 0;
	private int landMineViewWidth, landMineViewHeight;
	public static LandMineView landMineView = null;
	private LandMine[][] landMine = null;
	private int textSize = 28;
	private TextView leftLm, useTime;
	private int timerCnt = 0;
	private Thread timerThread = new Thread(new MyThread());
	private boolean isover = true, startTimer = false;// ��ʱ������
	private int invisable = 0;

	public LandMineView(Context context) {
		super(context);
		timerThread.start();
		initGameView();
	}

	public LandMineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		timerThread.start();
		initGameView();
	}

	public LandMineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		timerThread.start();
		initGameView();
	}

	public void initGameView() {
		removeAllViews();
		landMine = null;
		landMineView = this;
		setColumnCount(lenSide);
		setBackgroundColor(0xffbbaaee);
		landMine = new LandMine[lenSide][lenSide];
		int lmWidth = (Math.min(landMineViewWidth, landMineViewHeight) - 5) / lenSide;
		addLandMine(lmWidth, lmWidth);
		// startGame();
	}

	public void addLandMine(int lmWidth, int lmHeight) {
		LandMine lm;
		for (int y = 0; y < lenSide; y++) {
			for (int x = 0; x < lenSide; x++) {
				lm = new LandMine(getContext());
				landMine[x][y] = lm;
				landMine[x][y].setTvWidth(lmWidth);
				addView(lm, lmWidth, lmHeight);

			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		landMineViewWidth = w;
		landMineViewHeight = h;
	}

	/***
	 * ��ʼ��Ϸ,��������б���ϵ�����
	 */
	public void startGame() {
		startTimer = true;
		landMineLeft = landMineCnt;
		isover = true;
		timerCnt = 0;
		for (int y = 0; y < lenSide; y++) {
			for (int x = 0; x < lenSide; x++) {
				landMine[x][y].setNum(0);
				landMine[x][y].setViewAble(false);
				landMine[x][y].setLandMine(false);
				landMine[x][y].setTextSize(textSize);

				landMine[x][y].set_locationType(getLocationType(x, y));
				landMine[x][y].setLocation(new Point(x, y));
			}
		}
		for (int lm = 0; lm < landMineCnt; lm++) {

			initLandMine();
		}
		if (leftLm != null)
			leftLm.setText(landMineLeft + "");
	}

	/****
	 * ��ʼ�������б�
	 */
	public void initLandMine() {
		int offsetX = getRadomNum(), offsetY = getRadomNum();
		while (landMine[offsetX][offsetY].getShowNum() == 99) {// ����Ѿ��б�ը�Ͳ��������
			offsetX = getRadomNum();
			offsetY = getRadomNum();
		}
		landMine[offsetX][offsetY].setNum(99);
		if (getLocationType(offsetX, offsetY) == 1) {// ���Ͻ�
			setNumPlus(offsetX + 1, offsetY);
			setNumPlus(offsetX + 1, offsetY + 1);
			setNumPlus(offsetX, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 2) {// ���Ͻ�
			setNumPlus(offsetX - 1, offsetY);
			setNumPlus(offsetX - 1, offsetY + 1);
			setNumPlus(offsetX, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 3) {// ���½�
			setNumPlus(offsetX, offsetY - 1);
			setNumPlus(offsetX + 1, offsetY - 1);
			setNumPlus(offsetX + 1, offsetY);
		} else if (getLocationType(offsetX, offsetY) == 4) {// ���½�
			setNumPlus(offsetX - 1, offsetY);
			setNumPlus(offsetX - 1, offsetY - 1);
			setNumPlus(offsetX, offsetY - 1);
		} else if (getLocationType(offsetX, offsetY) == 5) {// ��ߵ�һ��
			setNumPlus(offsetX, offsetY - 1);
			setNumPlus(offsetX + 1, offsetY - 1);
			setNumPlus(offsetX + 1, offsetY);
			setNumPlus(offsetX, offsetY + 1);
			setNumPlus(offsetX + 1, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 6) {// ���һ��
			setNumPlus(offsetX - 1, offsetY - 1);
			setNumPlus(offsetX, offsetY - 1);
			setNumPlus(offsetX - 1, offsetY);
			setNumPlus(offsetX - 1, offsetY + 1);
			setNumPlus(offsetX, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 7) {// ��һ��
			setNumPlus(offsetX - 1, offsetY);
			setNumPlus(offsetX - 1, offsetY + 1);
			setNumPlus(offsetX, offsetY + 1);
			setNumPlus(offsetX + 1, offsetY + 1);
			setNumPlus(offsetX + 1, offsetY);
		} else if (getLocationType(offsetX, offsetY) == 8) {// ���һ��
			setNumPlus(offsetX - 1, offsetY - 1);
			setNumPlus(offsetX - 1, offsetY);
			setNumPlus(offsetX, offsetY - 1);
			setNumPlus(offsetX + 1, offsetY - 1);
			setNumPlus(offsetX + 1, offsetY);
		} else {
			// ����
			setNumPlus(offsetX - 1, offsetY - 1);
			// ��
			setNumPlus(offsetX, offsetY - 1);
			// ����
			setNumPlus(offsetX + 1, offsetY - 1);
			// ��
			setNumPlus(offsetX - 1, offsetY);
			// ��
			setNumPlus(offsetX + 1, offsetY);
			// ����
			setNumPlus(offsetX - 1, offsetY + 1);
			// ��
			setNumPlus(offsetX, offsetY + 1);
			// ����
			setNumPlus(offsetX + 1, offsetY + 1);
		}

	}

	/***
	 * �ж��Ƿ���Ҫ��TextView�ϵ�ֵ��һ
	 * 
	 * @param offsetX
	 * @param offsetY
	 */
	public void setNumPlus(int offsetX, int offsetY) {
		if (landMine[offsetX][offsetY].getShowNum() != 99) {
			landMine[offsetX][offsetY].setNum(landMine[offsetX][offsetY].getShowNum() + 1);
		}
	}

	private int getRadomNum() {
		return (int) (Math.random() * (lenSide - 1));
	}

	/***
	 * ����LandMine.java����
	 * 
	 * @return
	 */
	public static LandMineView getLandMineView() {
		return landMineView;
	}

	/***
	 * �ж��Ƿ�����Ҫ�ݹ�
	 * 
	 * @param offsetX
	 * @param offsetY
	 */
	public void checkBlank(int offsetX, int offsetY) {
		if (!landMine[offsetX][offsetY].getViewAble()) {
			if (landMine[offsetX][offsetY].getShowNum() == 0) {
				landMine[offsetX][offsetY].touchView();
				openBlankByPoit(new Point(offsetX, offsetY));
			} else {
				landMine[offsetX][offsetY].touchView();
			}
		}
	}

	/***
	 * ������з���û������Ҳ���ǵ������丽�������пհ׿���ʾ����
	 * 
	 * @param _location
	 */
	public void openBlankByPoit(Point _location) {
		int offsetX = _location.x;
		int offsetY = _location.y;
		if (getLocationType(offsetX, offsetY) == 1) {// ���Ͻ�
			checkBlank(offsetX + 1, offsetY);
			checkBlank(offsetX + 1, offsetY + 1);
			checkBlank(offsetX, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 2) {// ���Ͻ�
			checkBlank(offsetX - 1, offsetY);
			checkBlank(offsetX - 1, offsetY + 1);
			checkBlank(offsetX, offsetY + 1);

		} else if (getLocationType(offsetX, offsetY) == 3) {// ���½�
			checkBlank(offsetX, offsetY - 1);
			checkBlank(offsetX + 1, offsetY - 1);
			checkBlank(offsetX + 1, offsetY);
		} else if (getLocationType(offsetX, offsetY) == 4) {// ���½�
			checkBlank(offsetX - 1, offsetY);
			checkBlank(offsetX - 1, offsetY - 1);
		} else if (getLocationType(offsetX, offsetY) == 5) {// ��ߵ�һ��
			checkBlank(offsetX, offsetY - 1);
			checkBlank(offsetX + 1, offsetY - 1);
			checkBlank(offsetX + 1, offsetY);
			checkBlank(offsetX, offsetY + 1);
			checkBlank(offsetX + 1, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 6) {// ���һ��
			checkBlank(offsetX - 1, offsetY - 1);
			checkBlank(offsetX, offsetY - 1);
			checkBlank(offsetX - 1, offsetY);
			checkBlank(offsetX - 1, offsetY + 1);
			checkBlank(offsetX, offsetY + 1);
		} else if (getLocationType(offsetX, offsetY) == 7) {// ��һ��
			checkBlank(offsetX - 1, offsetY);
			checkBlank(offsetX - 1, offsetY + 1);
			checkBlank(offsetX, offsetY + 1);
			checkBlank(offsetX + 1, offsetY + 1);
			checkBlank(offsetX + 1, offsetY);
		} else if (getLocationType(offsetX, offsetY) == 8) {// ���һ��
			checkBlank(offsetX - 1, offsetY);
			checkBlank(offsetX - 1, offsetY - 1);
			checkBlank(offsetX, offsetY - 1);
			checkBlank(offsetX + 1, offsetY - 1);
			checkBlank(offsetX + 1, offsetY);
		} else {

			checkBlank(offsetX - 1, offsetY - 1);
			checkBlank(offsetX, offsetY - 1);
			checkBlank(offsetX + 1, offsetY - 1);
			checkBlank(offsetX - 1, offsetY);
			checkBlank(offsetX + 1, offsetY);

			checkBlank(offsetX - 1, offsetY + 1);
			checkBlank(offsetX, offsetY + 1);
			checkBlank(offsetX + 1, offsetY + 1);
		}

	}

	/****
	 * �жϷ����λ��
	 * 
	 * @param offsetX
	 *            x����
	 * @param offsetY
	 *            y����
	 * @return
	 */
	public int getLocationType(int offsetX, int offsetY) {

		if ((offsetX - 1) < 0 && (offsetY - 1) < 0) {// ���Ͻ�

			return 1;
		} else if ((offsetX + 1) >= lenSide && (offsetY - 1) < 0) {// ���Ͻ�

			return 2;
		} else if ((offsetX - 1) < 0 && (offsetY + 1) >= lenSide) {// ���½�

			return 3;
		} else if ((offsetX + 1) >= lenSide && (offsetY + 1) >= lenSide) {// ���½�

			return 4;
		} else if ((offsetX - 1) < 0 && (offsetY - 1) >= 0 && (offsetY + 1) < lenSide) {// ��ߵ�һ��

			return 5;
		} else if ((offsetX + 1) >= lenSide && (offsetY - 1) >= 0 && (offsetY + 1) < lenSide) {// ���һ��

			return 6;
		} else if ((offsetY - 1) < 0 && (offsetX - 1) >= 0 && (offsetX + 1) < lenSide) {// ��һ��

			return 7;
		} else if ((offsetY + 1) >= lenSide && (offsetX - 1) >= 0 && (offsetX + 1) < lenSide) {// ���һ��
			return 8;
		}

		return 9;
	}

	/****
	 * ���е���
	 * 
	 * @param _location
	 *            ��������
	 */
	public void landMainFire(Point _location) {
		isover = false;

		new AlertDialog.Builder(getContext()).setMessage("��ò��е�����Ϸ����").setPositiveButton("���¿�ʼ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame();
			}
		}).setCancelable(false).show();
	}

	public void landMainSuccess() {

		isover = false;
		new AlertDialog.Builder(getContext()).setMessage("���,��ʱ��  " + timerCnt + "  ���ҳ�ȫ��������Ϸ����").setPositiveButton("���¿�ʼ", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame();
			}
		}).setCancelable(false).show();
	}

	/***
	 * ��Ϸ������ʾ���е���
	 */
	public void gameOver() {
		for (int y = 0; y < lenSide; y++) {
			for (int x = 0; x < lenSide; x++) {
				if (landMine[x][y].getShowNum() == 99) {
					landMine[x][y].getAllLandMine();
				}
			}
		}
	}

	public void setLenSide(int lenSide) {
		this.lenSide = lenSide;
	}

	public void setLandMineCnt(int landMineCnt) {
		this.landMineCnt = landMineCnt;
		landMineLeft = landMineCnt;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public void setLeftLm(TextView leftLm) {
		this.leftLm = leftLm;
	}

	public void setUseTime(TextView useTime) {
		this.useTime = useTime;
	}

	public void setLandMineLeft(int landMineLeft) {
		this.landMineLeft = this.landMineLeft + landMineLeft;
		leftLm.setText(this.landMineLeft + "");

	}

	public void setInvisable(int cnt) {
		this.invisable = this.invisable + cnt;
		if (invisable == lenSide * lenSide) {
			landMainSuccess();
		}
	}

	final Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (startTimer) {
					timerCnt++;
					useTime.setText("" + timerCnt);
				}
			}
			super.handleMessage(msg);
		}
	};

	public class MyThread implements Runnable { // thread
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000); // sleep 1000ms
					if (isover) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
				}
			}
		}
	}

}
