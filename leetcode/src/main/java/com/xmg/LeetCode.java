package com.xmg;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author makui
 * @created 2022/11/21
 **/
public class LeetCode {

    @Test
    public void solution() {
        int[] num = new int[]{3, 2, 4,5,8};
        final int[] ints = this.twoSum2(num, 8);
        System.out.println(Arrays.toString(ints));
    }

    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0};
    }
    public int[] twoSum2(int[] nums, int target) {
        int[] result = new int[2];
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            final int num = nums[i];
            if (hashMap.containsKey(num)) {
                result[0] = hashMap.get(num);
                result[1] = i;
                return result;
            }
            hashMap.put(target - num, i);
        }
        return new int[]{0};
    }
}
