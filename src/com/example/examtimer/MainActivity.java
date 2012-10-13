package com.example.examtimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private Timer timer;
	private TextView tvTime;
	private CheckBox chkExtraTime;
	private Button btnStart;
	private Button btnReset;

	// attributes
	private static final int DEFAULT_TIME = 600000;
	private static final int EXTRA_TIME = 660000;
	private boolean isExtraMode = false;
	private boolean isRunning = false;

	// Timer modes
	private enum TimerMode {
		DEFAULT, EXTRA
	};

	private void setTimerMode(TimerMode mode) {
		switch (mode) {
		case DEFAULT:
			timer = new Timer(DEFAULT_TIME, 1000);
			setTimerText(DEFAULT_TIME);
			break;
		case EXTRA:
			timer = new Timer(EXTRA_TIME, 1000);
			setTimerText(EXTRA_TIME);
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// UI 요소 초기화
		initUI();

		// 타이버 초기화
		setTimerMode(TimerMode.DEFAULT);
	}

	private void initUI() {
		tvTime = (TextView) findViewById(R.id.time);
		chkExtraTime = (CheckBox) findViewById(R.id.extra_time);
		btnStart = (Button) findViewById(R.id.start);
		btnReset = (Button) findViewById(R.id.reset);

		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		chkExtraTime.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class Timer extends CountDownTimer {

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onFinish() {
			Toast.makeText(MainActivity.this, "Finished!", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			setTimerText(millisUntilFinished);

			// 이제 여기서 8분 체크
			/**
			 * 시험지 읽는시간 1분 (선택) 중간 알림 (8/9분째) 마지막 알림 (10/11분째)
			 */
		}

	}

	private int milliToMinute(long milliseconds) {
		return (int) ((milliseconds / (1000 * 60)) % 60);
	}

	private int milliToSec(long milliseconds) {
		return (int) (milliseconds / 1000) % 60;
	}
	
	private void setExtraCheckBoxEnable(boolean enable) {
		chkExtraTime.setEnabled(enable);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.start:
			// 설정된 모드에 따라서 타이머 초기화
			if (isExtraMode) {
				setTimerMode(TimerMode.EXTRA);
			} else {
				setTimerMode(TimerMode.DEFAULT);
			}

			timer.start();

			// toggle the buttons' visibility
			btnStart.setVisibility(View.GONE);
			btnReset.setVisibility(View.VISIBLE);
			
			// 상태 변화 반영
			isRunning = true;
			setExtraCheckBoxEnable(false);
			break;
		case R.id.reset:
			if (timer != null) {
				timer.cancel();
			}

			// 선택된 모드에 따라서 타이머 초기화
			if (isExtraMode) {
				setTimerMode(TimerMode.EXTRA);
			} else {
				setTimerMode(TimerMode.DEFAULT);
			}
			
			// toggle the buttons' visibility
			btnStart.setVisibility(View.VISIBLE);
			btnReset.setVisibility(View.GONE);
			
			isRunning = false;
			setExtraCheckBoxEnable(true);
			break;
		}

	}

	private void setTimerText(long milliseconds) {
		int minutes = milliToMinute(milliseconds);
		int seconds = milliToSec(milliseconds);
		String timeString = String.format("%02d:%02d", minutes, seconds);
		tvTime.setText(timeString);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isExtraMode = isChecked;
		
		if (isExtraMode) {
			setTimerMode(TimerMode.EXTRA);
		} else {
			setTimerMode(TimerMode.DEFAULT);
		}
	}

}
