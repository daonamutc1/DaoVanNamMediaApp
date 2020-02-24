package com.example.mediaapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.example.mediaapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Song> songArrayList = new ArrayList<>();
    int position = 2;
    MediaPlayer mediaPlayer;
    boolean isPlay = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        songArrayList.add(new Song("Broken Angel", R.raw.broken_angel));
        songArrayList.add(new Song("Bruno Mars", R.raw.bruno_mars));
        songArrayList.add(new Song("Co be mua dong", R.raw.cobemuadong));
        songArrayList.add(new Song("Dem nam mo pho", R.raw.demnammopho));

        khoitao();

        mediaPlayer.start();
        //Nhớ gán thanh max cho seekbar
        binding.sbProgress.setMax(mediaPlayer.getDuration());
        setTimeTotal();
        UpdateTimeSong();
        binding.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlay) {
                    mediaPlayer.start();
                    isPlay = true;
                    binding.imgPlay.setImageResource(R.drawable.play);

                } else {
                    mediaPlayer.pause();
                    isPlay = false;
                    binding.imgPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                binding.sbProgress.setMax(mediaPlayer.getDuration());
                UpdateTimeSong();
            }
        });
        binding.imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    mediaPlayer.stop();
                    isPlay = false;
                    //Sau khi stop máy hủy hết toàn bộ dữ liệu nên cần set lại
                    khoitao();
                }
            }
        });
        binding.imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(true);
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(false);
            }
        });
        binding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(binding.sbProgress.getProgress());
            }
        });
    }

    private void khoitao() {
        mediaPlayer = new MediaPlayer().create(MainActivity.this, songArrayList.get(position).getFile());
        binding.tvName.setText(songArrayList.get(position).getName());

    }
    private void next(boolean isNext){
        if (isNext){
            if (position == songArrayList.size()-1){
                position=0;
            }else {
                position++;
            }
        }else {
            if (position==0){
                position=songArrayList.size()-1;
            }else {
                position--;
            }
        }
        mediaPlayer.stop();
        khoitao();
        mediaPlayer.start();
        setTimeTotal();
    }
    private void setTimeTotal(){
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("mm:ss");
        binding.tvSumTime.setText(simpleFormatter.format(mediaPlayer.getDuration()));
    }
    private void UpdateTimeSong(){
        //Nho chon bien android.os
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleFormatter = new SimpleDateFormat("mm:ss");
                binding.tvTime.setText(simpleFormatter.format(mediaPlayer.getCurrentPosition()));
                binding.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,1000);
            }
        },100);
    }
}
