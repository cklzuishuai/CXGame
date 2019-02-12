package com.myproject.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Game {
	private String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	//1:条子 2:筒子 3:万子
	private String[] types = {"1", "2", "3"};
	private String[] others = { "东风", "西风", "南风", "北风", "红中", "白板", "发财" };
	
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
    
    public void getRestAll(){
    	Set<Entry<Integer, String>> entrySet = majiang.entrySet();
        for (Entry<Integer, String> entry : entrySet)
        {
            System.out.print("[" + entry.getKey() + ":" + entry.getValue() + "]   ");
        }
    }
    
    
    
    
    public static void main(String[] args){
    	Game game = new Game();
    	game.init();
    	game.getRestAll();
    }
 
}
