package ex.view.yky.a1to50;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class playActivity extends Activity {

    private int checkNum = 1; //초기값 1부터 비교
    private TextView mCountDown;
    private int count = 3; //카운트값
    private int ranNum[] = new int[50]; //50까지
    private Button  btn[] = new Button[25];
    private Integer[] numBtn = {R.id.btn1 , R.id.btn2 , R.id.btn3 , R.id.btn4 , R.id.btn5 , R.id.btn6 , R.id.btn7
            , R.id.btn8 , R.id.btn9 , R.id.btn10 , R.id.btn11 , R.id.btn12 , R.id.btn13 , R.id.btn14 , R.id.btn15 , R.id.btn16
            , R.id.btn17 , R.id.btn18 , R.id.btn19 , R.id.btn20 , R.id.btn21 , R.id.btn22 , R.id.btn23 , R.id.btn24 , R.id.btn25};
    private Chronometer chronometer;
    private final SoundPool sp = new SoundPool(1,         // 최대 음악파일의 개수
            AudioManager.STREAM_MUSIC, // 스트림 타입
            0);        // 음질 - 기본값:0
    private int soundID3, soundID4;
    //파이어베이스의 실시간 디비를 사용하기 위해 인스턴스를 가져옴
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String record;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.setContentView(R.layout.activity_play); //첫번째에 메인을 깔음

        //구글애드몹광고삽입
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //SDK초기화
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6080511305583538/7767032394");

        // 각각의 재생하고자하는 음악을 미리 준비한다
        final int soundID = sp.load(this, // 현재 화면의 제어권자
                R.raw.yes,    // 음악 파일
                1);        // 우선순위
        final int soundID2 = sp.load(this, // 현재 화면의 제어권자
                R.raw.no,    // 음악 파일
                1);        // 우선순위

        soundID3 = sp.load(this, // 현재 화면의 제어권자
                R.raw.count,    // 음악 파일
                1);        // 우선순위
        soundID4 = sp.load(this, // 현재 화면의 제어권자
                R.raw.start,    // 음악 파일
                1);        // 우선순위


        for(int i=0; i<ranNum.length; i++){
            ranNum[i] = i+1; //1~50 (세팅)
        }

        //배열 num의 임의의 요소 2개를 골라서 위치를 바꾼다. 70번 반복(셔플) 숫자높일수록 많이 섞임
        //배열의 인덱스에서 랜덤으로 두개 뽑아 값을 교체해줌
        for(int x=0; x<70; x++) {
            int i = (int)(Math.random() * 25); //25까지의 숫자만 셔플(0~24)
            int j = (int)(Math.random() * 25); //25까지의 숫자만 셔플(0~24)
            int tmp = 0; //임시변수

            tmp = ranNum[i];
            ranNum[i] = ranNum[j];
            ranNum[j] = tmp;
        }

        chronometer = findViewById(R.id.chronometer_view);

        OnClickListener clickListener  = new OnClickListener(){
            @Override
            public void onClick(View v){
                int clickNum = Integer.parseInt( String.valueOf(((Button)v).getText()) );
                    String result = checkValue(v , checkNum , clickNum );
                    if(result.equals("success")){
                        //맞는버튼 눌렀을때
                        //Toast.makeText(getApplicationContext() , "Yes!" , Toast.LENGTH_SHORT).show();
                        sp.play(soundID, // 준비한 soundID
                                1,         // 왼쪽 볼륨 float 0.0(작은소리)~1.0(큰소리)
                                1,         // 오른쪽볼륨 float
                                0,         // 우선순위 int
                                0,     // 반복회수 int -1:무한반복, 0:반복안함
                                1.2f);    // 재생속도 float 0.5(절반속도)~2.0(2배속)
                        // 음악 재생
                            if(checkNum <= 25) {
                                ((Button) v).setText(String.valueOf(ranNum[checkNum + 24]));
                            } else{
                                v.setEnabled(false); //비활성화
                                v.setVisibility(View.INVISIBLE); //버튼 안보여줌
                            }
                            if(checkNum == 50){
                                chronometer.stop();
                                record = String.valueOf(chronometer.getText());
                                showMessage();
                                /*Toast.makeText(getApplicationContext() ,
                                        "FINISH! 기록 : "+chronometer.getText() , Toast.LENGTH_LONG).show();*/
                                return;
                            }
                        checkNum++;
                    } else{
                        //틀린버튼 눌렀을때
                        sp.play(soundID2, // 준비한 soundID
                                1,         // 왼쪽 볼륨 float 0.0(작은소리)~1.0(큰소리)
                                1,         // 오른쪽볼륨 float
                                0,         // 우선순위 int
                                0,     // 반복회수 int -1:무한반복, 0:반복안함
                                1.2f);    // 재생속도 float 0.5(절반속도)~2.0(2배속)
                        // 음악 재생
                        //Toast.makeText(getApplicationContext() , "NO!" , Toast.LENGTH_SHORT).show();
                    }
            }
        };


        for(int i=0; i<btn.length; i++){
            btn[i] = findViewById(numBtn[i]); //버튼 고유값 세팅
            btn[i].setEnabled(false); //시작전 버튼 비활성화
            btn[i].setOnClickListener(clickListener); //버튼에 리스너 장착
        }

        //그다음 인플레이션으로 겹치는 레이아웃을 깐다
        //인플레이터 객체를 이용하여 해당 레이아웃에 다른xml을 띄울수 있다.(카운트다운에 이용)
        LayoutInflater inflater = (LayoutInflater)getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.over, null);

        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
                linear.setGravity(Gravity.CENTER);
        win.addContentView(linear, paramlinear);//이 부분이 레이아웃을 겹치는 부분
        //add는 기존의 레이아웃에 겹쳐서 배치하라는 뜻이다.

        mCountDown = findViewById(R.id.tv_countdown);
        mCountDown.setText(String.valueOf(count));
        /*sp.play(soundID3, // 준비한 soundID
                1,         // 왼쪽 볼륨 float 0.0(작은소리)~1.0(큰소리)
                1,         // 오른쪽볼륨 float
                0,         // 우선순위 int
                0,     // 반복회수 int -1:무한반복, 0:반복안함
                0.5f);    // 재생속도 float 0.5(절반속도)~2.0(2배속)*/
        autoCountHandler.postDelayed(autoCountRunnable,1000);
    }

    private Handler autoCountHandler = new Handler(); //핸들러 - 다른 객체들이 보낸 메시지를 받고 처리함
    private Runnable autoCountRunnable = new Runnable() { //쓰레드 - 핸들러에게 메시지를 보내 동적뷰, 병행처리가능.
        public void run() {
            count--;
            if(count !=0) {
                mCountDown.setText(String.valueOf(count));
                sp.play(soundID3, // 준비한 soundID
                        1,         // 왼쪽 볼륨 float 0.0(작은소리)~1.0(큰소리)
                        1,         // 오른쪽볼륨 float
                        0,         // 우선순위 int
                        0,     // 반복회수 int -1:무한반복, 0:반복안함
                        0.5f);    // 재생속도 float 0.5(절반속도)~2.0(2배속)
            }
            if (count > 0) { //3부터 1까지 카운트
                autoCountHandler.postDelayed(autoCountRunnable, 1000);
            } else {
                if (autoCountHandler != null) {
                    //0되면 플레이
                    mCountDown.setText(""); //카운트다운 숫자 화면에서 안보이게
                        for(int i=0; i<btn.length; i++){
                            btn[i].setText(String.valueOf(ranNum[i]));
                            btn[i].setEnabled(true);
                        }
                    sp.play(soundID4, // 준비한 soundID
                            1,         // 왼쪽 볼륨 float 0.0(작은소리)~1.0(큰소리)
                            1,         // 오른쪽볼륨 float
                            0,         // 우선순위 int
                            0,     // 반복회수 int -1:무한반복, 0:반복안함
                            0.5f);    // 재생속도 float 0.5(절반속도)~2.0(2배속)
                    Toast.makeText(getApplicationContext() , "START!" , Toast.LENGTH_SHORT).show();
                    autoCountHandler.removeCallbacks(autoCountRunnable); //카운트 다운 끝

                    //타이머 기록 시작
                    chronometer.setBase(SystemClock.elapsedRealtime()); //스탑후 리셋할때도 사용
                    chronometer.start();

                }
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        if(autoCountHandler != null){
            autoCountHandler.removeCallbacks(autoCountRunnable);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
       // getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }*/

    //터치된 숫자가 다음에 눌러져야 될 버튼값이 맞는지 체크하는 메소드
    public String checkValue(View v , int checkNum , int clickNum){
        if(checkNum == clickNum){
            return "success";
        } else{
            return "fail";
        }
    }

    //다이얼로그 팝업
    public void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("축하합니다! 기록 : "+record);
        //builder.setMessage("");
        final EditText et = new EditText(playActivity.this);
        et.setHint("Insert Your Name");
        builder.setView(et);

        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("등록하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User user = new User(String.valueOf(et.getText()) , record);
                databaseReference.child("users").push().setValue(user);
                Toast.makeText(getApplicationContext() , "랭킹등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext() , rankActivity.class); //랭킹목록으로 이동
                startActivity(intent);
            }
        });
        builder.setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
