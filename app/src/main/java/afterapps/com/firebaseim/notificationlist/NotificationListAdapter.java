package afterapps.com.firebaseim.notificationlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import afterapps.com.firebaseim.R;
import afterapps.com.firebaseim.beans.Message;


/**
 * Created by kshyo on 2018/02/28.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private final String mId;
    private List<Message> mDataSet;

    NotificationListAdapter(List<Message> dataset, String id){
        mDataSet = dataset;
        mId = id;
    }
    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;


            v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_listitem, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificationListAdapter.ViewHolder holder, int position) {
        Message notification = mDataSet.get(position);
        holder.mTextView.setText(notification.getBody());
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;

        ViewHolder(View v){
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.rvtextview);
        }
    }
}

