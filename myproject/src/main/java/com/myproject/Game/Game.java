package com.myproject.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Game {
	private static String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static String[] types = {"条", "筒", "万"};
	private static String[] others = { "东风", "南风", "西风", "北风", "红中", "发财", "白板" };
	
	private String king;
	
	private String[] player = {"1", "2", "3", "4"};
    private List<Integer> player1 = new ArrayList<Integer>();
    private List<Integer> player2 = new ArrayList<Integer>();
    private List<Integer> player3 = new ArrayList<Integer>();
    private List<Integer> player4 = new ArrayList<Integer>();
	
	private Integer get_index = 0;
	
	private Map<String, ArrayList<Integer>> player_have = new HashMap<String, ArrayList<Integer>>();
	
	private Map<Integer, String> majiang = new HashMap<Integer, String>();
	
	private List<Integer> majiangNumber = new ArrayList<Integer>();

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
        getKing(get_index);
        System.out.println("发了" + get_index + "张牌");
        Collections.sort(player1);
        Collections.sort(player2);
        Collections.sort(player3);
        Collections.sort(player4);
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
    
    public void getPlayerHave(String player){
        //遍历List集合,获取元素,作为键,到集合Map中找值
        System.out.print(player + " " + "共有" + player_have.get(player).size() + "张牌:  ");
        for (Integer key : player_have.get(player))
        {
            String value = majiang.get(key);
            System.out.print("[" + value + "] ");
        }
        System.out.println();
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
    
    
    public static void main(String[] args){
    	Game game = new Game();
    	game.init();
    	game.getRestAll();
    	game.deal();
    	game.getPlayerHave("1");
    	game.getPlayerHave("2");
    	game.getPlayerHave("3");
    	game.getPlayerHave("4");
    }
    
 
}
