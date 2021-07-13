package com.example.cookbook.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {

    private EditText recipeName,chefName,recipeType,recipeDuration,countryName,recipeIngredients,recipeDirections;
    private Button submitRecipe;
    private ImageView recipeImageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private NavController navController;
    String rName,cName,rType,rDuration,country,rIngredients,rDirections;
    private static final int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private int upload_count = 0;
    //String URL;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add Recipe");

        recipeImageView = view.findViewById(R.id.addRecipeImageView);

        recipeName = view.findViewById(R.id.enterRecipeName);
        chefName = view.findViewById(R.id.enterChefName);
        recipeType = view.findViewById(R.id.enterRecipeType);
        recipeDuration = view.findViewById(R.id.enterDuration);
        countryName = view.findViewById(R.id.enterCountry);
        recipeIngredients = view.findViewById(R.id.enterIngredients);
        recipeDirections = view.findViewById(R.id.enterDirection);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        navController = Navigation.findNavController(getActivity(),R.id.Host_Fragment2);


        submitRecipe = view.findViewById(R.id.submitRecipeBtn);

        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (recipeName.getText().toString().trim().length()==0 || chefName.getText().toString().trim().length()==0 || recipeType.getText().toString().trim().length()==0 || recipeDuration.getText().toString().trim().length()==0 || countryName.getText().toString().trim().length()==0 || recipeIngredients.getText().toString().trim().length()==0 || recipeDirections.getText().toString().trim().length()==0 )
                {
                    Toast toast = Toast.makeText(getActivity(),"Enter All Details",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    rName = recipeName.getText().toString().trim();
                    cName = chefName.getText().toString().trim();
                    rType = recipeType.getText().toString().trim();
                    rDuration = recipeDuration.getText().toString().trim();
                    country = countryName.getText().toString().trim();
                    rIngredients = recipeIngredients.getText().toString().trim();
                    rDirections = recipeDirections.getText().toString().trim();

                    AddRecipe();


                }
            }
        });


        recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent, PICK_IMAGE);


            }
        });

    }


    private void AddRecipe()
    {


        //Upload Image

        StorageReference storageRef=FirebaseStorage.getInstance().getReference().child("Recipe Images/");
        storageRef.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri) {
                            String URL = uri.toString();
                            //This is your image url do whatever you want with it.

                            uploadData(URL);
//                            DatabaseReference recipeList = reference.child(firebaseAuth.getUid())
//                                    .child("Recipe Image Link")
//                                    .child(rName).push();


//                            RecipeInfo obj = new RecipeInfo();
//                            obj.setRecipeImageLink(URL);
//                            recipeList.setValue(obj);

                        }
                    });
                }
            }
        });


    }

    private void uploadData(String url) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("User Recipe Details");

        DatabaseReference recipeListBranch = reference.child(firebaseAuth.getUid()).child(rName).push();

        RecipeInfo recipeInfo = new RecipeInfo(rName,cName,rType,rDuration,country,rIngredients,rDirections,url);

        recipeInfo.setRecipeName(rName);
        recipeInfo.setChefName(cName);
        recipeInfo.setRecipeType(rType);
        recipeInfo.setRecipeDuration(rDuration);
        recipeInfo.setCountryName(country);
        recipeInfo.setRecipeIngredients(rIngredients);
        recipeInfo.setRecipeDirections(rDirections);
        recipeInfo.setRecipeImageLink(url);

        recipeListBranch.setValue(recipeInfo);



        Toast.makeText(getActivity(), "Recipe Added", Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.discoverFragment);


    }

//    private void StoreLink(String url) {
//
//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("ImgLink",url);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                .child("User Recipe Details")
//                .child(firebaseAuth.getUid())
//                .child(rName)
//                .child("Image Link")
//                .push();
//
//        reference.setValue(hashMap);
//
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {

            ImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),ImageUri);
                recipeImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PICK_IMAGE)
//        {
//            if (resultCode == RESULT_OK)
//            {
//                if (data.getClipData() != null)
//                {
//
//                    int countClipData = data.getClipData().getItemCount();
//                    int currentImageSelect = 0;
//
//                    while(currentImageSelect < countClipData)
//                    {
//
//                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
//                        ImageList.add(ImageUri);
//
//                        currentImageSelect = currentImageSelect + 1;
//                    }
//
//                }
//                else
//                {
//                    Toast.makeText(getActivity(), "Please select multiple image", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }
}