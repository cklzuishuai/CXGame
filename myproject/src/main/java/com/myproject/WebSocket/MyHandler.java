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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myproject.Game.Game;

@ServerEndpoint("/community/{ro_user}")
@Component
public class MyHandler {
	
	//存放房间与对应的socket连接集合(用户集合)
	private static final Map<Integer, CopyOnWriteArraySet<MyHandler>> room_user = new ConcurrentHashMap<Integer, CopyOnWriteArraySet<MyHandler>>();
	//存放房间对应的牌局
	private static final Map<Integer, Game> room_game = new HashMap<Integer, Game>();
	private Session session; //Socket会话
	private String userId;  //用户ID
	private Integer roomId = 1;  //房间ID
	
	@OnOpen
	public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) {
		System.out.println(ro_user+"建立socket连接");
		this.session = session;
		this.userId = ro_user;
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
		//System.out.println(players);
		if(players.size() == 4){
			initGame();
		}
	}
	
	@OnClose    
	public void onClose() {
		System.out.println("有一连接关闭");
		CopyOnWriteArraySet<MyHandler> players = room_user.get(roomId);
	    if (players != null) {
	    	  players.remove(this);
	    }
	    //System.out.println(players);
	}
	
    @OnError    
    public void onError(Session session, Throwable error) {        
    	//log.info("发生错误" + new Date());        
    	error.printStackTrace();    
    }
    
    @OnMessage
    public void onMessage(final String message, Session session) {
    	Game game = room_game.get(roomId);
    	JSONObject jso = JSON.parseObject(message);
    	if(jso.getString("type").equals("play")){
    		game.playRound(userId,jso.getInteger("index"));
    		showPrivateHave();
    	}
    }
    
    private void showPrivateHave(){
    	Game game = room_game.get(roomId);    	
    	this.session.getAsyncRemote().sendText(game.getPlayerHave(this.userId));
    }
    
    //牌局初始化
    public void initGame(){
    	Game game = new Game();
    	room_game.put(roomId, game);
    	System.out.println("牌局初始化");
    	game.init();
        CopyOnWriteArraySet<MyHandler> players = room_user.get(roomId);        
        if (players != null) {            
        	for (MyHandler item : players) { 
        		item.session.getAsyncRemote().sendText(game.getPlayerHave(item.userId));
        	}
        	for (MyHandler item : players) {                
        		item.session.getAsyncRemote().sendText(game.getKing());
        	} 
        	for (MyHandler item : players) {    
        		if(item.userId.equals(game.getFirst())){
        			item.session.getAsyncRemote().sendText("play");
        		}
        	}
        }

    }
    

}
