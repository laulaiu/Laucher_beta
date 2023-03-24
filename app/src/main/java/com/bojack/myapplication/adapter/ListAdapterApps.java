package com.bojack.myapplication.adapter;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bojack.myapplication.ListApp;
import com.bojack.myapplication.R;
import com.bojack.myapplication.model.AppsMd;
import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
        if(viewType == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_header_apps, parent, false);
            return new ListAdapterApps.Holder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_apps, parent, false);
        return new ListAdapterApps.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {

        if(position == 0){

            int secund = 2;
            int tempo = secund * 1000;

            new Timer().scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {

                        ((ListApp)context).runOnUiThread(new Runnable() {
                            public void run() {
                                getStatusInfo(holder.hora_tv , holder.wifiStatus_img);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, tempo, tempo);




        }else{
            //lista os apps
            if(lista.get(position).getIcone() != null){

                String packageApp = lista.get(position).getPackage_app();
                ResolveInfo ri = context.getPackageManager().resolveActivity(context.getPackageManager().getLaunchIntentForPackage(packageApp), 0);

                Glide.with(context).load(ri.loadIcon(context.getPackageManager())).into(holder.icone_app_adp);
                //Glide.with(context).load(lista.get(position).getIcone()).into(holder.icone_app_adp);
            }else{
                Glide.with(context).load(R.drawable.icon_erro).into(holder.icone_app_adp);
            }
            holder.nome_app_adp.setText(lista.get(position).getNome());


            holder.container_APP_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String packageApp = lista.get(position).getPackage_app();
                    String nameApp = lista.get(position).getNome();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            "app_recente",
                            Context.MODE_PRIVATE);

                    int indice = sharedPref.getAll().size();
                    Toast.makeText(context, "ind:"+indice, Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(nameApp, packageApp );
                    editor.apply();


                    //sharedPref.edit().clear().apply();

                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageApp);
                    context.startActivity(intent);

                }
            });

        }

    }

    private void getStatusInfo(TextView hora_tv, ImageView wifiStatus_img) {

        Date dataAtual = new Date();
        LocalDateTime dataHoraAtual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dataHoraAtual = LocalDateTime.now();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hora_tv.setText(dataHoraAtual.getHour()+":"+dataHoraAtual.getMinute());
        }


        ConnectivityManager connManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            wifiStatus_img.setImageResource(R.drawable.icons_wifi);
        }else{
            wifiStatus_img.setImageResource(R.drawable.icons_wifi_erro);
        }

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

        ImageView icone_app_adp;
        ImageView wifiStatus_img;
        TextView nome_app_adp, hora_tv;
        ConstraintLayout container_APP_click;
        RecyclerView rc_recentesApps;

        public Holder(@NonNull View itemView) {
            super(itemView);

            try{
                hora_tv = itemView.findViewById(R.id.hora_tv);
                wifiStatus_img = itemView.findViewById(R.id.wifiStatus_img);
                rc_recentesApps = itemView.findViewById(R.id.rc_recentesApps);
            }catch (Exception e){

            }

            icone_app_adp = itemView.findViewById(R.id.icone_app_adp);
            nome_app_adp = itemView.findViewById(R.id.nome_app_adp);
            container_APP_click = itemView.findViewById(R.id.container_APP_click);

        }

    }
}
