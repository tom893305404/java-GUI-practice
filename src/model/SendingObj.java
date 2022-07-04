package model;

import java.io.Serializable;

public class SendingObj implements Serializable {
	private String UserName =null;
	private String UUID = null;
	private Integer chessLocationX = null;
	private Integer chessLocationY = null;
	private Integer flag1;
	private Integer flag2;
	/*	flag1 & flag2
	 * 	gameover
	 * 		yes		no
	 * 	1  |___win____|___yourturn(get locate)______|
	 * 	0  |___lose___|___notyourturn(wait)___|
	 * _____________________________________
	 * 	0	0	wait
	 *	0	1	your turn 
	 *	1 	0 	you lose
	 *	1 	1	you win 
	 */
	
	public SendingObj() {
		
	}
	public SendingObj(Integer flag1,Integer flag2) {
		this.flag1 = flag1;
		this.flag2 = flag2;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getChessLocationX() {
		return chessLocationX;
	}

	public Integer getChessLocationY() {
		return chessLocationY;
	}
	public void setChessLoaction(Integer x,Integer y) {
		this.chessLocationX = x;
		this.chessLocationY = y;
	}
	public void setflag1(Integer f) {
		flag1 = f;
	}
	public void setflag2(Integer f) {
		flag2 = f;
	}
	public Integer getflag1() {
		return flag1;
	}
	public Integer getflag2() {
		return flag2;
	}
	
}
