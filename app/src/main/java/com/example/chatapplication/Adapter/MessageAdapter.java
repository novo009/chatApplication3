package com.example.chatapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.MessageActivity;
import com.example.chatapplication.Model.Chats;
import com.example.chatapplication.Model.Users;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
 public static final int MSG_TYPE_LEFT=0;
 public static final int MSG_TYPE_RIGHT=1;
    private Context mContext;
    private List<Chats> mChats;
    private String imageurl;
    FirebaseUser firebaseUser;


    public MessageAdapter(Context mContext,List<Chats> mChats,String imageurl){
        this.mContext=mContext;
        this.mChats=mChats;
        this.imageurl=imageurl;


    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            show_message=itemView.findViewById(R.id.show_messages);
            profile_image=itemView.findViewById(R.id.profile_image);

        }


    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType==MSG_TYPE_RIGHT){

           View view= LayoutInflater.from(mContext).inflate(R.layout.chat_itemright,parent,false);
           return new MessageAdapter.ViewHolder(view);

       }else{

           View view= LayoutInflater.from(mContext).inflate(R.layout.chat_itemleft,parent,false);
           return new MessageAdapter.ViewHolder(view);

       }
}

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
            Chats chat=mChats.get(position);
            holder.show_message.setText(chat.getMessage());

            if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
            }

    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }


}
