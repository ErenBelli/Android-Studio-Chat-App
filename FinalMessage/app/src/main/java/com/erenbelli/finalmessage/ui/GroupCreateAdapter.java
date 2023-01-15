package com.erenbelli.finalmessage.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erenbelli.finalmessage.GroupModel;
import com.erenbelli.finalmessage.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupCreateAdapter extends RecyclerView.Adapter<GroupCreateAdapter.GroupViewHolder> {

    List<GroupModel> groupModelList;
    public GroupCreateAdapter(List<GroupModel> groupModelList){
        this.groupModelList = groupModelList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_group, parent, false));
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel groupModel = groupModelList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewLogoGroup;
        TextView groupName, groupExp;
        public GroupViewHolder (View itemView){
            super(itemView);
            imageViewLogoGroup = itemView.findViewById(R.id.imageViewGroupLogoItem);
            groupName = itemView.findViewById(R.id.textViewGroupNameItem);
            groupExp = itemView.findViewById(R.id.textViewGroupExpItem);
        }

        public void setData(GroupModel groupModel){
            groupName.setText(groupModel.getName());
            groupExp.setText(groupModel.getName());


            if(groupModel.getImg() != null){
                Picasso.get().load(groupModel.getImg()).into(imageViewLogoGroup);
            }
        }
    }


}
