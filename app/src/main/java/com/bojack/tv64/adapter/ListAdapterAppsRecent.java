package com.bojack.tv64.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bojack.tv64.R;
import com.bojack.tv64.model.AppsMd;
import com.bojack.tv64.model.SharedPrefInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListAdapterAppsRecent extends RecyclerView.Adapter<ListAdapterAppsRecent.Holder> {

    List<String> lista;
    Context context;


    public ListAdapterAppsRecent(Context context) {
        this.context = context;
        getListapp();
    }

    public void getListapp(){
        SharedPreferences sharedPref = context.getSharedPreferences(
                "app_recente",
                Context.MODE_PRIVATE);

        Map<String, String> map = (Map<String, String>) sharedPref.getAll();
        List<String> list_app = new ArrayList<>(map.values());
        Collections.reverse(list_app);
        this.lista = list_app;
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
        AppsMd ap = null;
        try{

            ResolveInfo ri = context.getPackageManager().resolveActivity(context.getPackageManager().getLaunchIntentForPackage(packageApp), 0);
            ap = new SharedPrefInfo().getApp(context, ri);

            Glide.with(context).load(ap.getIcone()).into(holder.icone_app_recent);
            holder.nome_app_recent.setText(ap.getNome());

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


        AppsMd finalAp = ap;
        holder.container_APP_click_recent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Apps")
                        .setMessage("você deseja apagar '"+finalAp.getNome()+"' ?")
                        .setPositiveButton("APAGAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences sharedPref = context.getSharedPreferences(
                                        "app_recente",
                                        Context.MODE_PRIVATE);

                                for(Map.Entry<String, ?> a : sharedPref.getAll().entrySet()){
                                    if(packageApp.equals(a.getValue())){
                                        sharedPref.edit().remove(a.getKey()).apply();
                                        break;
                                    }
                                }

                                Toast.makeText(context, "DELETADO!", Toast.LENGTH_SHORT).show();
                                getListapp();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();

                return false;
            }
        });


    }



    @Override
    public int getItemViewType(int position) {
        return position;
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
