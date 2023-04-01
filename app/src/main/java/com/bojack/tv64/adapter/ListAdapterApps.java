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
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListAdapterApps extends RecyclerView.Adapter<ListAdapterApps.Holder> {

    List<AppsMd> lista;
    Context context;

    public ListAdapterApps(Context context,List<AppsMd> lista) {
        this.lista = lista;
        this.context = context;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_apps, parent, false);
        return new ListAdapterApps.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {

            if(lista.get(position).getIcone() != null){
                String packageApp = lista.get(position).getPackage_app();
                ResolveInfo ri = context.getPackageManager().resolveActivity(context.getPackageManager().getLaunchIntentForPackage(packageApp), 0);
                Glide.with(context).load(ri.loadIcon(context.getPackageManager())).into(holder.icone_app_adp);
            }else{
                Glide.with(context).load(R.drawable.icon_erro).into(holder.icone_app_adp);
            }

            holder.nome_app_adp.setText(lista.get(position).getNome());

            holder.container_APP_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(lista.get(position).getPackage_app());
                    context.startActivity(intent);
                }
            });

            holder.container_APP_click.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String packageApp = lista.get(position).getPackage_app();
                    String nameApp = lista.get(position).getNome();

                    new AlertDialog.Builder(context)
                            .setTitle("Apps")
                            .setMessage("você deseja adicionar '"+nameApp+"' ?")
                            .setPositiveButton("Adiocionar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences sharedPref = context.getSharedPreferences(
                                            "app_recente",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    for(Map.Entry<String, ?> a : sharedPref.getAll().entrySet()){
                                        if(packageApp.equals(a.getValue())){
                                            sharedPref.edit().remove(a.getKey()).apply();
                                            Map<String, String> map = (Map<String, String>) sharedPref.getAll();
                                            List<String> ls = new ArrayList<String>(map.values());
                                            for(int k = 0; k < ls.size();k++){
                                                editor.putString(k+"", ls.get(k));
                                            }
                                            break;
                                        }
                                    }

                                    int indice = sharedPref.getAll().size();

                                    editor.putString(indice+"", packageApp );
                                    editor.apply();
                                    Toast.makeText(context, "SALVO!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_menu_upload_you_tube)
                            .show();

                    return false;
                }
            });
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        ImageView icone_app_adp;
        TextView nome_app_adp;
        ConstraintLayout container_APP_click;

        public Holder(@NonNull View itemView) {
            super(itemView);
            icone_app_adp = itemView.findViewById(R.id.icone_app_adp);
            nome_app_adp = itemView.findViewById(R.id.nome_app_adp);
            container_APP_click = itemView.findViewById(R.id.container_APP_click);
        }

    }
}
