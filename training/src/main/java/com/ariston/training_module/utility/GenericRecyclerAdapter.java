package com.ariston.training_module.utility;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ariston.training_module.R;
import com.ariston.training_module.utility.clicklisteners.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

public class GenericRecyclerAdapter<T> extends RecyclerView.Adapter<GenericRecyclerAdapter.ViewHolder> {
    private final Context context;
    private BindListener<T> bindListener;
    private List<T> items;
    private int layout = R.layout.layout_simple_list;
    private ItemClick<T> itemClick;
    private RecyclerView recyclerview;
    private int lastPosition;
    private boolean animation = true;


    public GenericRecyclerAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
    }

    public GenericRecyclerAdapter(Context context) {
        this.context = context;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public GenericRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GenericRecyclerAdapter.ViewHolder viewHolder, int i) {
        if (bindListener != null) bindListener.bind(items.get(i), viewHolder);
        if (animation) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    (i > lastPosition) ? R.anim.top_anim_recycler
                            : R.anim.bottom_anim_recycler);
            viewHolder.itemView.startAnimation(animation);
            lastPosition = i;
        }
        if (itemClick != null) {
            PushDownAnim.setPushDownAnimTo(viewHolder.itemView).setOnClickListener(v -> {
                if (items == null) return;
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != -1) {
                    itemClick.onClick(items.get(adapterPosition), viewHolder);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setBindListener(BindListener<T> bindListener) {
        this.bindListener = bindListener;
    }

    public void addData(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
        if (recyclerview != null)
            AnimUtils.runLayoutAnimation(recyclerview);
    }

    public void setItemClickListener(ItemClick<T> itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerview = recyclerView;
    }

    @Override
    public void onViewDetachedFromWindow(GenericRecyclerAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }


    public void setTranslateAnimation(boolean animation) {
        this.animation = animation;
    }

    public void clear() {
        if (items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public List<T> getCopyOfList() {

        return new ArrayList<>(items);
    }


    public interface BindListener<T> {
        void bind(T obj, GenericRecyclerAdapter.ViewHolder viewHolder);
    }

    public interface ItemClick<T> {
        void onClick(T obj, GenericRecyclerAdapter.ViewHolder holderObject);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
