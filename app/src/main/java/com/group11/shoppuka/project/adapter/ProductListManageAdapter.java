package com.group11.shoppuka.project.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.view.DetailProductPageActivity;
import com.group11.shoppuka.project.view.ModifyProductActivity;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductListManageAdapter extends RecyclerView.Adapter<ProductListManageAdapter.AccountAdapterViewHolder> {
    private final Context mContext;
    private ProductResponse products;
    public ProductListManageAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(ProductResponse products){
        this.products = products;
    }

    @NonNull
    @Override
    public AccountAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hoder_product,parent,false);
        return new AccountAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapterViewHolder holder, int position) {


        String url = MyApplication.localHost + products.getData().get(position).getAttributes().getImageURL();
        Product currentProduct = products.getData().get(position);
        Glide.with(holder.itemView.getContext()).load(url).into(holder.civAccount);
        if (currentProduct.getAttributes().getName().length() <= 27)
            holder.tvNameProduct.setText(currentProduct.getAttributes().getName());
        else
            holder.tvNameProduct.setText((currentProduct.getAttributes().getName()).substring(0, Math.min(currentProduct.getAttributes().getName().length(), 27)) + "...");
        holder.price.setText(String.valueOf(currentProduct.getAttributes().getPrice())+" VNĐ");
        holder.salePrice.setText(String.valueOf(currentProduct.getAttributes().getSalePrice())+ " VNĐ");
        holder.llItemAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = products.getData().get(position);
                Intent intent = new Intent(view.getContext(), ModifyProductActivity.class);
                intent.putExtra("product",product);
                view.getContext().startActivity(intent);
            }
        });
        execBtnOptionAccount(holder, position);
    }

    private void execBtnOptionAccount(AccountAdapterViewHolder holder, int position){
        holder.btnOptionalAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopMenu(holder,position);
            }
        });
    }

    private void showPopMenu(AccountAdapterViewHolder view, int position){
        PopupMenu popupMenu = new PopupMenu(mContext,view.btnOptionalAccount);
        popupMenu.getMenuInflater().inflate(R.menu.menu_account_optional,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_update_account:
                        Product product = products.getData().get(position);
                        Intent intent = new Intent(mContext, ModifyProductActivity.class);
                        intent.putExtra("product",product);
                        mContext.startActivity(intent);
                        break;
                    case R.id.menu_delete_account:
                        openDialogVerify(Gravity.CENTER,position);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void openDialogVerify(int gravity, int position){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(androidx.core.R.layout.custom_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        dialog.setCancelable(false);
        dialog.show();

        Button btnAccept = dialog.findViewById(R.id.btn_accept_dialog);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_dialog);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(ProductViewModel.class);
                viewModel.deleteData(products.getData().get(position).getId());
                dialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }



    @Override
    public int getItemCount() {
        if(products != null){
            return products.getData().size();
        }
        return 0;
    }


    public class AccountAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameProduct;
        private CircleImageView civAccount;
        private CardView itemAccount;
        private ImageButton imgEditAccount;
        private LinearLayout llItemAccount;
        private ImageButton btnOptionalAccount;
        private Button btnAcceptDialog, btnCancelDialog;

        private TextView price;
        private TextView salePrice;

        public AccountAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View view){
            tvNameProduct = view.findViewById(R.id.tv_name_account);
            itemAccount = view.findViewById(R.id.cvAccount);
            llItemAccount = view.findViewById(R.id.ll_item_account);
            btnOptionalAccount = view.findViewById(R.id.btn_optional_account);
            btnAcceptDialog = view.findViewById(R.id.btn_accept_dialog);
            btnCancelDialog = view.findViewById(R.id.btn_cancel_dialog);
            civAccount= view.findViewById(R.id.civ_account);
            price = view.findViewById(R.id.tv_price_product);
            salePrice = view.findViewById(R.id.tv_sale_price_product);
        }
    }
}
