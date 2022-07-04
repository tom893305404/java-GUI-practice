package client;

import java.awt.*;
import javax.swing.*;

import model.SendingObj;
import model.Checkerboard;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

public class client {
	private ClientGui gui;
	private Checkerboard board;
	private Integer Turner;

	private String serverName = "192.168.0.11";
    private int serverPort = 8081;
    private Socket socket = null;
	client() {
		board = new Checkerboard();
		gui = new ClientGui(this);
		Turner = model.Checkerboard.BLACK;// 黑子先手
	}

	public static void main(String[] args) {
		new client();
	}
	public void renewBoard() { //更新visual board
		board = new Checkerboard();
	}

	public Checkerboard getBoard() { //取得visual board
		return board;
	}

	public Integer getTurner() { //取得轉子
		return Turner;
	}

	public void Turnner() { //單機使用的黑白棋轉子
		if (Turner == model.Checkerboard.BLACK) {
			Turner = model.Checkerboard.WHITE;
		} else {
			Turner = model.Checkerboard.BLACK;
		}
	}
    public void connect() throws UnknownHostException, IOException { //Asking to connect the server
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected to server " + socket.getRemoteSocketAddress());
       
    }
    public Socket getSocket() {
    	return this.socket;
    }
    public void disconnect() {
        try {
            //objectOutputStream.close();
            //objectInputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

}


