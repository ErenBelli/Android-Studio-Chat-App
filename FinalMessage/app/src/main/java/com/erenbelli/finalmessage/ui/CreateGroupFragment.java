package com.erenbelli.finalmessage.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.erenbelli.finalmessage.GroupModel;
import com.erenbelli.finalmessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CreateGroupFragment extends Fragment {


    EditText grupName, exp;
    ImageView grupLogo;
    Button createGroupBtn;
    RecyclerView rw;
    Uri fPath;

    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    FirebaseFirestore mStore;


    ArrayList<GroupModel> groupModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        grupName = view.findViewById(R.id.editTextGroupName);
        exp = view.findViewById(R.id.expBlank);
        grupLogo = view.findViewById(R.id.imageGroupLogo);
        rw = view.findViewById(R.id.createGroupItems);
        createGroupBtn = view.findViewById(R.id.createGroupBtn);

        groupModelArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStore = FirebaseFirestore.getInstance();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == getActivity().RESULT_OK){
                        Intent data = result.getData();
                        fPath = data.getData();
                        grupLogo.setImageURI(fPath);
                    }
                }
        );

        grupLogo.setOnClickListener(v ->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        createGroupBtn.setOnClickListener(v ->{
            String grupAd = String.valueOf(grupName.getText());
            String explanations = String.valueOf(exp.getText());

            if(grupAd.isEmpty()){
                Toast.makeText(getContext(), "Grup ismi boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }

            if(explanations.isEmpty()){
                Toast.makeText(getContext(), "Grup açıklaması boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }

            if(fPath != null){
                StorageReference storageReference = mStorage.getReference().child("images/" + UUID.randomUUID().toString());

                storageReference.putFile(fPath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri ->{
                        String downloadUrl = uri.toString();
                        Toast.makeText(getContext(), "Resim oluşturuldu", Toast.LENGTH_SHORT).show();
                        CreateGroup(grupAd, explanations, downloadUrl);
                    });
                });


                return;
            }
            else{
                CreateGroup(grupAd, explanations, null);
            }

        });






        FetchGroups();
        return view;
    }

    private void CreateGroup(String name, String explanation, String img){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/user/" + userId + "/" + "groups").add(new HashMap<String, Object>(){{
            put("name", name);
            put("explanation", explanation);
            put("img", img);
            put("numbers", new ArrayList<String>());
        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Grup başarılı bir şekilde oluşturuldu", Toast.LENGTH_SHORT).show();
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel(name, explanation, img, (List<String>) documentSnapshot.get("numbers"), documentSnapshot.getId());
                rw.getAdapter().notifyItemInserted(groupModelArrayList.size() -1);
            });


        }).addOnFailureListener(e ->{
            Toast.makeText(getContext(), "Grup oluşturulurken hata oluştu", Toast.LENGTH_SHORT).show();
        });


    }

    private void FetchGroups(){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/user/" + userId + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelArrayList.clear();

            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
               GroupModel groupModel = new GroupModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("img"), (List<String>)documentSnapshot.get("numbers"), documentSnapshot.getId());
               groupModelArrayList.add(groupModel);


            }

            rw.setAdapter(new GroupCreateAdapter(groupModelArrayList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rw.setLayoutManager(linearLayoutManager);

        });
    }

}