package com.myproject.WebSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.myproject.Game.Game;

@ServerEndpoint("/community/{ro_user}")
@Component
public class MyHandler {
	
	//存放房间与对应的socket连接集合(用户集合)
	private static final Map<Integer, CopyOnWriteArraySet<MyHandler>> room_user = new ConcurrentHashMap<Integer, CopyOnWriteArraySet<MyHandler>>();
	//存放房间对应的牌局
	private static final Map<Integer, Game> room_game = new HashMap<Integer, Game>();
	private Session session; //Socket会话
	private Integer userId;  //用户ID
	private Integer roomId;  //房间ID
	
	@OnOpen
	public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) {
		System.out.println(ro_user+"建立socket连接");
		this.session = session;
		CopyOnWriteArraySet<MyHandler> players = room_user.get(roomId);
		if (players == null) {
			synchronized (room_user) {
				if (!room_user.containsKey(roomId)) {
					players = new CopyOnWriteArraySet<MyHandler>();
					room_user.put(roomId, players);
				}
			}
		}
		players.add(this);
		if(players.size() == 4){
			initGame();
		}
	}
	
	@OnClose    
	public void onClose() {
		System.out.println("有一连接关闭");
	}
	
    @OnError    
    public void onError(Session session, Throwable error) {        
    	//log.info("发生错误" + new Date());        
    	error.printStackTrace();    
    }
    
    @OnMessage
    public void onMessage(final String message, Session session) {
    	
    }
    
    //牌局初始化
    public void initGame(){
    	
    }

}
