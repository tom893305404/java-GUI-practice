package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import model.Checkerboard;

public class gameThread implements Runnable {
	private class PLAYER{
		ObjectInputStream InStream;
		ObjectOutputStream OutStream;
		PLAYER(ObjectInputStream in,ObjectOutputStream out){
			InStream = in;
			OutStream = out;
		}
	}
	private Socket playerSocket1;
	private Socket playerSocket2;
	private Checkerboard board;
	private Integer Turnner;
	private PLAYER player1;
	private PLAYER player2;
	
	gameThread(Socket _player1,Socket _player2){
		//System.out.println("in gameThread Constructor");
		playerSocket1 = _player1;
		playerSocket2 = _player2;
		try {
			player1 = new PLAYER(new ObjectInputStream(_player1.getInputStream()),
						new ObjectOutputStream(_player1.getOutputStream()));
			player2 = new PLAYER(new ObjectInputStream(_player2.getInputStream()),
						new ObjectOutputStream(_player2.getOutputStream()));
		} catch (IOException e) {
			System.out.println("at gameThread.java IOException...");
			e.printStackTrace();
		}
		//
		board = new Checkerboard();
		Turnner = model.Checkerboard.BLACK;//黑子先手
		
	}
	@Override
	public void run() { //負責遊戲的進程
		System.out.println("In gameThread.run()");
		//告訴兩個客戶端他們配對到了
		try {
			player1.OutStream.writeUTF("Matched");
			player2.OutStream.writeUTF("Matched");
			
			var obj1 = new model.SendingObj(0, 1); //your turn
			var obj2 = new model.SendingObj(0, 0);// wait
			//var obj3 = new model.SendingObj(1, 0);//lose
			//var obj4 = new model.SendingObj(1, 1);//win

			while(true) {
				//預設player1 先手
				player1.OutStream.writeObject(obj1);//your turn
				player2.OutStream.writeObject(obj2);//wait
				
				while(true) {//wait player1 echo
					if (player1.InStream.available() != 0)break;
				}
				//read the chessman and place (player1)
				model.SendingObj echo1 = (model.SendingObj)player1.InStream.readObject();
				var x = echo1.getChessLocationX();
				var y = echo1.getChessLocationY();
				var accept = board.placechessman(this.Turnner, x, y);
				if (accept == 0) {//no problem
					if (board.gameOver() == 1) {break;}
					this.Turnner();
				}else if (accept == 1){//his position has been place
					//要回傳告訴客戶端放置失敗 要求重傳位置
				}
				
				//
				model.SendingObj echo2 = (model.SendingObj) player2.InStream.readObject();
				
				
				
			}
			//結束的流程(向雙方傳送結束信號，並告訴誰贏了)(this.Tunner的顏色是贏的顏色)
			if (this.Turnner == model.Checkerboard.BLACK) {
				win(player1,player2);
			}else {
				win(player2,player1);
			}
			
			
		} catch (IOException e) {
			System.out.println("IOException Error in run()");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException in run()");
		}
		
		
		
	}
	/*private void _placechessman(PLAYER player) throws ClassNotFoundException, IOException {
		model.SendingObj echo1 = (model.SendingObj)player.InStream.readObject();
		var x = echo1.getChessLocationX();
		var y = echo1.getChessLocationY();
		board.placechessman(this.Turnner, x, y);
		if (board.gameOver() == 1) {break;}
		this.Turnner();
	}*/
	private void win(PLAYER winner,PLAYER loser) throws IOException {
		var objwin = new model.SendingObj(1, 1);//win
		var objlose = new model.SendingObj(1,0);//lose
		winner.OutStream.writeObject(objwin);
		loser.OutStream.writeObject(objlose);
	}
	public void Turnner() { //單機使用的黑白棋轉子
		if (Turnner == model.Checkerboard.BLACK) {
			Turnner = model.Checkerboard.WHITE;
		} else {
			Turnner = model.Checkerboard.BLACK;
		}
	}

}
