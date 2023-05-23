package com.group11.shoppuka.project.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.FragmentCategoryPageBinding;
import com.group11.shoppuka.project.model.Attributes;
import com.group11.shoppuka.project.model.Product;
import com.group11.shoppuka.project.service.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryPageFragment newInstance(String param1, String param2) {
        CategoryPageFragment fragment = new CategoryPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentCategoryPageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.103:1337/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService myApi = retrofit.create(ApiService.class);

        myApi.getProduct(2).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = response.body();
                Attributes attributes = product.getData().getAttributes();
                System.out.println(attributes.getName());
                Log.i("API",product.toString());
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                t.printStackTrace();
            }
        });


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(getContext(),"DocumentSnapshot added with ID: " + documentReference.getId(),Toast.LENGTH_LONG);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),"DocumentSnapshot added with ID: " + e.getMessage(),Toast.LENGTH_LONG);
//                    }
//                });
        return view;


    }
}