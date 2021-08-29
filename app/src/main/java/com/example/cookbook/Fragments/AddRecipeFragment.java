package com.example.cookbook.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.Models.RecipeSearchInfo;
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
import com.yesterselga.countrypicker.CountryPicker;
import com.yesterselga.countrypicker.CountryPickerListener;
import com.yesterselga.countrypicker.Theme;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {

    private EditText recipeName,chefName,recipeDuration,countryName,recipeIngredients,recipeDirections;
    private Button submitRecipe;
    private ImageSwitcher recipeImageView;
    private ImageButton next,previous;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private NavController navController;
    String rName,cName,rType,rDuration,country,rIngredients,rDirections;
    private static final int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private int position = 0;

    String[] types = {"Select","Breakfast", "Brunch", "Lunch", "Dinner", "Snacks", "Appetisers", "Desserts", "Baking", "Drinks", "Other"};
    private Spinner spinner;

    private ProgressDialog progressDialog;


    private int upload_count = 0;
    private int j = 0;

    //Sahil's
    DatabaseReference mref;

    CountryPicker picker;

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

        getActivity().setTitle(R.string.AddRecipe);

        recipeImageView = view.findViewById(R.id.addRecipeImageView);


        spinner = view.findViewById(R.id.enterRecipeTypeSpinner);

        recipeName = view.findViewById(R.id.enterRecipeName);
        chefName = view.findViewById(R.id.enterChefName);
        recipeDuration = view.findViewById(R.id.enterDuration);
        countryName = view.findViewById(R.id.enterCountry);
        recipeIngredients = view.findViewById(R.id.enterIngredients);
        recipeDirections = view.findViewById(R.id.enterDirection);
        next = view.findViewById(R.id.btnNext);
        previous = view.findViewById(R.id.btnPrevious);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        navController = Navigation.findNavController(getActivity(),R.id.Host_Fragment2);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        submitRecipe = view.findViewById(R.id.submitRecipeBtn);

        Animation in = AnimationUtils.loadAnimation(getContext(),android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(),android.R.anim.slide_out_right);

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);


        picker = CountryPicker.newInstance("Select Country", Theme.DARK);  // Set Dialog Title and Theme

        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String var1, String var2, String var3, int var4) {

                countryName.setText(var1);

                picker.dismiss();

            }
        });

        countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);



                if (recipeName.getText().toString().trim().length()==0 || chefName.getText().toString().trim().length()==0 || spinner.getSelectedItemPosition()==0 || recipeDuration.getText().toString().trim().length()==0 || countryName.getText().toString().trim().length()==0 || recipeIngredients.getText().toString().trim().length()==0 || recipeDirections.getText().toString().trim().length()==0 || ImageList.size() == 0)
                {
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(getActivity(),R.string.Enter_All_Details,Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    rName = recipeName.getText().toString().trim();
                    cName = chefName.getText().toString().trim();
                    rType = spinner.getSelectedItem().toString().trim();
                    rDuration = recipeDuration.getText().toString().trim();
                    country = countryName.getText().toString().trim();
                    rIngredients = recipeIngredients.getText().toString().trim();
                    rDirections = recipeDirections.getText().toString().trim();

                    saveData(); //sahil's
                    AddRecipe();
                }
            }
        });


        recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent,"Select Image(s)"), PICK_IMAGE);


            }
        });

        recipeImageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                return imageView;
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position > 0)
                {
                    recipeImageView.setInAnimation(in);
                    position--;
                    recipeImageView.setImageURI(ImageList.get(position));

                }
                else
                {
                    Toast.makeText(getActivity(), "No Previous Images", Toast.LENGTH_SHORT).show();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position < ImageList.size() - 1)
                {

                    recipeImageView.setOutAnimation(out);
                    position++;
                    recipeImageView.setImageURI(ImageList.get(position));

                }
                else
                {
                    Toast.makeText(getActivity(), "No More Images", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }




    private void AddRecipe()
    {


        StorageReference storageRef=FirebaseStorage.getInstance().getReference().child("Recipe Images").child(rName);

        for(upload_count = 0;upload_count < ImageList.size();upload_count++)
        {
            Uri IndividualImage = ImageList.get(upload_count);
            StorageReference imageName = storageRef.child("Image"+IndividualImage.getLastPathSegment());


            imageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);

                            uploadData(url);
                        }
                    });
                }
            });
        }



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
        //recipeInfo.setRecipeImageLink(url);



        for (int i = 0;i>=j;i--)
        {
            recipeListBranch.setValue(recipeInfo);
        }
        j++;



        DatabaseReference reference1 = firebaseDatabase.getReference().child("User Recipe Images");

        DatabaseReference recipeListBranch1 = reference1.child(firebaseAuth.getUid()).child(rName);


        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("recipeName",rName);
        hashMap.put("recipeImageLink",url);

        recipeListBranch1.push().setValue(hashMap);

        Toast.makeText(getActivity(), "Recipe Added", Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.discoverFragment);

        progressDialog.dismiss();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getClipData() != null)
        {

            int count = data.getClipData().getItemCount();

            for (int i=0;i<count;i++)
            {
                ImageUri = data.getClipData().getItemAt(i).getUri();
                ImageList.add(ImageUri);

            }

            recipeImageView.setImageURI(ImageList.get(0));
            position = 0;

            recipeImageView.setBackground(null);
            progressDialog.dismiss();


        }
        else
        {
            Uri ImageUri = data.getData();
            ImageList.add(ImageUri);
            recipeImageView.setImageURI(ImageList.get(0));
            position = 0;


        }

        super.onActivityResult(requestCode, resultCode, data);


    }

    private void saveData() {

        String r_name = recipeName.getText().toString();
        String c_name = chefName.getText().toString();

        //Saving Recipe name
        RecipeSearchInfo recipeSearchInfo = new RecipeSearchInfo(r_name,c_name);
        mref = FirebaseDatabase.getInstance().getReference("recipe_chef's_names");

        String recipeID = mref.push().getKey();
        mref.child(recipeID).setValue(recipeSearchInfo);
    }
}