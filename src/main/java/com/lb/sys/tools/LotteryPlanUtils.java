package com.lb.sys.tools;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于计划比对开奖号码与预设开奖号码
 */
public class LotteryPlanUtils {

	/**
	 * 时时彩
	 */
	// 用于时时彩万位定位胆的计划
	public static int sscWwDwd(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		if (planContent.contains(nums[0])) {
			return 1;
		} else {
			return 0;
		}
	}

	// 用于时时彩前二直选的计划
	public static int sscQ2zx(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		String[] h2Nums = planContent.split("-");
		if (h2Nums[0].contains(nums[0]) && h2Nums[1].contains(nums[1])) {
			return 1;
		} else {
			return 0;
		}
	}

	// 用于时时彩后三直选的计划
	public static int sscH3zx(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		String[] h3Nums = planContent.split("-");
		String lastOne = nums[nums.length - 1];
		String lastTwo = nums[nums.length - 2];
		String lastThree = nums[nums.length - 3];
		if (h3Nums[0].contains(lastThree) && h3Nums[1].contains(lastTwo) && h3Nums[2].contains(lastOne)) {
			return 1;
		} else {
			return 0;
		}
	}

	// 用于时时彩后三直选的计划
	public static int sscH3z6(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		String lastOne = nums[nums.length - 1];
		String lastTwo = nums[nums.length - 2];
		String lastThree = nums[nums.length - 3];
		Set<String> set = new HashSet<>();
		set.add(lastOne);
		set.add(lastTwo);
		set.add(lastThree);
		if (set.size() == 3 && planContent.contains(lastOne) && planContent.contains(lastTwo)
				&& planContent.contains(lastThree)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * pk10
	 */
	// 冠军定位胆
	public static int pk10GjDwd(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		if(!StringUtil.isBlank(planContent) && planContent.contains(nums[0])) {
			return 1;
		}else {
			return 0;
		}
		
	}
	
	/**
	 * 快3
	 */
	// 和值
	public static int k3Hz(String luckNum, String planContent) {
		return addThreeNum(luckNum, planContent);
	}

	private static int addThreeNum(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		Integer first = Integer.parseInt(nums[0]);
		Integer sencond = Integer.parseInt(nums[1]);
		Integer third = Integer.parseInt(nums[2]);
		
		Integer count = first + sencond + third;
		if(!StringUtil.isBlank(planContent)) {
			String[] planNums = planContent.split(" ");
			for (String planNum : planNums) {
				if(Integer.parseInt(planNum) == count) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	/**
	 * 快乐十分
	 */
	//数投
	public static int klsfSt(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		if(!StringUtil.isBlank(planContent) && planContent.contains(nums[0])) {
			return 1;
		}else {
			return 0;
		}
	}
	
	/**
	 * 11选5
	 */
	// 定位胆 第一位
	public static int dwd11x5Num1(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		if(!StringUtil.isBlank(planContent) && planContent.contains(nums[0])) {
			return 1;
		}else {
			return 0;
		}
	}
	
	/**
	 * 3D
	 */
	// 定位胆
	public static int dwd3d(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		String[] planNums = planContent.split("-");
		if (planNums[0].contains(nums[0]) && planNums[1].contains(nums[1]) && planNums[2].contains(nums[2])) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 北京28 
	 */
	//特码
	public static int bj28Tm(String luckNum, String planContent) {
		return addThreeNum(luckNum, planContent);
	}
	
	/**
	 * 七星彩
	 */
	// 二字现
	public static int qxc2zx(String luckNum, String planContent) {
		String[] nums = luckNum.split(",");
		Set<String> set = new HashSet<>();
		for (String num : nums) {
			set.add(num);
		}
		if(!StringUtil.isBlank(planContent) && set.size() > 1) {
			int count = 0;
			for (String num : set) {
				if(planContent.contains(num)) {
					count ++;
				}
			}
			if(count >= 2) {
				return 1;
			}
		}
		return 0;
	}
}
