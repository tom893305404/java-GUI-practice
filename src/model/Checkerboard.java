package model;


public class Checkerboard {
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;
	private Integer [][] Board;
	//0->color, 1->x, 2->y
	private Integer [] LastPlace = new Integer[3];

	public Checkerboard(){
		Board = new Integer[15][15];
		for(var i=0;i<15;i++) {
			for(var j=0;j<15;j++) {
			Board[i][j]=Checkerboard.EMPTY;
			}
		}
		
	}
	public Integer placechessman(int color,int x,int y) {
		if(Board[x][y] == Checkerboard.EMPTY) {
					Board[x][y] = color;
					LastPlace[0] = color;
					LastPlace[1] = x;
					LastPlace[2] = y;
					return 0;
		}else {
			System.out.println("this position has been place");
			return 1;
			}
	}
	private void printBoard() {
		for(var i=0;i<15;i++) {
			for(var j=0;j<15;j++) {
				System.out.printf("%d",Board[j][i]);
			}
			System.out.println();
		}
		System.out.println("--------------------------------------------");
	}
	public Integer gameOver(){ //檢查遊戲是否結束
		var color = LastPlace[0];
		var x = LastPlace[1];
		var y = LastPlace[2];
		if(x == null && y == null) {//一開始會有null
			return -1;
		}
		var count0  = 0; // |
		var count00 = 0;
		var count1  = 0; // 一
		var count11 = 0;
		var count2  = 0; // /
		var count22 = 0;
		var count3  = 0; // \
		var count33 = 0;
		for(int i=0;i<5;i++)
		{
			
		// | 
		//下	
			if(y+i < 15) {
				if(Board[x][y+i] == color) count0++;
				if(count0 == 5)return 1;
			}
		//上	
			if(y-i >= 0) {
				if(Board[x][y-i] == color) count00++;
				if(count00 == 5)return 1;
			}
		// 一
		//左
			if(x+i<15) {
				if(Board[x+i][y] == color) count1++;
				if(count1 == 5)return 1;
			}
		//右	
			if(x-i >=0) {
				if(Board[x-i][y] == color) count11++;
				if(count11 == 5)return 1;
			}
			
		//右上
			if(x+i < 15 && y-i >= 0) {
				if(Board[x+i][y-i] == color) count2++;
				if(count2 == 5)return 1;
			}
		//左下
			if(x-i >=0 && y+i < 15) {
				if(Board[x-i][y+i] == color) count22++;
				if(count22 == 5)return 1;
			}
		// \
		//左上
			if(x-i >= 0 && y-i >= 0) {
				if(Board[x-i][y-i] == color) count3++;
				if(count3 == 5)return 1;
			}
		//右下
			if(x+i < 15 &&  y+i < 15) {
				if(Board[x+i][y+i] == color) count33++;
				if(count33 == 5)return 1;
			}
			
		}
		printBoard();
		return 0;
	}
}
