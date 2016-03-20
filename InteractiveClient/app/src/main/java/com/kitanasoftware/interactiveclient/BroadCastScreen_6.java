package com.kitanasoftware.interactiveclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.kitanasoftware.interactiveclient.Broadcast.WifiUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BroadCastScreen_6 extends DrawerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f8bfd8"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.broad_cast_screen_6, null);
    }

    SharedPreferences sp;

    Timer stream_play_timer = new Timer();
    Timer broadcast_timer = new Timer();
    private final long BROADCAST_TIME = 5000;//5 seconds
    private final long STREAM_PLAY_MONITOR_TIME = 1000;//2 seconds

    public static DatagramSocket socket;
    public byte[] buffer;

    private int VOICE_STREAM_PORT = 50005;
    private int BROADCAST_IP_PORT = 36367;

    //AudioRecord recorder = null;
    private AudioTrack speaker = null;

    //Audio Configuration.
    private int RECORDER_SAMPLE_RATE = 44100;
    @SuppressWarnings("deprecation")
    private int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
    private int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private boolean status_sending = false;
    private boolean status_receiving = true;

    ArrayList<String> devices = new ArrayList<String>();
    ArrayList<AudioRecord> recorders = new ArrayList<AudioRecord>();

    Thread listenForBroadcastsThread = null;
    Thread broadcastIpThread = null;
    PlayStream playStream = null;

    private double OLD_PLAY_RANDOM = 0.0;//monitor stream play
    private double LASTEST_PLAY_RANDOM = 0.0;//monitor stream play

    Bitmap streamingVoice = null;
    Bitmap notStreamingVoice = null;


    @Override
    public void onResume() {
        super.onResume();

//Check if device is connected to hotspot.
        if (!WifiUtility.isWifiConnected(this)) {
            if (!WifiUtility.isHotSpot(this)) {
                return;
            }
        }

//Only open new threads, if null
//Only open new threads, if null
        if (playStream == null) {
            playStream = (PlayStream) new PlayStream().execute();
            ListenForBroadcasts();
        }
        if (broadcastIpThread == null) {
            sendBroadcast();
        }
        if (listenForBroadcastsThread == null) {
            ListenForBroadcasts();
        }

        monitorStreamPlay();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (stream_play_timer != null) {
            stream_play_timer.cancel();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void start(View view) {

        if (playStream == null) {
            playStream = (PlayStream) new PlayStream().execute();
            System.out.println(" Pressed start!!!!!! ");
        }
    }

    public void stop(View view) {

    }

    //Listen for voice stream and play.
    public class PlayStream extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                @SuppressWarnings("resource")
//Socket Created
                        DatagramSocket socket = new DatagramSocket(VOICE_STREAM_PORT);

                int MIN_BUFFER_SIZE = 8192;

                byte[] buffer = new byte[MIN_BUFFER_SIZE];

                speaker = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
                        RECORDER_SAMPLE_RATE,
                        RECORDER_CHANNELS,
                        RECORDER_AUDIO_ENCODING,
                        MIN_BUFFER_SIZE * 5,
                        AudioTrack.MODE_STREAM);
                speaker.play();

                while (status_receiving == true) {
                    try {

                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//Packet Received
                        socket.receive(packet);

//reading content from packet

                        buffer = packet.getData();
                        String ip = packet.getAddress().toString();
                        System.out.println();

//Writing buffer content to Audiotrack (speaker)
                        speaker.write(buffer, 0, MIN_BUFFER_SIZE);
                        OLD_PLAY_RANDOM = LASTEST_PLAY_RANDOM;
                        LASTEST_PLAY_RANDOM = Math.random();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (SocketException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
        }
    }

    public void broadcastIp(final String message) {
        broadcastIpThread = new Thread(new Runnable() {
            @Override
            public void run() {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    DatagramSocket socket = new DatagramSocket();
                    socket.setBroadcast(true);
                    byte[] sendData = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData,
                            sendData.length,
                            WifiUtility.getBroadcastAddress(),
                            BROADCAST_IP_PORT);
                    socket.send(sendPacket);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        broadcastIpThread.start();
    }

    public void sendBroadcast() {
        broadcast_timer = new Timer();
        broadcast_timer.schedule(new TimerTask() {
            public void run() {
                broadcastIp("Ping");
            }
        }, 0, BROADCAST_TIME);
    }

    //Listen to UDP broadcast and update ip list.
    public void ListenForBroadcasts() {
        listenForBroadcastsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//Keep socket open to listen to all the UDP traffic that is destined for this port.
                    socket = new DatagramSocket(5001, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);

                    while (true) {

//Receive a packet
                        byte[] buffer = new byte[15000];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        final String sender_ip = packet.getAddress().getHostAddress();
                        String device_ip = WifiUtility.getIpAddress();

                        System.out.println(sender_ip + " sender");
                        System.out.println(device_ip + " devi");

// Intent intent2 = new Intent(getApplicationContext(), StartConn.class);
// startService(intent2);

                        sp = getSharedPreferences("editor", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ip", sender_ip);
                        editor.commit();

//if ping is coming from this device, ignore.
                        if (!sender_ip.equalsIgnoreCase(device_ip)) {

                            boolean ip_address_already_exist = false;
//Check if devices exists on the list
                            for (int i = 0; i < devices.size(); i++) {
                                if (devices.get(i).equalsIgnoreCase(sender_ip)) {
                                    ip_address_already_exist = true;
                                }
                            }

                            if (!ip_address_already_exist) {

//Update devices list
                                devices.add(sender_ip);

//if device is streaming voice
                            }

                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        listenForBroadcastsThread.start();
    }

    //Hack
//Determine if stream is being played or not.
    public void monitorStreamPlay() {
        stream_play_timer = new Timer();
        stream_play_timer.schedule(new TimerTask() {
            public void run() {

                OLD_PLAY_RANDOM = LASTEST_PLAY_RANDOM;//set the stream time random value, playing the stream should change it

                try {
                    Thread.sleep(1000); //wait one seconds, for change in    random reading.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//Check if updated, or unchanged
                if (OLD_PLAY_RANDOM == LASTEST_PLAY_RANDOM) {//Not streaming voice.
                    BroadCastScreen_6.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.button11)).setImageBitmap(notStreamingVoice);
                        }
                    });
                } else {//Streaming voice.
                    BroadCastScreen_6.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.button11)).setImageBitmap(streamingVoice);
                        }
                    });
                }

            }
        }, 0, STREAM_PLAY_MONITOR_TIME);
    }

}