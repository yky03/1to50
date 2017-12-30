package ex.view.yky.a1to50;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ex.view.yky.a1to50.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startBtn = findViewById(R.id.startBtn); //시작버튼
        Button rankBtn = findViewById(R.id.rankBtn); //랭킹
        Button quitBtn = findViewById(R.id.quitBtn); //종료

        //시작
        startBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext() , playActivity.class);
                startActivity(intent);
            }
        });

        //랭킹
        rankBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext() , rankActivity.class);
                startActivity(intent);
            }
        });

        //종료
        quitBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //순서대로 호출
                moveTaskToBack(true); //현재어플을 백그라운드로 넘김
                finish(); //현재 액티비티 종료
                android.os.Process.killProcess(android.os.Process.myPid()); //현재의 프로세스 및 서비스 종료
            }
        });


    }



}
