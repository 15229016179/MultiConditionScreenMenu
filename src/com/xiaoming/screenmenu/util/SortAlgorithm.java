package com.xiaoming.screenmenu.util;  

import java.util.List;

/**
 * 	Copyright	2016	CoderDream's Eclipse
 * 
 * 	All right reserved.
 * 	
 * 	Created on 2016年3月17日 下午2:14:58
 * 	
 * 	Update on 2016年3月17日 下午2:14:58
 * 
 * 	@author xiaoming
 *	
 * 	@mail wangfeng.wf@warmdoc.com
 * 
 * 	@tags An overview of this file: 排序算法类
 * 
 */
public class SortAlgorithm {  
  
    /** 
     * 交换指定元素的位置 
     * @param <E>
     * @param rawDatas 
     * @param condition 
     * @param x 
     * @param y 
     */  
    private static <E> void swapElement(List<E> rawDatas, long[] condition, int x, int y) {  
  
    	long tempC = condition[x];
    	condition[x] = condition[y];
    	condition[y] = tempC;
    	if(rawDatas != null){
    		E temp = rawDatas.get(x);  
    		rawDatas.set(x, rawDatas.get(y));
    		rawDatas.set(y, temp);
    	}
    }  
  
    /** 
     * 交换排序法/冒泡排序法
     * 方法：对相邻两元素进行比较、进行交换，嵌套for每次循环依据条件数组将最大元素往后移动，下一次循环将剩余元素进行类似操作。 
     * 适用：初始状态基本有序，不需要进行反复元素交换
     * @param <E> list中的泛型实体对象
     *  
     * @param rawDatas 进行排序的数据集合
     * @param condition 要排序的条件，来自数据集合的每一个对象代表
     * @param sortType 排序类型 正序"asc"/倒序"desc"
     */  
    public static <E> void exchangeSort(List<E> rawDatas, long[] condition, String sortType) {  
        if (sortType.equals("asc")) { // 正序
            for (int i = 1; i < condition.length; i++) {  
                // 比较条件数组中相邻的两个元素，较大的数往后移动
                for (int j = 0; j < condition.length - i; j++) {  
                    if (condition[j] > condition[j + 1]) {  
                        // 交换相邻两个元素的位置
                    	swapElement(rawDatas, condition, j, j + 1);  
                    }  
                }  
            }  
        } else if (sortType.equals("desc")) { // 倒序
            for (int i = 1; i < condition.length; i++) {  
                // 比较条件数组中相邻的两个元素，较小的数往后移动
                for (int j = 0; j < condition.length - i; j++) {  
                    if (condition[j] < condition[j + 1]) {  
                        // 交换相邻两个元素的位置
                    	swapElement(rawDatas, condition, j, j + 1);  
                    }  
                }  
            }  
        }  
  
    }  
  
    /** 
     * 选择排序法/直接排序法
     * 方法：嵌套for每次循环依据排序条件从待排序的数组元素中选出最小或最大的一个元素，并依次排列组合。 
     * 特点：交换次数比冒泡排序少，速度较冒泡排序高
     *  
     * @param <E> list中的泛型实体对象
     *  
     * @param rawDatas 进行排序的数据集合
     * @param condition 要排序的条件，来自数据集合的每一个对象代表
     * @param sortType 排序类型 正序"asc"/倒序"desc"
     */  
    public static <E> void selectSort(List<E> rawDatas, long[] condition, String sortType) {  
        if (sortType.equals("asc")) {
            int record;  
            for (int i = 1; i < condition.length; i++) {  
                record = 0;  
                for (int j = 1; j <= condition.length - i; j++) {  
                    if (condition[j] > condition[record]) {  
                        record = j;  
                    }  
                }  
                // 交换condition.length - i和当前最小元素
                swapElement(rawDatas, condition, condition.length - i, record);  
            }  
        } else if (sortType.equals("desc")) { 
            int record;  
            for (int i = 1; i < condition.length; i++) {  
                record = 0;  
                for (int j = 1; j <= condition.length - i; j++) {  
                    if (condition[j] < condition[record]) {  
                        record = j;  
                    }  
                }  
                // 交换condition.length - i和当前最小元素
                swapElement(rawDatas, condition, condition.length - i, record);  
            }  
        }  
  
    }  
  
