package com.kenzahn.zahn;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kenzahn.zahn.adapter.PodcastListAdapter;
import com.kenzahn.zahn.model.PodcastListModel;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MediaPlayerActivity extends AppCompatActivity
{
    ImageView image_playPause;
    TextView textCurrentTime,textTotalDuration;
    SeekBar playerSeekbar;
    MediaPlayer mediaPlayer;
    Handler handler=new Handler();
    int position;
    ImageView iv_back,iv_for;
    TypeFaceTextViewBold tv_title;
    TypeFaceTextView tv_des;
   // String [] songs;
    ArrayList<PodcastListModel> arrayList=new ArrayList<>();
    ProgressDialog pd;
    //ArrayList<PodcastListModel> player_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading....");
        pd.setCancelable(false);
        image_playPause=findViewById(R.id.image_play_pause);
        textCurrentTime=findViewById(R.id.tv_start_time);
        textTotalDuration=findViewById(R.id.tv_total_duration);
        playerSeekbar=findViewById(R.id.seekBar);
        iv_back=findViewById(R.id.iv_backword);
        iv_for=findViewById(R.id.iv_forword);
        tv_des=findViewById(R.id.tv_p_des);
        tv_title=findViewById(R.id.tv_p_title);
        mediaPlayer=new MediaPlayer();
        playerSeekbar.setMax(100);
        getData();
        image_playPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying())
            {
                handler.removeCallbacks(updater);
                mediaPlayer.pause();
                image_playPause.setImageResource(R.drawable.play);
            }else {
                mediaPlayer.start();
                image_playPause.setImageResource(R.drawable.pause);
                updateSeekbar();
            }
        });

//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                if (pd != null && pd.isShowing()){
//                    pd.dismiss();
//                }
//                mp.start();
//                image_playPause.setImageResource(R.drawable.pause);
//                updateSeekbar();
//            }
//        });
        playerSeekbar.setOnTouchListener((v, event) -> {
            SeekBar seekBar=(SeekBar)v;
            int playposition=(mediaPlayer.getDuration()/100)*seekBar.getProgress();
            mediaPlayer.seekTo(playposition);
            textCurrentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
            return false;
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent)
            {
                playerSeekbar.setSecondaryProgress(percent);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                playerSeekbar.setProgress(0);
                image_playPause.setImageResource(R.drawable.play);
//                textCurrentTime.setText("0:0");
//                textTotalDuration.setText("0:0");
                mp.stop();
                mp.reset();
                if (position+1<arrayList.size())
                {
                    position=position+1;
                    mediaPlayer.stop();
                    prepareMediaPlayer(position);
                }else {
                    Toast.makeText(MediaPlayerActivity.this, "No more episodes", Toast.LENGTH_SHORT).show();
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isNetworkAvailable())
                {
                    if (position!=0)
                    {
                        position=position-1;
                        mediaPlayer.stop();
                        prepareMediaPlayer(position);
                    }else {
                        Toast.makeText(MediaPlayerActivity.this, "No more episodes", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MediaPlayerActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

                }



            }
        });
        iv_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable())
                {
                    if (position+1<arrayList.size())
                    {
                        position=position+1;
                        mediaPlayer.stop();
                        prepareMediaPlayer(position);
                    }else {
                        Toast.makeText(MediaPlayerActivity.this, "No more episodes", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MediaPlayerActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

                }


            }
        });
        getSupportActionBar().setTitle(arrayList.get(position).getPodcastTopic());

//        if (songs.length>0)
//        {
//            prepareMediaPlayer(position);
//        }

    }
    private String milliSecondsToTimer(long milliSeconds)
    {
        String timerString="";
        String secondsString;
        int houres=(int) (milliSeconds / (1000 * 60 * 60));
        int minutes=(int) (milliSeconds % (1000 * 60 * 60)) / (1000*60);
        int seconds=(int) ((milliSeconds % (1000 * 60 * 60)) % (1000*60) / 1000);
        if (houres>0)
        {
            timerString=houres + ":";
        }
        if (seconds < 10)
        {
            secondsString="0" + seconds;
        }else {
            secondsString="" + seconds;
        }
        timerString= timerString + minutes+ ":" + secondsString;
        return  timerString;
    }

    private  void updateSeekbar()
    {
        if (mediaPlayer.isPlaying())
        {
            playerSeekbar.setProgress((int) (((float)mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater,1000);
        }
    }
    private Runnable updater=new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
            long currentPosition=mediaPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSecondsToTimer(currentPosition));
            // handler.postDelayed(updater,100);
        }
    };

    private void prepareMediaPlayer(int position)
    {

        try {
          //  pd.show();

//            mediaPlayer.setDataSource(songs[position]);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrayList.get(position).getPodcastURL());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            textTotalDuration.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
            tv_title.setText(arrayList.get(position).getPodcastName());
            tv_des.setText(arrayList.get(position).getDescription());
            //startPlay();
            onResume();
        }catch (Exception exception)
        {
            System.out.println(exception.getMessage());
           // Toast.makeText(this, ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
       // mediaPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    void getData()
    {
       Bundle b= getIntent().getExtras();
       if (b!=null)
       {
          // player_list = b.getParcelableArrayList("array");
           position=b.getInt("pos");
           //songs= b.getStringArray("song");
           String res=b.getString("res");
          // String name=b.getString("name");
           //getSupportActionBar().setTitle(name==null?"Podcast":name);
           assignData(res);
       }

    }

    private void assignData(String response)
    {
        arrayList.clear();

        try {
            JSONArray ja=new JSONArray(response);
            for (int i=0;i<ja.length();i++)
            {
                JSONObject json= ja.getJSONObject(i);
                String LastClassDate= json.optString("LastClassDate");
                int PodcastID= json.optInt("PodcastID");
                String CreateDate= json.optString("CreateDate");
                String PodcastURL= json.optString("PodcastURL");
                String PodcastName= json.optString("PodcastName");
                int PodcastTopicID= json.optInt("PodcastTopicID");
                boolean Active= json.optBoolean("Active");
                String Description= json.optString("PodcastName");
                String PodcastTopic= json.optString("PodcastTopic");
                PodcastListModel podcastListModel=new PodcastListModel(LastClassDate,PodcastID,CreateDate,PodcastURL,PodcastName,PodcastTopicID,Active,Description,PodcastTopic);
                arrayList.add(podcastListModel);
            }
            if (arrayList.size()>0)
            {
                prepareMediaPlayer(position);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void startPlay()
    {
        mediaPlayer.start();
        image_playPause.setImageResource(R.drawable.pause);
        updateSeekbar();
        pd.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        image_playPause.setImageResource(R.drawable.pause);
        updateSeekbar();

    }
}
