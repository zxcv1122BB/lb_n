package com.lb.sys.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RedPacketUtil {  
	
	private  float MINMONEY =0.01f;  
	private  float MAXMONEY =200;
    //每个红包最大是平均值的倍数  
    private static final float TIMES = 5.1f;  
      
    public RedPacketUtil(float MINMONEY,float money) {
    	this.MAXMONEY=(float) (0.9*money);
    	this.MINMONEY=MINMONEY;
    }
    
    private boolean isRight(float  money,int count){
      double avg = money/count;
      if(avg<MINMONEY){
        return false;
      }
      else if(avg>MAXMONEY){
        return false;
      }
      return true;
    }
    
    private float randomRedPacket(float money,float mins,float maxs,int count){
      if(count==1){
        return (float)(Math.round(money*100))/100;
      }
      if(mins == maxs){
        return mins;//如果最大值和最小值一样，就返回mins
      }
      float max = maxs>money?money:maxs;
      
      float one = ((float)Math.random()*(max-mins)+mins);
      one = (float)(Math.round(one*100))/100;
      float moneyOther = money - one;
      if(isRight(moneyOther,count-1)){
        return one;
      }
      else{
        //重新分配
        float avg = moneyOther / (count-1);
        if(avg<MINMONEY){
          return randomRedPacket(money,mins,one,count);
        }else if(avg>MAXMONEY){
          return randomRedPacket(money,one,maxs,count);
        }
      }
      return one;
    }
    public List<Float> splitRedPackets(float money,int count) {
      List<Float> list = new ArrayList<Float>();
      if(count==1){
    	  list.add(money);
        return list;
      }
      if(!isRight(money,count)){
        return null;
      }
      
      float max = (float)(money*TIMES/count);

      max = max>MAXMONEY?MAXMONEY:max;
      for(int i=0;i<count;i++){
        float one = randomRedPacket(money,MINMONEY,max,count-i);
        list.add(one);
        money-=one;
      }
      Collections.shuffle(list);
      return list;
    }
}