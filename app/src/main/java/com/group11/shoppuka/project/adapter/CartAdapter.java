package com.group11.shoppuka.project.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group11.shoppuka.project.bean.ICartItem;
import com.group11.shoppuka.project.bean.IChildItem;
import com.group11.shoppuka.project.bean.IGroupItem;
import com.group11.shoppuka.project.listener.OnCheckChangeListener;
import com.group11.shoppuka.project.listener.OnChildCollapsingChangeListener;
import com.group11.shoppuka.project.viewholder.CartViewHolder;

import java.util.List;


public abstract class CartAdapter<VH extends CartViewHolder> extends RecyclerView.Adapter<VH> implements OnChildCollapsingChangeListener {
    public static final int PAYLOAD_CHECKBOX = 0x000001;
    public static final int PAYLOAD_COLLAPSING = 0x000002;

    protected List<ICartItem> mData;
    protected Context mContext;
    protected OnCheckChangeListener onCheckChangeListener;
    protected boolean isCanCollapsing;

    public CartAdapter(Context context, List<ICartItem> data) {
        this(context, data, false);
    }

    public CartAdapter(Context context, List<ICartItem> data, boolean canCollapsing) {
        mContext = context;
        mData = data;
        isCanCollapsing = canCollapsing;
    }

    public boolean isCanCollapsing() {
        return isCanCollapsing;
    }

    public void setCanCollapsing(boolean canCollapsing) {
        isCanCollapsing = canCollapsing;
        if (mData != null) {
            notifyDataSetChanged();
        }
    }

    public void setOnCheckChangeListener(OnCheckChangeListener l) {
        onCheckChangeListener = l;
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        VH viewHolder = null;
        switch (viewType) {

            case ICartItem.TYPE_GROUP:
                View groupView = layoutInflater.inflate(getGroupItemLayout(), parent, false);
                viewHolder = getGroupViewHolder(groupView);
                break;
            case ICartItem.TYPE_CHILD:
                View childView = layoutInflater.inflate(getChildItemLayout(), parent, false);
                viewHolder = getChildViewHolder(childView);
                break;
            default:
                viewHolder = getOtherViewHolder();
                break;
        }
        return viewHolder;
    }

    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        ICartItem cartItemBean = mData.get(position);
        holder.bindData(cartItemBean);
        if (holder.mCheckBox != null) {
            holder.mCheckBox.setOnClickListener(new OnCheckBoxClickListener(position,
                    cartItemBean.getItemType()));
            if (holder.mCheckBox.isChecked() != cartItemBean.isChecked()) {
                holder.mCheckBox.setChecked(cartItemBean.isChecked());
            }
        }
        if (isCanCollapsing) {
            if (ICartItem.TYPE_CHILD == cartItemBean.getItemType()) {
                changeItemViewShow(holder.itemView, cartItemBean.isCollapsing());
            }

            if (ICartItem.TYPE_GROUP == cartItemBean.getItemType()) {
                bindItemViewCollapsingListener(holder.itemView, position);
            }
        } else {
            if (ICartItem.TYPE_CHILD == cartItemBean.getItemType()) {
                if (holder.itemView.getVisibility() != View.VISIBLE)
                    changeItemViewShow(holder.itemView, false);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            this.onBindViewHolder(holder, position);
        } else {
            if (PAYLOAD_CHECKBOX == ((int) payloads.get(0))) {
                if (holder.mCheckBox != null) {
                    if ((holder.mCheckBox.isChecked() != mData.get(position).isChecked())) {
                        holder.mCheckBox.setChecked(mData.get(position).isChecked());
                    }
                }
            } else if (PAYLOAD_COLLAPSING == ((int) payloads.get(0))) {
                if (ICartItem.TYPE_CHILD == mData.get(position).getItemType()) {
                    changeItemViewShow(holder.itemView, mData.get(position).isCollapsing());
                }
            }
        }
    }


