package com.polytech.stfu.ihm;


import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.support.v4.content.LocalBroadcastManager;

import com.polytech.stfu.jeu.Jeu;
import com.polytech.stfu.score.Score;

import java.util.SortedSet;

import static com.polytech.stfu.score.Score.getHighScoreList;


public class GameActivity extends Activity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private GameReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TetrisView tetrisView = new TetrisView(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(tetrisView);

        receiver = new GameReceiver(this,tetrisView);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("TETRIS"));

        Jeu.getJeu().startGame();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"haha onResume");
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("TETRIS"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    // EVENTS

    @Override
    public void onBackPressed() {
        if(Jeu.getJeu().isInPause()){
            sendGameUnpause();
        }
        else{
            sendGamePause();
            this.showScoresDialog();
        }
    }




    // DIALOG BOX
    public void showPauseDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("Menu pause");


        adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                sendGameUnpause();
            }
        });

        //On modifie l'ic�ne de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_dialog_alert);

        adb.setPositiveButton("Reprendre", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendGameUnpause();

                //Lorsque l'on cliquera sur le bouton reprendre on reprendra une partie
            }
        });
        adb.setNeutralButton("Rejouer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur le bouton rejouer on recommencera une partie
                sendGameRestart();
            }
        });
        adb.setNegativeButton("Retourner au menu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur Retourner au menu on retourna a la page du menu principal
                //Intent mainMenuActivity = new Intent(GameActivity.this, mainMenuActivity.class);
                //startActivity(mainMenuActivity);
                finish();
            }
        });
        /*adb.setOnDismissListener(new DialogInterface.OnDismissListener(){

        });*/
        adb.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == 4)
                    sendGameUnpause();
                return false;
            }
        });

        adb.show();
    }
    public void showScoresDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dialog_highscores, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);

        //On affecte la vue personnalise que l'on a cree ? notre AlertDialog
        adb.setView(alertDialogView);
        adb.setTitle("Tableau des scores");

        /*
        //On modifie l'icone de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        SortedSet<Couple> highscores = getHighScoreList(Jeu.getJeu().getMode(),this);

        TextView textName1 = (TextView)alertDialogView.findViewById(R.id.highscore_name1);
        TextView textName2 = (TextView)alertDialogView.findViewById(R.id.highscore_name2);
        TextView textName3 = (TextView)alertDialogView.findViewById(R.id.highscore_name3);
        TextView textName4 = (TextView)alertDialogView.findViewById(R.id.highscore_name4);
        TextView textName5 = (TextView)alertDialogView.findViewById(R.id.highscore_name5);
        TextView tabNames[] = {textName1,textName2,textName3,textName4,textName5};

        TextView textScore1 = (TextView)findViewById(R.id.highscore_score1);
        TextView textScore2 = (TextView)findViewById(R.id.highscore_score2);
        TextView textScore3 = (TextView)findViewById(R.id.highscore_score3);
        TextView textScore4 = (TextView)findViewById(R.id.highscore_score4);
        TextView textScore5 = (TextView)findViewById(R.id.highscore_score5);
        TextView tabScores[] = {textScore1,textScore2,textScore3,textScore4,textScore5};

        int i = 0;
        for(Couple c : highscores) {
            tabNames[i].setText(c.getPseudo());
            tabScores[i].setText(String.valueOf(c.getScore()));
            i++;
        }

*/

        //On cr2e un bouton "Rejour" ? notre AlertDialog et on lui affecte un ?v?nement
        adb.setPositiveButton("Rejouer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur rejouer une partie est relanc?e
                sendGameRestart();
            }
        });
        adb.setNeutralButton("Retourner au menu",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur rejouer une partie est relanc?e
                finish();
            }
        });
        adb.show();
    }
    public void showNewScoreDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dialog_new_score, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalis? que l'on a cr?e ? notre AlertDialog
        adb.setView(alertDialogView);
        adb.setTitle("Nouveau meilleur score");

        //On modifie l'ic?ne de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_dialog_alert);

        //On affecte un bouton "OK" ? notre AlertDialog et on lui affecte un ?v?nement
        adb.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on r?cup?re l'EditText correspondant ? notre vue personnalis?e (cad ? alertDialogView)
                EditText et = (EditText)alertDialogView.findViewById(R.id.player_pseudo);
                Score.save(Jeu.getJeu().getMode(), et.getText().toString(), Jeu.getJeu().getScore(), GameActivity.this);

            }
        });
        adb.show();
    }

    private void sendGameRestart(){
        Intent intent = new Intent("TETRIS");
        intent.putExtra("Source", "Ihm");
        intent.putExtra("Action", R.string.GAME_RESTART);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendGamePause(){
        Intent intent = new Intent("TETRIS");
        intent.putExtra("Source", "Ihm");
        intent.putExtra("Action", R.string.GAME_PAUSE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendGameUnpause(){
        Intent intent = new Intent("TETRIS");
        intent.putExtra("Source", "Ihm");
        intent.putExtra("Action", R.string.GAME_UNPAUSE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
