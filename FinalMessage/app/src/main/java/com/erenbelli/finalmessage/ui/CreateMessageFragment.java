package com.erenbelli.finalmessage.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.erenbelli.finalmessage.GroupModel;
import com.erenbelli.finalmessage.MessageModel;
import com.erenbelli.finalmessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateMessageFragment extends Fragment {


    EditText msgName, msg;
    Button createMsg;
    RecyclerView rwMessages;
    ArrayList<MessageModel> messageModelList;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        msgName = view.findViewById(R.id.messageName);
        msg = view.findViewById(R.id.msgBlank);
        createMsg = view.findViewById(R.id.createMessageBtn);
        rwMessages = view.findViewById(R.id.messageItems);
        messageModelList = new ArrayList<>();


        createMsg.setOnClickListener(v -> {
            String messageName = String.valueOf(msgName.getText());
            String messageExplanation = String.valueOf(msg.getText());

            if (messageName.isEmpty() || messageExplanation.isEmpty()) {
                Toast.makeText(getContext(), "Mesaj Adı kısmı ve Mesaj boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }
            CreateMessage(messageName, messageExplanation);
        });

        FetchMessages();
        return view;
    }


    private void CreateMessage(String messageName, String messageExplanation) {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/user/" + userId + "/messages").add(new HashMap<String, String>() {{
            put("name", messageName);
            put("explanation", messageExplanation);
        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Mesaj başarılı bir şekilde oluşturuldu", Toast.LENGTH_SHORT).show();
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                MessageModel messageModel = new MessageModel(messageName, messageExplanation, documentSnapshot.getId());
                messageModelList.add(messageModel);
                rwMessages.getAdapter().notifyItemInserted(messageModelList.size() -1);
            });


        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Mesaj oluşturulurken hata oluştu", Toast.LENGTH_SHORT).show();


        });
    }

    private void FetchMessages(){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/user/" + userId + "/messages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            messageModelList.clear();

            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                MessageModel messageModel = new MessageModel(documentSnapshot.getString("name"), documentSnapshot.getString("explanation"), documentSnapshot.getId());
                messageModelList.add(messageModel);

            }
            rwMessages.setAdapter(new MessageAdapter(messageModelList));

            rwMessages.setAdapter(new MessageAdapter(messageModelList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rwMessages.setLayoutManager(linearLayoutManager);

        })
                .addOnFailureListener(e ->{
                    Toast.makeText(getContext(), "Mesaj çekilirken bir problem oluştu.", Toast.LENGTH_SHORT).show();
                });
    }


}