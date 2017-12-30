package ex.view.yky.a1to50;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class rankActivity extends Activity {

    //파이어베이스의 실시간 디비를 사용하기 위해 인스턴스를 가져옴
    //users 라는 키를 가진 값들을 참조
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    private ArrayList<String> list = new ArrayList<>();
    private int count = 0; //순서 역할
    /*private Button printBtn = (Button)findViewById(R.id.print);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        //오름차순 정렬 데이터 , 상위10개까지만 불러옴
        databaseReference.orderByChild("record").limitToFirst(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                count++;
                User user = dataSnapshot.getValue(User.class);
                list.add(String.valueOf(count)+". "+ String.valueOf(user.username) +" "+String.valueOf(user.record));
               // Toast.makeText(getApplicationContext() ," 기록 " + user.record + " 이름 : " +user.username,  Toast.LENGTH_SHORT ).show();
               // System.out.println(dataSnapshot.getKey() + " 기록 " + user.record + " 이름 : " +user.username);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 다량의 데이터를 화면에 표현하는 방법

        //     1. 다량의 데이터를 준비 (배열, xml, ArrayList 객체)
        //    2. Adapter 정의 : 데이터와 AdapterView의 연결관계를 정의
        //    3. AdapterView 선택 (ListView, Spinner, Gallery, GridView..)
        //        Adapter 를 등록 (setAdapter() 메서드)

        // ArrayAdapter adapter = new ArrayAdapter(
        // getApplicationContext(), // 현재 화면의 제어권자
        // android.R.layout.simple_list_item_1,//한행마다 보여줄 레이아웃을 지정
        // data);                                // 다량의 데이터

        final ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.myrow,  //한행마다 보여줄 레이아웃을 지정
                list); // 다량의 데이터

        ListView lv = findViewById(R.id.listview1);
        lv.setAdapter(adapter);  // 리스트 뷰에 adapter 를 등록한다

        Handler mh = new Handler();
        mh.postDelayed(new Runnable() {
            @Override
            public void run() {
                //리스트뷰갱신
                adapter.notifyDataSetChanged();
            }
        }, 2000);

    }

}