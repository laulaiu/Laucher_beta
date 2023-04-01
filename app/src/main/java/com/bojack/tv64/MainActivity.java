package com.bojack.tv64;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bojack.tv64.adapter.ListAdapterAppsRecent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    ImageButton btn_listaApp;
    RecyclerView rc_favoritesApp;
    ImageView internet_img;
    TextView hora_tv,downloadProgress;
    Button install_app;
    LinearLayout info_container_download;


    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler();
        permissions();

        //enable buttons
        btn_listaApp.setEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_listaApp = findViewById(R.id.btn_listaApp);
        rc_favoritesApp = findViewById(R.id.rc_favoritesApp);
        hora_tv = findViewById(R.id.hora_tv);
        internet_img = findViewById(R.id.internet_img);
        install_app = findViewById(R.id.install_app);
        info_container_download = findViewById(R.id.info_container_download);
        downloadProgress = findViewById(R.id.downloadProgress);

        permissions();
        clockInfo();

        install_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileApk();
            }
        });

        btn_listaApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_listaApp.setEnabled(false);
                startActivity(new Intent(MainActivity.this, ListApp.class));
            }
        });

    }

    private void permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int storageRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                int storageWrite = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (storageWrite == PackageManager.PERMISSION_GRANTED &
                        storageRead == PackageManager.PERMISSION_GRANTED  ) {
                    verifyAnyApp();
                } else {
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            }, 12);
                }
            }
        }
    }

    int stopTimer = 0;
    String pathApkFile = Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOWNLOADS+"/tv_64/";
    public void downloadApp(){

        try{
            Uri uri = Uri.parse("https://irtix.net/aplicativos/X64_1.5.5.apk");
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("My File");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/tv_64/","tv_64x.apk");

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = manager.enqueue(request);

            new Thread(() -> {

                int secund = 2;
                int tempo = secund * 1000;
                Timer time = new Timer();


                time.scheduleAtFixedRate(new TimerTask() {
                    @SuppressLint("Range")
                    public void run() {
                        Cursor cursor = null;
                        try {
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(downloadId);
                            cursor = manager.query(query);
                            cursor.moveToFirst();

                            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                                if(stopTimer >= 1){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            install_app.setVisibility(View.VISIBLE);
                                            Toast.makeText(MainActivity.this, "Download Complete!", Toast.LENGTH_LONG).show();
                                            info_container_download.setVisibility(View.GONE);
                                        }
                                    });
                                    time.cancel();
                                }else{
                                    stopTimer++;
                                }
                            }
                            int bytes_downloaded = cursor.getInt(cursor
                                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            final int dl_progress = (int) ((bytes_downloaded * 100) / bytes_total);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    downloadProgress.setText(dl_progress +"%");
                                }
                            });
                            cursor.close();
                        } catch (Exception e) {
                            cursor.close();
                            time.cancel();
                            e.printStackTrace();
                        }
                    }
                }, tempo, tempo);
            }).start();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Baixando...!", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ae){
            ae.printStackTrace();
            info_container_download.setVisibility(View.VISIBLE);
        }

    }

    private void verifyAnyApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
                boolean onOff = false;
                for (ResolveInfo ri : ril) {
                    if (ri.activityInfo != null) {
                    String packageApp =  ri.activityInfo.packageName;
                    if(packageApp.equals("com.mediasperfect.perfect.player.tvbox")){
                        onOff = true;
                        break;
                        }
                    }
                }

                if(!onOff){
                    boolean onOff1 = new File(pathApkFile+ "tv_64x.apk").exists();
                    if(onOff1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                install_app.setVisibility(View.VISIBLE);
                            }
                        });
                    }else{
                        downloadApp();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                install_app.setVisibility(View.GONE);
                                info_container_download.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }else{
                    install_app.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    private void OpenFileApk() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() + ".provider",
                            new File(pathApkFile+   "tv_64x.apk"));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
                    startActivity(intent );

                } catch (Exception cv) {
                    Toast.makeText(MainActivity.this, "Error open File apk", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void clockInfo() {

        int secund = 2;
        int tempo = secund * 1000;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            getStatusInfo(hora_tv , internet_img);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, tempo, tempo);

    }

    private void getStatusInfo(TextView hora_tv, ImageView internet_img) {

        LocalDateTime dataHoraAtual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dataHoraAtual = LocalDateTime.now();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hora_tv.setText(dataHoraAtual.getHour()+":"+dataHoraAtual.getMinute());
        }
        ConnectivityManager connManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo eThernet = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo dAdos = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected()) {
            internet_img.setImageResource(R.drawable.icons_wifi);
        }else{
            if(eThernet.isConnected()){
                internet_img.setImageResource(R.drawable.icons_ethernet);
            }else{
                if(dAdos.isConnected()){
                    internet_img.setImageResource(R.drawable.icon_4g);
                }else{
                    internet_img.setImageResource(R.drawable.icons_wifi_erro);
                }
            }
        }
    }

    private void updateRecycler() {
        rc_favoritesApp.setLayoutManager(new GridLayoutManager(getApplicationContext(),4,LinearLayoutManager.VERTICAL,false));
        rc_favoritesApp.setAdapter(new ListAdapterAppsRecent(MainActivity.this));
    }

}