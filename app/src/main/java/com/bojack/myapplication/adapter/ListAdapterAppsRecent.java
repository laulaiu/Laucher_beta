package com.bojack.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bojack.myapplication.R;
import com.bojack.myapplication.model.AppsMd;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;

import java.util.List;

public class ListAdapterAppsRecent extends RecyclerView.Adapter<ListAdapterAppsRecent.Holder> {

    List<String> lista;
    Context context;

    public ListAdapterAppsRecent(Context context,List<String> lista) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_apps_recent, parent, false);
        return new ListAdapterAppsRecent.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {


        String packageApp = lista.get(position);
        try{
            ResolveInfo ri = context.getPackageManager().resolveActivity(context.getPackageManager().getLaunchIntentForPackage(packageApp), 0);

            Glide.with(context).load(ri.loadIcon(context.getPackageManager())).into(holder.icone_app_recent);
            holder.nome_app_recent.setText(
                            context.getPackageManager()
                            .getResourcesForApplication(ri.activityInfo.applicationInfo)
                            .getString(ri.activityInfo.labelRes));

        }catch (Exception ae){
            Glide.with(context).load(R.drawable.icon_erro).into(holder.icone_app_recent);
        }


        holder.container_APP_click_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageApp);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        ImageView icone_app_recent;
        TextView nome_app_recent;
        ConstraintLayout container_APP_click_recent;

        public Holder(@NonNull View itemView) {
            super(itemView);

            icone_app_recent = itemView.findViewById(R.id.icone_app_recent);
            nome_app_recent = itemView.findViewById(R.id.nome_app_recent);
            container_APP_click_recent = itemView.findViewById(R.id.container_APP_click_recent);

        }

    }

}
