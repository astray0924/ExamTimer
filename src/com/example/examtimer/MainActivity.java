package com.example.examtimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private TextView tvTime;
	private Timer timer;
	private Button btnStart;
	private Button btnReset;

	// attributes
	private int INIT_MILLISECONDS = 600000;
	
	// events
	private boolean firstAlarm;
	private boolean secondAlarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// UI 요소 초기화
		initUI();

		// 타이버 초기화
		timer = new Timer(INIT_MILLISECONDS, 1000);
		setTimerText(INIT_MILLISECONDS);
	}

	private void initUI() {
		tvTime = (TextView) findViewById(R.id.time);
		btnStart = (Button) findViewById(R.id.start);
		btnReset = (Button) findViewById(R.id.reset);

		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);
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
			Toast.makeText(MainActivity.this, "Finished!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			setTimerText(millisUntilFinished);
			
			// 이제 여기서 8분 체크
		}

	}

	private int milliToMinute(long milliseconds) {
		return (int) ((milliseconds / (1000 * 60)) % 60);
	}

	private int milliToSec(long milliseconds) {
		return (int) (milliseconds / 1000) % 60;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.start:
			timer.start();
			break;
		case R.id.reset:
			if (timer != null) {				
				timer.cancel();
			}
			
			timer = new Timer(INIT_MILLISECONDS, 1000);
			setTimerText(INIT_MILLISECONDS);
			break;
		}

	}

	private void setTimerText(long milliseconds) {
		int minutes = milliToMinute(milliseconds);
		int seconds = milliToSec(milliseconds);

		String timeString = String.format("%02d:%02d", minutes, seconds);
		tvTime.setText(timeString);
	}
}
