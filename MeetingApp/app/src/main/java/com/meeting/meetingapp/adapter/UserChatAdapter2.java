package com.meeting.meetingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meeting.meetingapp.ChatHelper.ChatHelper;
import com.meeting.meetingapp.ChatHelper.ExtraIntent;
import com.meeting.meetingapp.CheckConflicts;
import com.meeting.meetingapp.GraphTest;
import com.meeting.meetingapp.MainActivity;
import com.meeting.meetingapp.R;
import com.meeting.meetingapp.User;

import java.util.ArrayList;
import java.util.List;



public class UserChatAdapter2 extends RecyclerView.Adapter<UserChatAdapter2.ViewHolderUsers>{


    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    private List<User> mUsers;
    private Context mContext;
    private String mCurrentUserEmail;
    private Long mCurrentUserCreatedAt;
    private String mCurrentUserId;
    ArrayList<String> selectedUsers = new ArrayList<String>();

    public UserChatAdapter2(Context context, List<User> fireChatUsers) {
        mUsers = fireChatUsers;
        mContext = context;
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderUsers(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.final_meeting_user_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderUsers holder, int position) {

        final User fireChatUser = mUsers.get(position);

        // Set avatar
        int userAvatarId= ChatHelper.getDrawableAvatarId(fireChatUser.getAvatarId());
        Drawable avatarDrawable = ContextCompat.getDrawable(mContext,userAvatarId);
        holder.getUserAvatar().setImageDrawable(avatarDrawable);

        // Set display name
        holder.getUserDisplayName().setText(fireChatUser.getDisplayName());

        // Set presence status
        holder.getStatusConnection().setText(fireChatUser.getConnection());

        // Set presence text color
        if(fireChatUser.getConnection().equals(ONLINE)) {
            // Green color
            holder.getStatusConnection().setTextColor(Color.parseColor("#00FF00"));
        }else {
            // Red color
            holder.getStatusConnection().setTextColor(Color.parseColor("#FF0000"));
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void refill(User users) {
        mUsers.add(users);
        notifyDataSetChanged();
    }

    public void changeUser(int index, User user) {
        mUsers.set(index,user);
        notifyDataSetChanged();
    }

    public void setCurrentUserInfo(String userUid, String email, long createdAt) {
        mCurrentUserId = userUid;
        mCurrentUserEmail = email;
        mCurrentUserCreatedAt = createdAt;
    }

    public void clear() {
        mUsers.clear();
    }

    /* ViewHolder for RecyclerView */
    public class ViewHolderUsers extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mUserAvatar;
        private TextView mUserDisplayName;
        private TextView mStatusConnection;
        private Context mContextViewHolder;

        public ViewHolderUsers(Context context, View itemView) {
            super(itemView);
            mUserAvatar = (ImageView)itemView.findViewById(R.id.img_avatar_final);
            mUserDisplayName = (TextView)itemView.findViewById(R.id.display_name_final);
            mStatusConnection = (TextView)itemView.findViewById(R.id.text_view_connection_status);
            mContextViewHolder = context;

            itemView.setOnClickListener(this);
        }

        public ImageView getUserAvatar() {
            return mUserAvatar;
        }
        public TextView getUserDisplayName() {
            return mUserDisplayName;
        }
        public TextView getStatusConnection() {
            return mStatusConnection;
        }

        @Override
        public void onClick(View view) {

            Intent chatIntent = new Intent(mContextViewHolder, GraphTest.class);


            // Start new activity
            mContextViewHolder.startActivity(chatIntent);

        }
    }
}