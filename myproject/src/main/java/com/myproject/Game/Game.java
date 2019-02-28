package com.myproject.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

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
	
	private Integer last_index = 135; //杠后摸的牌
	
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
        Map<Integer,String> result = new TreeMap<Integer,String>();
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
    
    public void getAfterGang(String player){
    	player_have.get(player).add(last_index);
    	System.out.println(player+"摸牌"+":"+majiangNumber.get(last_index)+"-"+majiang.get(majiangNumber.get(last_index)));
    	last_index--;
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
    			player_have.get(player).remove(key);
    			count++;
    		}
    		if(count == 2){
    			break;
    		}
    	}
    }
    
    public void Gang(String player){
    	int count = 0;
    	for(Integer key : player_have.get(player)){
    		if(majiang.get(key).equals(focus)){
    			player_have.get(player).remove(key);
    			count++;
    		}
    		if(count == 3){
    			break;
    		}
    	}
    }
    
    public void Eat(String player,String eattype){
    	if(eattype.equals("eat12x")){
    		int front1;
        	int front2;
        	String focus_name = majiang.get(this.focus);
        	int i = focus_name.charAt(0)-'0';
        	front1 = --i;
    		front2 = --i;
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(front1)+focus_name.charAt(1))){
    				player_have.get(player).remove(key);
    				break;
    			}
    		}
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(front2)+focus_name.charAt(1))){
    				player_have.get(player).remove(key);
    				break;
    			}
    		}
    	}
    	if(eattype.equals("eat1x3")){
    		int front;
        	int after;
        	String focus_name = majiang.get(this.focus);
        	int i = focus_name.charAt(0)-'0';
    		front = --i;
    		i = i+2;
    		after = i;
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(front)+focus_name.charAt(1))){
    				player_have.get(player).remove(key);
    				break;
    			}
    		}
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(after)+focus_name.charAt(1))){
    				player_have.get(player).remove(key);
    				break;
    			}
    		}
    	}
    	if(eattype.equals("eatx23")){
    		int after1;
        	int after2;
        	String focus_name = majiang.get(this.focus);
        	int i = focus_name.charAt(0)-'0';
    		after1 = ++i;
    		after2 = ++i;
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(after1)+focus_name.charAt(1))){
    				player_have.get(player).remove(key);
    				break;
    			}
    		}
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(after2)+focus_name.charAt(1))){
    				player_have.get(player).remove(key);
    				break;
    			}
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
    
    
    public String getOperationEat(String player){
    	for(String key : others){      //风头直接返回false
    		if(key.equals(majiang.get(focus))){
    			return "false";
    		}
    	}
    	JSONObject result = new JSONObject();
    	int flag = 0;
    	if(eat12x(player)){
    		result.put("eat12x", "true");
    		flag = 1;
    	}
    	if(eat1x3(player)){
    		result.put("eat1x3", "true");
    		flag = 1;
    	}
    	if(eatx23(player)){
    		result.put("eatx23", "true");
    		flag = 1;
    	}
    	if(flag == 0){
    		return "false";
    	}else{
    		return result.toString();
    	}
    }
    
    
    public boolean getOperationWin(String player){
    	if(majiang.get(focus).equals(king)){
    		return false;
    	}
    	if(isPaoHua(player)){
    		return false;
    	}
    	ArrayList<Integer> list = new ArrayList<Integer>();  //copy一份玩家手中的牌
    	list.addAll(player_have.get(player));
    	list.add(focus);
    	Collections.sort(list);
    	if(list.size() == 2){
    		if(majiang.get(list.get(0)).equals(majiang.get(list.get(1)))){
    			return true;
    		}
    		return false;
    	}
    	if(list.size() == 14){
    		return false;
    	}
    	return false;
    }
    
    public boolean isPaoHua(String player){
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	list.addAll(player_have.get(player));
    	Collections.sort(list);
    	HashSet<String> hs_dui = getDui(list);
    	if(list.size() == 1){
    		if(majiang.get(list.get(0)).equals(king)){
    			return true;
    		}
    		return false;
    	}
    	if(hs_dui.size()>0){
    		for(String dui : hs_dui){
    			ArrayList<Integer> tmp = new ArrayList<Integer>();
    			tmp.addAll(list);
    			Collections.sort(tmp);
    			int kingcount=0;
    			int count =0;
    			int count2=0;
    			int count3=0;
    			for(Integer key : list ){
    				int num = 0;
    				if(majiang.get(key).equals(dui)){
    					list.remove(key);
    					num++;
    				}
    				if(num == 2){
    					break;
    				}
    			}
    			for(Integer key : list ){
    				if(majiang.get(key).equals(king)){
    					list.remove(key);
    					kingcount++;
    				}
    			}
    			for(Integer key : list ){
    				if(majiang.get(key).equals(king)){
    					list.remove(key);
    					kingcount++;
    				}
    			}
    			for(int i=0;i<list.size()-2;i++){                        //跳过克子和对子判断顺子
    				HashSet<String> hs_kz = getKeZi(list);
    				HashSet<String> hs_ji = getJiang(list);
    				int j = majiang.get(list.get(i)).charAt(0)-'0';
    				if(j<8){
    					int after1 = i+1;
    					int after2 = i+2;
    					boolean flag1 = false;
    					boolean flag2 = false;
    					Integer key1 = -1;
    					Integer key2 = -1;
    					for(Integer key : list){
    						if(hs_kz.contains(majiang.get(key)) && hs_ji.contains(majiang.get(key))){
    							break;
    						}
    						if(majiang.get(key).equals(String.valueOf(after1)+majiang.get(list.get(i)).charAt(1))){
    							flag1 = true;
    							key1 = key;
    						}
    						else if(majiang.get(key).equals(String.valueOf(after2)+majiang.get(list.get(i)).charAt(1))){
    							flag2 = true;
    							key2 = key;
    						}
    						if(flag1 && flag2){
    							list.remove(key1);
    							list.remove(key2);
    							count3++;
    							i=-1;
    							break;
    						}
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
    
    
    public HashSet<String> getKeZi(List list){
    	HashSet<String>  hs = new HashSet<String>();
    	for(int i=0;i<list.size()-2;i++){
    		int after1 = i+1;
    		int after2 = i+2;
    		int after3 = i+3;
    		if(majiang.get(list.get(i)).equals(majiang.get(list.get(after1))) && majiang.get(list.get(i)).equals(majiang.get(list.get(after2))) && !majiang.get(list.get(i)).equals(majiang.get(list.get(after3)))){
    			hs.add(majiang.get(list.get(i)));
    		}
    	}
    	return hs;
    }
    
    public HashSet<String> getJiang(List list){
    	HashSet<String>  hs = new HashSet<String>();
    	for(int i=0;i<list.size()-1;i++){
    		int after1 = i+1;
    		int after2 = i+2;
    		if(majiang.get(list.get(i)).equals(majiang.get(list.get(after1))) && !majiang.get(list.get(i)).equals(majiang.get(list.get(after2)))){
    			hs.add(majiang.get(list.get(i)));
    		}
    	}
    	return hs;
    }
    
    public HashSet<String> getDui(List list){
    	HashSet<String>  hs = new HashSet<String>();
    	for(int i=0;i<list.size()-1;i++){
    		int after1 = i+1;
    		int after2 = i+2;
    		if(majiang.get(list.get(i)).equals(majiang.get(list.get(after1)))){
    			hs.add(majiang.get(list.get(i)));
    		}
    	}
    	return hs;
    }
    
    
    public boolean eat12x(String player){
    	boolean flag1 = false;
    	boolean flag2 = false;
    	int front1;
    	int front2;
    	String focus_name = majiang.get(this.focus);
    	int i = focus_name.charAt(0)-'0';
    	if(i < 3){
    		return false;
    	}else{
    		front1 = --i;
    		front2 = --i;
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(front1)+focus_name.charAt(1))){
    				flag1 = true;
    			}
    			else if(majiang.get(key).equals(String.valueOf(front2)+focus_name.charAt(1))){
    				flag2 = true;
    			}
    			if(flag1 && flag2){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    public boolean eat1x3(String player){
    	boolean flag1 = false;
    	boolean flag2 = false;
    	int front;
    	int after;
    	String focus_name = majiang.get(this.focus);
    	int i = focus_name.charAt(0)-'0';
    	if(i < 2 || i>8){
    		return false;
    	}else{
    		front = --i;
    		i = i+2;
    		after = i;
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(front)+focus_name.charAt(1))){
    				flag1 = true;
    			}
    			else if(majiang.get(key).equals(String.valueOf(after)+focus_name.charAt(1))){
    				flag2 = true;
    			}
    			if(flag1 && flag2){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    public boolean eatx23(String player){
    	boolean flag1 = false;
    	boolean flag2 = false;
    	int after1;
    	int after2;
    	String focus_name = majiang.get(this.focus);
    	int i = focus_name.charAt(0)-'0';
    	if(i > 7){
    		return false;
    	}else{
    		after1 = ++i;
    		after2 = ++i;
    		for(Integer key : player_have.get(player)){ 
    			if(majiang.get(key).equals(String.valueOf(after1)+focus_name.charAt(1))){
    				flag1 = true;
    			}
    			else if(majiang.get(key).equals(String.valueOf(after2)+focus_name.charAt(1))){
    				flag2 = true;
    			}
    			if(flag1 && flag2){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    
    
    
    
    
    public static void main(String[] args){
    	Game game = new Game();
    	System.out.println(game.getNextPlayer());
    }
    
    
 
}
