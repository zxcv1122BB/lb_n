package com.lb.sys.tools;

/**
 * 数字彩走势图工具
 * @author ASUS
 *
 */
public class DigitalOpenTools {
	//将string类型的数组转换成int
	public static  int[] StringToInt(String[] arrs){
	    int[] ints = new int[arrs.length];
	    for(int i=0;i<arrs.length;i++){
	        ints[i] = Integer.parseInt(arrs[i]);
	    }
	    return ints;
	}
	
	/***
	 * 
	 * 目前只有极速3d定位走势
	 * @param open_no：开奖号(以逗号隔开)
	 * @param up_dwd:上一期的定位走势图数据
	 * @param length_no:开奖号范围个数
	 * @param one_type_id:彩种id
	 * @return
	 */
	public static String getOpenDataStr(String open_no,String up_dwd,int start,int end,String one_type_id) {
		String [] luck_no = open_no.split(","); 
		String new_data = ""; 
		int[] luckNo_num = StringToInt(luck_no);
		if(StringUtil.isBlank(up_dwd)) {
		   switch (one_type_id) {
			case "33":
			case "40":
				//和值特码 k3和值走势 =3-18 或 kl28=0-27
				int sum_tm = luckNo_num[0]+luckNo_num[1]+luckNo_num[2];
				for(int i=start;i<=end;i++) {
					if(sum_tm==i) {
						new_data +="0|";
					}else {
						new_data +="1|";
					}
				}
				if(new_data!=null && !"".equals(new_data)) {
					new_data = new_data.substring(0, new_data.length()-1);
				}
				break;
			case "34":
			case "39":
				//pk10=1-10
				for (int i = 0; i <luckNo_num.length; i++) {
					int k = 1;
					for (int j = start;j <=end; j++) {
						if(j==luckNo_num[i]) {
							new_data +="0";
						}else {
							new_data +="1";
						}
						k++;
						if(j!=k) new_data+="|";
					}
					if(new_data!=null && !"".equals(new_data)) {
						new_data = new_data.substring(0, new_data.length()-1);
					}
					if(i!=luck_no.length) new_data+="#";
				}
				break;
			default:
				//0-9
				for (int i = 0; i <luckNo_num.length; i++) {
					int k = 1;
					for (int j = start; j <=end; j++) {
						if(j==luckNo_num[i]) {
							new_data +="0";
						}else {
							new_data +="1";
						}
						k++;
						if(j!=k) new_data+="|";
					}
					if(new_data!=null && !"".equals(new_data)) {
						new_data = new_data.substring(0, new_data.length()-1);
					}
					if(i!=luck_no.length) new_data+="#";
				}
				break;
		   }
		   return new_data;
		}
		
		String [] updwds = up_dwd.split("#");
		switch (one_type_id) {
			case "38":
				//3d
				for(int i=0;i<luckNo_num.length;i++) {
					String[] updwdStr = updwds[i].split("\\|");
					int[] updwdInt = StringToInt(updwdStr);
					for (int j = start; j < end; j++) {
						if(luckNo_num[i]==j) {
						  new_data+= "0";
						}else {
						  new_data+= (updwdInt[j]+1)+"";	
						}
						if(j!=updwdInt.length) new_data+="|";
					}
					if(new_data!=null && !"".equals(new_data)) {
						new_data = new_data.substring(0, new_data.length()-1);
					}
					if(i!=luck_no.length) new_data+="#";
				}
				break;
			case "32":
			case "35":
			case "36":
				//ssc=0-9
				for (int i = 0; i <luck_no.length; i++) {
					String[] onePwd = updwds[i].split("\\|");
					int[] stringToIntOnePwd = StringToInt(onePwd);
					//0-9
					for (int j = start; j<end; j++) {
						int item_luck_no = Integer.valueOf(luck_no[i].trim());
						if(item_luck_no==j) {
							new_data+="0|";
						}else {					
							new_data+=(stringToIntOnePwd[j]+1)+"|";
						}
					}
					if(new_data!=null && !"".equals(new_data)) {
						new_data = new_data.substring(0, new_data.length()-1);
					}
					if(i!=luck_no.length-1) new_data+="#";
				}
				break;
			case "33":
				//和值特码 k3 和值走势=3-18
				int sum_tm = luckNo_num[0]+luckNo_num[1]+luckNo_num[2];
				//上一期的特码走势数据切割
				int[] countData =StringToInt(up_dwd.split("\\|"));
				for(int i=0;i<countData.length;i++) {
					if((i+3)==sum_tm) {
						new_data+="0|";	
					}else {
						new_data+=(countData[i]+1)+"|";
					}
				}
				if(new_data!=null && !"".equals(new_data)) {
					new_data = new_data.substring(0, new_data.length()-1);
				}
				break;
			case "34":
			case "39":
				//pk10=1-10
				//上一期的走势数据切割 先按#切割
				for(int i=start;i<=end;i++) {
					String[] twoPwd = updwds[i-1].split("\\|");
					int[] stringToIntArrs = StringToInt(twoPwd);
					for(int k=start;k<=end;k++) {
						if(luckNo_num[i-1] == k) {
							new_data+="0|";
						}else {
							new_data+=(stringToIntArrs[k-1]+1)+"|";
						}
					}
					if(new_data!=null && !"".equals(new_data)) {
						new_data = new_data.substring(0, new_data.length()-1);
					}
					if(i!=end) new_data+="#";
				}
				break;
			case "40":
				//和值特码 kl28=0-27
				int sum_tm_kl = luckNo_num[0]+luckNo_num[1]+luckNo_num[2];
				//上一期的特码走势数据切割
				int[] countData_kl =StringToInt(up_dwd.split("\\|"));
				for(int i=0;i<countData_kl.length;i++) {
					if(i==sum_tm_kl) {
						new_data+="0|";	
					}else {
						new_data+=(countData_kl[i]+1)+"|";
					}
				}
				if(new_data!=null && !"".equals(new_data)) {
					new_data = new_data.substring(0, new_data.length()-1);
				}
				break;
			default:
				new_data = "";
				break;
		}
		return new_data;
	}
	
}