    protected void bindItemViewCollapsingListener(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGroupItemClickCollapsibleChild(mData, position);
            }
        });
    }


    private void changeItemViewShow(View itemView, boolean hide) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (hide) {
            itemView.setVisibility(View.GONE);
            layoutParams.height = 0;
            layoutParams.width = 0;
        } else {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }
        itemView.setLayoutParams(layoutParams);
    }


    private class OnCheckBoxClickListener implements View.OnClickListener {
        int mPosition, mItemType;

        private OnCheckBoxClickListener(int position, int itemType) {
            mPosition = position;
            mItemType = itemType;
        }

        @Override
        public void onClick(View v) {
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCheckedChanged(mData, mPosition, ((CheckBox) v).isChecked(), mItemType);
                onCheckChangeListener.onCalculateChanged(mData.get(mPosition));
            }
        }
    }

    /**
     * delete all checked item
     */
    public void removeChecked() {
        int iMax = mData.size() - 1;
        for (int i = iMax; i >= 0; i--) {
            if (mData.get(i).isChecked()) {
                mData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mData.size());
            }
        }
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }


    public void removeChild(int position) {
        boolean isLastOne = false;
        if ((ICartItem.TYPE_GROUP == mData.get(position - 1).getItemType()) &&
                (((position + 1) == mData.size()) ||
                        (ICartItem.TYPE_GROUP == mData.get(position + 1).getItemType()))) {
            isLastOne = true;
        }
        if (mData.get(position).isChecked()) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
            if (isLastOne) {
                mData.remove(position - 1);
                notifyItemRemoved(position - 1);
                notifyItemRangeChanged(position - 1, mData.size());
            }
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCalculateChanged(null);
            }
        } else {
            if (!isLastOne) {

                if (onCheckChangeListener != null) {
                    onCheckChangeListener.onCheckedChanged(mData, position, true, mData.get(position).getItemType());
                }
            }
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
            if (isLastOne) {
                mData.remove(position - 1);
                notifyItemRemoved(position - 1);
                notifyItemRangeChanged(position - 1, mData.size());
            }
        }
    }

    /**
     * make all item's status change
     *
     * @param isCheck
     */
    public void checkedAll(boolean isCheck) {
        boolean noOneChange = true;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked() != isCheck) {
                mData.get(i).setChecked(isCheck);
                noOneChange = false;
            }
        }
        if (!noOneChange) {
            notifyDataSetChanged();
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCalculateChanged(null);
            }
        }
    }

    public void setNewData(List<ICartItem> datas) {
        mData.clear();
        addData(datas);
    }

    public void addData(List<ICartItem> datas) {
        mData.addAll(datas);
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    private void addItem(int addPosition, ICartItem itemBean) {
        mData.add(addPosition, itemBean);


        notifyItemInserted(addPosition);

        notifyItemRangeChanged(addPosition, mData.size() - addPosition);
    }

    /**
     * add normal
     *
     * @param addPosition
     * @param itemBean
     */


    /**
     * add group
     *
     * @param addPosition
     * @param groupItemBean
     */
    public void addGroup(int addPosition, IGroupItem<IChildItem> groupItemBean) {
        addGroup(addPosition, groupItemBean, false);
    }

    public void addGroup(int addPosition, IGroupItem<IChildItem> groupItemBean,
                         boolean isStrict) {
        if (isStrict) {
            if (groupItemBean.getChilds() == null || groupItemBean.getChilds().size() == 0) {
                Log.i("CartAdapter", "This GroupItem have no one ChildItem");
            } else {
                addItem(addPosition, groupItemBean);
                for (int i = 0; i < groupItemBean.getChilds().size(); i++) {
                    addItem(addPosition + i + 1, groupItemBean.getChilds().get(i));
                }
            }
        } else {
            addItem(addPosition, groupItemBean);
            if (groupItemBean.getChilds() != null && groupItemBean.getChilds().size() != 0) {
                for (int i = 0; i < groupItemBean.getChilds().size(); i++) {
                    addItem(addPosition + i + 1, groupItemBean.getChilds().get(i));
                }
            }
        }
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    public void addGroup(IGroupItem<IChildItem> groupItemBean) {
        addGroup(groupItemBean, false);
    }

    public void addGroup(IGroupItem<IChildItem> groupItemBean, boolean isStrict) {
        addGroup(mData.size(), groupItemBean, isStrict);
    }

    /**
     * add child
     *
     * @param addPosition
     * @param childItemBean
     */
    public void addChild(int addPosition, IChildItem childItemBean) {
        addItem(addPosition, childItemBean);
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCheckedChanged(mData, addPosition,
                    mData.get(addPosition).isChecked(), ICartItem.TYPE_CHILD);
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    public void addChild(IChildItem childItemBean) {
        if (!isHaveGroup() || mData.get(mData.size() - 1).getItemType() == ICartItem.TYPE_NORMAL) {
            Log.i("CartAdapter", "addChild is fail,have no group");
            return;
        }
        addChild(mData.size(), childItemBean);
    }

    /**
     * @return false:have no group.
     */
    private boolean isHaveGroup() {
        boolean isHaveGroup = false;

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                isHaveGroup = true;
                break;
            }
        }
        return isHaveGroup;
    }

    @Override
    public void onGroupItemClickCollapsibleChild(List<ICartItem> beans, int position) {
        boolean collapsing = !beans.get(position).isCollapsing();
        beans.get(position).setCollapsing(collapsing);
        for (int i = (position + 1); i < beans.size(); i++) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                if (beans.get(i).isCollapsing() != collapsing) {
                    beans.get(i).setCollapsing(collapsing);
//                    notifyItemChanged(i, CartAdapter.PAYLOAD_COLLAPSING);
                    notifyItemChanged(i);
                }
            }
        }
    }

    /**
     * If you have other styles of layout, you can return through this.
     *
     * @return
     */
    protected VH getOtherViewHolder() {
        return null;
    }

    /**
     * Returns a ViewHolder(extent CartViewHolder) for different items.
     *
     * @param itemView
     * @return
     */

    protected abstract VH getGroupViewHolder(View itemView);

    protected abstract VH getChildViewHolder(View itemView);

    /**
     * item's layout's id.
     *
     * @return
     */
    protected abstract @LayoutRes
    int getChildItemLayout();

    protected abstract @LayoutRes
    int getGroupItemLayout();



    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public List<ICartItem> getData() {
        return mData;
    }

}