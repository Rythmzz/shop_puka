package com.group11.shoppuka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.group11.shoppuka.project.LoginFragment;
import com.group11.shoppuka.project.view.AccountPageFragment;
import com.group11.shoppuka.project.view.CategoryPageFragment;
import com.group11.shoppuka.project.view.HomePageFragment;
import com.group11.shoppuka.project.view.OrderPageFragment;
import com.group11.shoppuka.project.view.SearchPageFragment;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle drawerToggle;

    ViewFlipper viewFlipper;

    NavigationView navigationView;

    Fragment fragmentDangNhap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();


    }

    private void setEvent(){
        //drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Banner();
        navigationView = (NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){

                switch (item.getItemId()){
                    case R.id.Home:
                        Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_LONG).show();
                            fragmentDangNhap = new LoginFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content1,fragmentDangNhap).commit();
                        break;
                    case R.id.Cate:
                        Toast.makeText(MainActivity.this,"Category",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.Search:
                        Toast.makeText(MainActivity.this,"Search",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.Order:
                        Toast.makeText(MainActivity.this,"Order",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.Account:
                        Toast.makeText(MainActivity.this,"Account",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"Not choice",Toast.LENGTH_LONG).show();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void setControl(){
        drawerLayout = findViewById(R.id.drawerLayout);
        viewFlipper = findViewById(R.id.viewFlipper);
        navigationView=findViewById(R.id.navigationView);


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    private void Banner(){
        ImageView view1 = new ImageView(getApplicationContext());
        view1.setImageResource(R.drawable.group11);
        ImageView view2= new ImageView(getApplicationContext());
        view2.setImageResource(R.drawable.pk_product_aothun);
        viewFlipper.addView(view1);
        viewFlipper.addView(view2);
        viewFlipper.setAutoStart(true);
    }


}