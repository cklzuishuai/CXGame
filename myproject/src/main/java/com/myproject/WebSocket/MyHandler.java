package com.myproject.WebSocket;

import java.io.IOException;
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
	public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) throws IOException {
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
		//System.out.println("当前在线玩家"+players);
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
	    //System.out.println("当前在线玩家"+players);
	}
	
    @OnError    
    public void onError(Session session, Throwable error) {        
    	//log.info("发生错误" + new Date());        
    	error.printStackTrace();    
    }
    
    @OnMessage
    public void onMessage(final String message, Session session) throws IOException {
    	Game game = room_game.get(roomId);
    	JSONObject jso = JSON.parseObject(message);
    	if(jso.getString("type").equals("play")){
    		game.playRound(userId,jso.getInteger("index"));
    		game.setNextPlayer(userId);
    		showPrivateHave();
    		CopyOnWriteArraySet<MyHandler> players = room_user.get(roomId);
    		int flag = 0; //0:无吃碰
    		if (players != null) {
    			for (MyHandler item : players) {
    				if(item != this){
    					JSONObject result = new JSONObject();
    					int flag2 = 0;
    					if(game.getOperationGang(item.userId,jso.getInteger("index"))){
    						result.put("gang","true");
    						flag2 = 1;
    						flag = 1;
    					}
    					if(game.getOperationPeng(item.userId,jso.getInteger("index"))){
    						result.put("peng","true");
    						flag2 = 1;
    						flag = 1;
    					}
    					if(flag2 == 1){
    						item.session.getBasicRemote().sendText(result.toJSONString());
    					}
    				}
    			}
    			if(flag == 0){
    				System.out.println("没人吃碰,下家开始");
    				for (MyHandler item : players) {
    					if(item.userId.equals(game.getNextPlayer())){
    						game.getRound(item.userId);
            				JSONObject result = new JSONObject();
            				result.put("play", "true");
    						item.session.getBasicRemote().sendText(result.toJSONString());
    					}
    				}
    			}
    		}	
    	}// end of type play
    	
    	if(jso.getString("type").equals("peng")){
    		game.setNextPlayer(userId);
    		game.Peng(userId);
    		
    	}// end of type peng
    	
    	
    }
    
    private void showPrivateHave(){
    	Game game = room_game.get(roomId);    	
    	this.session.getAsyncRemote().sendText(game.getPlayerHave(this.userId));
    }
    
    //牌局初始化
    public void initGame() throws IOException{
    	Game game = new Game();
    	room_game.put(roomId, game);
    	System.out.println("牌局初始化");
    	game.init();
        CopyOnWriteArraySet<MyHandler> players = room_user.get(roomId);        
        if (players != null) {            
        	for (MyHandler item : players) { 
        		item.session.getBasicRemote().sendText(game.getPlayerHave(item.userId));
        	}
        	for (MyHandler item : players) {                
        		item.session.getBasicRemote().sendText(game.getKing());
        	} 
        	for (MyHandler item : players) {    
        		if(item.userId.equals(game.getFirst())){
        				JSONObject result = new JSONObject();
        				result.put("play","true");
        				result.put("gangself", game.getOperationGangSelf(item.userId));
        				item.session.getBasicRemote().sendText(result.toJSONString());
        		}
        	}
        }

    }
    

}