    /** 
     * 插入排序法
     * 方法：嵌套for每次循环将新增的一项元素与已处理的元素进行依次比较并排在适当位置，每次将生成一个新的有序数组 
     * 特点：性能较好。 
     * 适用：当记录规模较小时
     * @param <E> list中的泛型实体对象
     *  
     * @param rawDatas 进行排序的数据集合
     * @param condition 要排序的条件，来自数据集合的每一个对象代表
     * @param sortType 排序类型 正序"asc"/倒序"desc"
     */  
    public static <E> void insertSort(List<E> rawDatas, long[] condition, String sortType) {  
        if (sortType.equals("asc")) {
            for (int i = 1; i < condition.length; i++) {  
                for (int j = 0; j < i; j++) {  
                    if (condition[j] > condition[i]) {  
                    	// 交换x和y两个元素
                    	swapElement(rawDatas, condition, i, j);  
                    }  
                }  
            }  
        } else if (sortType.equals("desc")) {
            for (int i = 1; i < condition.length; i++) {  
                for (int j = 0; j < i; j++) {  
                    if (condition[j] < condition[i]) {  
                        // 交换x和y两个元素
                    	swapElement(rawDatas, condition, i, j);  
                    }  
                }  
            }  
        }  
  
    }  
  
    /** 
     * 反转数组的方法 
     * @param <E>
     *  
     * @param data 源数组 
     */  
    public static <E> void reverse(List<E> data) {  
        int length = data.size();  
        E temp = null;// 临时变量  
  
        for (int i = 0; i < length / 2; i++) {  
            temp = data.get(i);  
            data.set(i, data.get(length - 1 - i));  
            data.set(length - 1 - i, temp);  
        }  
  
    }  
  
    /** 
     *  
     * 二分查找特定整数在整型数组中的位置(递归) 
     *  
     * 查找线性表必须是有序列表 
     *  
     * @paramdataset 
     * @paramdata 
     * @parambeginIndex 
     * @paramendIndex 
     * @returnindex 
     */  
    public static int binarySearch(int[] dataset, int data, int beginIndex, int endIndex) {  
  
        int midIndex = (beginIndex + endIndex) >>> 1; // 相当于mid = (low + high) / 2，但是效率会高些  
  
        if (data < dataset[beginIndex] || data > dataset[endIndex] || beginIndex > endIndex) {  
            return -1;  
        }  
  
        if (data < dataset[midIndex]) {  
            return binarySearch(dataset, data, beginIndex, midIndex - 1);  
        } else if (data > dataset[midIndex]) {  
            return binarySearch(dataset, data, midIndex + 1, endIndex);  
        } else {  
            return midIndex;  
        }  
  
    }  
  
    /** 
     *  
     * 二分查找特定整数在整型数组中的位置(非递归) 
     *  
     * 查找线性表必须是有序列表 
     *  
     * @paramdataset 
     * @paramdata 
     * @returnindex 
     */  
    public static int binarySearch(int[] dataset, int data) {  
        int beginIndex = 0;  
        int endIndex = dataset.length - 1;  
        int midIndex = -1;  
  
        if (data < dataset[beginIndex] || data > dataset[endIndex] || beginIndex > endIndex) {  
            return -1;  
        }  
  
        while (beginIndex <= endIndex) {  
            midIndex = (beginIndex + endIndex) >>> 1; // 相当于midIndex =(beginIndex + endIndex) / 2，但是效率会高些  
  
            if (data < dataset[midIndex]) {  
                endIndex = midIndex - 1;  
            } else if (data > dataset[midIndex]) {  
                beginIndex = midIndex + 1;  
            } else {  
                return midIndex;  
            }  
        }  
  
        return -1;  
    }  
  
}  