package com.myproject.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Game {
	private static String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static String[] types = {"条", "筒", "万"};
	private static String[] others = { "东风", "南风", "西风", "北风", "红中", "发财", "白板" };
	
	private String king; //龙
	
	private String first="1";
	
	private String next_player="1";
	
	private String[] player = {"1", "2", "3", "4"};
    private List<Integer> player1 = new ArrayList<Integer>();  //每个玩家手中的牌
    private List<Integer> player2 = new ArrayList<Integer>();
    private List<Integer> player3 = new ArrayList<Integer>();
    private List<Integer> player4 = new ArrayList<Integer>();
    
    private List<Integer> player_deal1 = new ArrayList<Integer>();  //每个玩家打掉的牌
    private List<Integer> player_deal2 = new ArrayList<Integer>();
    private List<Integer> player_deal3 = new ArrayList<Integer>();
    private List<Integer> player_deal4 = new ArrayList<Integer>();
	
	private Integer get_index = 0; //下张要摸的牌
	
	private Integer focus;
	
	private Map<String, ArrayList<Integer>> player_have = new HashMap<String, ArrayList<Integer>>();
	
	private Map<String, ArrayList<Integer>> player_deal = new HashMap<String, ArrayList<Integer>>();
	
	private Map<Integer, String> majiang = new HashMap<Integer, String>(); //麻将牌和序号的键值对
	
	private List<Integer> majiangNumber = new ArrayList<Integer>(); //序号的列表

    public void init(){
		int index = 0;
		for (String type : types){
            for (String number : numbers)
            {
                for (int i = 0; i < 4; i++)
                {
                    // 存麻将
                    majiang.put(index, number + type);
                    // 存麻将编号
                    majiangNumber.add(index);
                    index++;
                }
            }
        }
		
	     for (String str : others){
	            for (int i = 0; i < 4; i++)
	            {
	                // 存麻将
	                majiang.put(index, str);
	                // 存麻将编号
	                majiangNumber.add(index);
	                index++;
	            }
	     }
	     
	     Collections.shuffle(majiangNumber);
	     this.deal();
	}
    
    
    //开局发牌
    public void deal(){
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                player1.add(majiangNumber.get(get_index++));
            }

            for (int j = 0; j < 4; j++)
            {
                player2.add(majiangNumber.get(get_index++));
            }

            for (int j = 0; j < 4; j++)
            {
                player3.add(majiangNumber.get(get_index++));
            }

            for (int j = 0; j < 4; j++)
            {
                player4.add(majiangNumber.get(get_index++));
            }

        }
        player1.add(majiangNumber.get(get_index));
        get_index += 4;
        player1.add(majiangNumber.get(get_index));
        get_index -= 3;
        player2.add(majiangNumber.get(get_index));
        get_index++;
        player3.add(majiangNumber.get(get_index));
        get_index++;
        player4.add(majiangNumber.get(get_index));
        get_index += 2;
        player_have.put(player[0], (ArrayList<Integer>) player1);
        player_have.put(player[1], (ArrayList<Integer>) player2);
        player_have.put(player[2], (ArrayList<Integer>) player3);
        player_have.put(player[3], (ArrayList<Integer>) player4);
        
        player_deal.put(player[0], (ArrayList<Integer>) player_deal1);
        player_deal.put(player[1], (ArrayList<Integer>) player_deal2);
        player_deal.put(player[2], (ArrayList<Integer>) player_deal3);
        player_deal.put(player[3], (ArrayList<Integer>) player_deal4);
        
        getKing(get_index);
        get_index++;
        System.out.println("发了" + get_index + "张牌");
        Collections.sort(player1);
        Collections.sort(player2);
        Collections.sort(player3);
        Collections.sort(player4);
        System.out.println(player1);
        System.out.println(player2);
        System.out.println(player3);
        System.out.println(player4);
    }
    
    public void getKing(Integer n){
    	Integer index = majiangNumber.get(n);
    	String fake_king = majiang.get(index);
    	for(int i =0;i<numbers.length;i++){
    		if(fake_king.charAt(0)==numbers[i].charAt(0)){
    			if(i != numbers.length-1){
    				king = numbers[++i] + fake_king.charAt(1);
    			}else{
    				king = numbers[0] + fake_king.charAt(1);
    			}
    		}
    	}
    	for(int i =0;i<others.length;i++){
    		if(fake_king.charAt(0)==others[i].charAt(0)){
    			if(i == others.length-1 ){
    				king = others[4];
    			}else if(i == others.length-4){
    				king = others[0];
    			}else{
    				king = others[++i];
    			}
    		}
    	}
    	System.out.println("翻出的牌是:"+fake_king);
    	System.out.println("龙是:"+king);
    }
    
    public String getPlayerHave(String player){
        //遍历List集合,获取元素,作为键,到集合Map中找值
        System.out.print(player + " " + "共有" + player_have.get(player).size() + "张牌:  ");
        //System.out.println(player_have.get(player));
        Map<Integer,String> result = new HashMap<Integer,String>();
        for (Integer key : player_have.get(player))
        {
            String value = majiang.get(key);
            //System.out.print("[" + value + "] ");
            result.put(key, value);
        }
        //System.out.println(result);
        return JSON.toJSONString(result);
    }
    
    
    //打印所有牌
    public void getRestAll(){
    	Set<Entry<Integer, String>> entrySet = majiang.entrySet();
        for (Entry<Integer, String> entry : entrySet)
        {
            System.out.print("[" + entry.getKey() + ":" + entry.getValue() + "]");
        }
        System.out.println();
        for (Integer mInteger : majiangNumber)
        {
            System.out.print("[" + mInteger + ":" + majiang.get(mInteger) + "]");
        }
        System.out.println();
    }
    
    
    //出牌
    public void playRound(String player,Integer index){
    	player_have.get(player).remove(index);
    	focus = index;
    	player_deal.get(player).add(index);
    	System.out.println(player+"出牌"+":"+index+"-"+majiang.get(index));
    	System.out.println(player_have.get(player));
    }
    
    public void getRound(String player){
    	player_have.get(player).add(get_index);
    	System.out.println(player+"摸牌"+":"+majiangNumber.get(get_index)+"-"+majiang.get(majiangNumber.get(get_index)));
    	get_index++;
    	System.out.println(player_have.get(player));
    }
    
    
    public String getKing(){
    	return king;
    }
    
    public String getFirst(){
    	return first;
    }
    
    public String getNextPlayer(){
    	return next_player;
    }
    
    public void setNextPlayer(String now_player){
       	int i = Integer.valueOf(now_player);
    	if(i == 4){
    		next_player = "1";
    	}else{
    		i++;
    		next_player = String.valueOf(i);
    	}
    }
    
    
    public boolean getOperationPeng(String player,Integer index){
    	String s = majiang.get(index);
    	int count = 0;
    	for(Integer key : player_have.get(player)){
    		if(majiang.get(key).equals(s)){
    			count++;
    		}
    	}
    	if(count == 2){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public void Peng(String player){
    	int count = 0;
    	for(Integer key : player_have.get(player)){
    		if(majiang.get(key).equals(focus)){
    			player_have.remove(key);
    			count++;
    		}
    		if(count == 2){
    			break;
    		}
    	}
    }
    
    public boolean getOperationGang(String player,Integer index){
    	String s = majiang.get(index);
    	int count = 0;
    	for(Integer key : player_have.get(player)){
    		if(majiang.get(key).equals(s)){
    			count++;
    		}
    	}
    	if(count == 3){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean getOperationGangSelf(String player){
    	JSONArray result = new JSONArray();
    	int flag = 0;
    	for(Integer key1 : player_have.get(player)){
    		int count = 0;
    		for(Integer key2 : player_have.get(player)){
    			if(majiang.get(key1).equals(majiang.get(key2))){
    				count++;
    			}
    		}
    		if(count==4){
    			JSONObject jso = new JSONObject();
    			jso.put(String.valueOf(key1),"true");
    			flag++;
    			result.add(jso);
    		}
    	}
    	if(flag == 0){
    		return false;
    	}else{
    		return true;
    	}
    }
    
    public static void main(String[] args){
    	Game game = new Game();
    	System.out.println(game.getNextPlayer());
    }
    
    
 
}
