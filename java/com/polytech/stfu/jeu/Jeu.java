package com.polytech.stfu.jeu;

import android.database.CrossProcessCursor;
import android.os.Parcelable;

public abstract class Jeu extends Thread{
	protected Vitesse vitesse;
	protected Acceleration acceleration;
	protected Grille grille;
	protected TypePiece piece;
	protected int intervalTime;
	private Mode mode;
	
	protected boolean pause;
	protected Object lockPause;
	
	private static Jeu jeu;
	
	public Jeu(){
		pause = false;
		lockPause = new Object();
		
		vitesse = Vitesse.NORMALE;
		acceleration = Acceleration.MODEREE;
		grille = new Grille();
		intervalTime = 500 * vitesse.getVal()/100;
		
		Jeu.jeu = this;
	}
	
	public static Jeu getJeu(){
		return jeu;
	}
	
	public void move(TypeMove move){
		if(grille.canMovePiece(move)){
			grille.movePiece(move);
		}
	}

	public void rotate(){
		if(grille.canRotatePiece()) {
			grille.rotatePiece();
		}
	}
	
	public void down(){
		while(grille.canMovePiece(TypeMove.DOWN)){
			grille.movePiece(TypeMove.DOWN);
		}
	}
	
	public TypePiece getTypeNextPiece(){
		return piece;
	}
	
	public void run(){
		piece = createFuturPiece();
		while(true){
			lockPause();
			try {
				sleep(intervalTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(grille.canMovePiece(TypeMove.DOWN)){
				grille.movePiece(TypeMove.DOWN);
			}
			else{
				updateScore(grille.removeLines());
				if(isFinish()){
					break;
				}
				piece = createFuturPiece();
				intervalTime *= 0.01 * acceleration.getVal();
			}
		}
	}
	
	public void startGame(){
		start();
	}
	
	public void pause(){
		synchronized (lockPause) {
			pause = true;
		}
	}
	
	public void restart(){
		synchronized (lockPause) {
			pause = false;
		}
	}
	
	public void end(){
		
	}
	
	protected void lockPause(){
		while(pause){
			yield();
		}
	}
	
	protected abstract boolean isFinish();
	
	protected abstract void updateScore(int line);
	
	protected TypePiece createFuturPiece(){
		int type = (int)(Math.random()*7);
		Point pointInitial = new Point(5,0);
		Piece newPiece;
		switch(type){
		case 0:
			newPiece = Piece.createPieceI(pointInitial);
			break;
		case 1:
			newPiece = Piece.createPieceJ(pointInitial);
			break;
		case 2:
			newPiece = Piece.createPieceL(pointInitial);
			break;
		case 3:
			newPiece = Piece.createPieceO(pointInitial);
			break;
		case 4:
			newPiece = Piece.createPieceS(pointInitial);
			break;
		case 5:
			newPiece = Piece.createPieceT(pointInitial);
			break;
		case 6:
			newPiece = Piece.createPieceZ(pointInitial);
			break;
		default:
			newPiece = Piece.createPieceI(pointInitial);
		}
		piece = newPiece.getTypePiece();
		grille.setNewPiece(newPiece);
		return piece;
	}
	
	public abstract int getScore();

	public Vitesse getVitesse() {
		return vitesse;
	}

	public void setVitesse(Vitesse vitesse) {
		this.vitesse = vitesse;
	}

	public Acceleration getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Acceleration acceleration) {
		this.acceleration = acceleration;
	}
	
	public TypePiece[][] getGrille(){
		return grille.getPlateau();
	}
	
	public TypePiece getTypePiece(){
		return piece;
	}
	
	public Mode getMode(){
		return mode;
	}
	
	public void aff(){
		System.out.println(grille);
	}
}