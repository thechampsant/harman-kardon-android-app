package com.fieldforce.harmonkardonff.custom_adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.PresentationActivity;
import com.fieldforce.harmonkardonff.PresentationView;
import com.fieldforce.harmonkardonff.R;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.DocumentModel;

public class NewTrainingModuleRecyclerAdapter extends RecyclerView.Adapter<NewTrainingModuleRecyclerAdapter.MyView> {

    ArrayList<DocumentModel> documentModelArrayList;
    Activity main;
    LayoutInflater layoutInflater;
    boolean fromOffers;

    public NewTrainingModuleRecyclerAdapter(ArrayList<DocumentModel> arrDocModel, Activity activity, boolean fromOffers) {
        documentModelArrayList = arrDocModel;
        main = activity;
        this.fromOffers = fromOffers;
        layoutInflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.new_training_recycler_row, null, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView holder, final int position) {
        holder.docName.setText(documentModelArrayList.get(position).DocName);
        holder.docUrl.setText(documentModelArrayList.get(position).DocURL);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = documentModelArrayList.get(position).DocURL;
               /* Intent i = new Intent(main, WebView.class);
                i.setData(Uri.parse(url));
                main.startActivity(i);*/

                Intent i=new Intent(main, PresentationView.class);
                i.putExtra("documenturl",url);
                main.startActivity(i);

                /*Intent intent = new Intent(main, NewTrainingDetailsActivity.class);
                intent.putExtra("Position", holder.getAdapterPosition());
                main.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentModelArrayList != null ? documentModelArrayList.size() : 0;
    }

    class MyView extends RecyclerView.ViewHolder {
        TextView docName;
        TextView docUrl;
        View view;

        public MyView(View itemView) {
            super(itemView);
            docName = (TextView) itemView.findViewById(R.id.docName);
            docUrl = (TextView) itemView.findViewById(R.id.docUrl);
            view = itemView;
        }
    }
}
