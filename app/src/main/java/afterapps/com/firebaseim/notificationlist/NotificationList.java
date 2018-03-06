package afterapps.com.firebaseim.notificationlist;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import afterapps.com.firebaseim.R;
import afterapps.com.firebaseim.beans.Message;
import afterapps.com.firebaseim.beans.User;

/**
 * Created by kshyo on 2018/02/23.
 */

public class NotificationList extends AppCompatActivity {

    private DatabaseReference mRef;
    private String UserUid = null;
    private ArrayList<Message> data;
    private TextView mText;
    private RecyclerView mRecyclerView;
    private String mId;
    private NotificationListAdapter mAdapter;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        // user uid 획득
        Intent intent = getIntent();
        UserUid = intent.getStringExtra("UserUid");
// notification list data
        Log.v("Notification List data ", String.valueOf(data));

       mText = (TextView) findViewById(R.id.rvtextview);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvNotification);
       data = new ArrayList<>();

       mId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       mAdapter = new NotificationListAdapter(data, mId);
       mRecyclerView.setAdapter(mAdapter);
       mRecyclerView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(NotificationList.this, "You touch the item: " + view.getId(), Toast.LENGTH_SHORT).show();

           }
       });

      mRef = FirebaseDatabase.getInstance().getReference().child("notifications").child("messages").child(UserUid);

      mRef.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                  try {
                      Message notification = dataSnapshot.getValue(Message.class);

                      data.add(notification);
                      mRecyclerView.scrollToPosition(data.size() -1);
                      mAdapter.notifyItemChanged(data.size() -1);
                  } catch (Exception ex) {
                      Log.e("error", ex.getMessage());
                  }
              }
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
        Log.v("Notification data", UserUid);



    }
}