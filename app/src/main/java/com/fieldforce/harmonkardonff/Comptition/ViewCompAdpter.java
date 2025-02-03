


package com.fieldforce.harmonkardonff.Comptition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCompAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Map<String, List<CompitionModel>> displaypic;
    private List<String> dates; // To maintain the order of headers
    private Context context;

    // ViewHolder for Header (Date)
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;

        public HeaderViewHolder(View view) {
            super(view);
            dateTextView = view.findViewById(R.id.headerTextView); // Assuming this TextView is in your layout
        }
    }

    // ViewHolder for Items (Competition details)
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat;
        public TextView et_qty;

        public MyViewHolder(View view) {
            super(view);
            cat = view.findViewById(R.id.spin1Cap);
            et_qty = view.findViewById(R.id.image1Cap);
        }
    }

    // Constructor
    public ViewCompAdpter(Map<String, List<CompitionModel>> displaypic, Context context) {
        this.displaypic = displaypic;
        this.context = context;
        this.dates = new ArrayList<>(displaypic.keySet());
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        int count = 0;
        for (String date : dates) {
            if (position == count) {
                return true;
            }
            count += displaypic.get(date).size() + 1; // +1 for the header
        }
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_layout, parent, false); // Assume `header_layout` contains a TextView for the header
            return new HeaderViewHolder(view);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_display_layout, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            // Bind Header Data (Date)
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            String date = getDateForPosition(position);
            headerHolder.dateTextView.setText("Date:-"+date);
        } else {
            // Bind Item Data
            MyViewHolder itemHolder = (MyViewHolder) holder;
            CompitionModel displayPicModel = getItemForPosition(position);

            if (displayPicModel != null) {
                itemHolder.cat.setText(displayPicModel.getQuestion());
                itemHolder.et_qty.setText(String.valueOf(displayPicModel.getQuantity()));
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (String date : dates) {
            count += displaypic.get(date).size() + 1; // +1 for the header
        }
        return count;
    }

    private String getDateForPosition(int position) {
        int count = 0;
        for (String date : dates) {
            if (position == count) {
                return date;
            }
            count += displaypic.get(date).size() + 1; // +1 for the header
        }
        return null;
    }

    private CompitionModel getItemForPosition(int position) {
        int count = 0;

        for (String date : dates) {
            if (position > count && position <= count + displaypic.get(date).size()) {
                return displaypic.get(date).get(position - count - 1); // Subtracting the header
            }
            count += displaypic.get(date).size() + 1; // +1 for the header
        }
        return null;
    }
}
