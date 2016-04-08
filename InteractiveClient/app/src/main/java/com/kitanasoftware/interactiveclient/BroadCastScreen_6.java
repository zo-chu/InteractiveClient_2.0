package com.kitanasoftware.interactiveclient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.kitanasoftware.interactiveclient.dataTransfer.WifiUtility;


public class    BroadCastScreen_6 extends DrawerAppCompatActivity {
    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.broad_cast_screen_6,null);
    }
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f8bfd8"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Check if device is connected to hotspot.
        if (!WifiUtility.isWifiConnected(this)) {
            if (!WifiUtility.isHotSpot(this)) {
                return;
            }
        }

        initializeApp();

        //Only open new threads, if null
        if (playStream == null) {
            playStream = (PlayStream) new PlayStream().execute();
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


    public void initializeApp() {

        streamingVoice = BitmapFactory.decodeResource(getResources(), R.drawable.audio);
        notStreamingVoice = BitmapFactory.decodeResource(getResources(), R.drawable.audio_mute);

        ((TextView) findViewById(R.id.item)).setText("Device IP Address: " + WifiUtility.getIpAddress());

        ((Button) findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.VISIBLE);
                        sendStream();
                    }
                }, 50);
            }
        });
        ((Button) findViewById(R.id.stop)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.VISIBLE);
                        stopStream();
                    }
                }, 50);
            }
        });

    }

    public void stopStream() {
        status_sending = false;
        for (int i = 0; i < recorders.size(); i++) {
            if (recorders.get(i) != null) {
                recorders.get(i).release();
            }
        }
    }

    public void sendStream() {
        status_sending = true;
        for (int i = 0; i < devices.size(); i++) {
            String device_ip = devices.get(i);
            sendStream(device_ip);
        }
    }

    protected void show(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }


    //Listen for voice streams and play.
    public class PlayStream extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }

        ;

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


    //Send voice stream to a specific device on the network.
    public void sendStream(final String device_ip) {


        Thread sendStreamThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    int MIN_BUFFER_SIZE = 8192;

                    @SuppressWarnings("resource")
                    //Socket Created
                            DatagramSocket socket = new DatagramSocket();

                    byte[] buffer = new byte[MIN_BUFFER_SIZE];

                    DatagramPacket packet;

                    //Destination Address retrieved.
                    final InetAddress destination = InetAddress.getByName(device_ip);

                    //Initialize Recorder
                    AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            RECORDER_SAMPLE_RATE,
                            RECORDER_CHANNELS,
                            RECORDER_AUDIO_ENCODING,
                            MIN_BUFFER_SIZE * 5);

                    //add to array
                    recorders.add(recorder);
                    recorder.startRecording();

                    while (status_sending == true) {

                        //reading data from MIC into buffer
                        MIN_BUFFER_SIZE = recorder.read(buffer, 0, buffer.length);

                        //putting buffer in the packet
                        packet = new DatagramPacket(buffer, buffer.length, destination, VOICE_STREAM_PORT);

                        //Send packet
                        socket.send(packet);
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        sendStreamThread.start();
    }


    //Listen to UDP broadcast and update ip list.
    public void ListenForBroadcasts() {
        listenForBroadcastsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Keep socket open to listen to all the UDP traffic that is destined for this port.
                    socket = new DatagramSocket(BROADCAST_IP_PORT, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);

                    while (true) {

                        //Receive a packet
                        byte[] buffer = new byte[15000];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        final String sender_ip = packet.getAddress().getHostAddress();
                        String device_ip = WifiUtility.getIpAddress();

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
                                if (status_sending) {//stream to the new device
                                    sendStream(device_ip);
                                }

                                //Add device ip to spinner
                                BroadCastScreen_6.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ArrayAdapter<String> ad = new ArrayAdapter<String>(BroadCastScreen_6.this,
                                                android.R.layout.simple_spinner_item,
                                                devices.toArray(new String[devices.size()]));
                                        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                        ((Spinner) findViewById(R.id.target_ips)).setAdapter(null);
                                        ((Spinner) findViewById(R.id.target_ips)).setAdapter(ad);
                                        ad.notifyDataSetChanged();
                                    }
                                });
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


    //Broadcast this device IP address to other devices on this network.
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


    //Send UDP broadcast every 5 seconds.
    public void sendBroadcast() {
        broadcast_timer = new Timer();
        broadcast_timer.schedule(new TimerTask() {
            public void run() {
                broadcastIp("Ping");
            }
        }, 0, BROADCAST_TIME);
    }

    //Hack
    //Determine if stream is being played or not.
    public void monitorStreamPlay() {
        stream_play_timer = new Timer();
        stream_play_timer.schedule(new TimerTask() {
            public void run() {

                OLD_PLAY_RANDOM = LASTEST_PLAY_RANDOM;//set the stream time random value, playing the stream should change it

                try {
                    Thread.sleep(1000); //wait one seconds, for change in random reading.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Check if updated, or unchanged
                if (OLD_PLAY_RANDOM == LASTEST_PLAY_RANDOM) {//Not streaming voice.
                    BroadCastScreen_6.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.indicator)).setImageBitmap(notStreamingVoice);
                        }
                    });
                } else {//Streaming voice.
                    BroadCastScreen_6.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.indicator)).setImageBitmap(streamingVoice);
                        }
                    });
                }

            }
        }, 0, STREAM_PLAY_MONITOR_TIME);
    }
}