package com.cuantomefalta.cuantomefalta;

import android.support.v4.util.Pair;

import java.util.ArrayList;

/**
 * Created by Jairo on 10/20/2017.
 */

public class Settings {

    private int min_grade=1;
    private int max_grade=100;
    private ArrayList<Pair<Integer,Double>> conversionTable;

    public Settings()
    {
        conversionTable = new ArrayList<Pair<Integer, Double>>();
        setConversionTable();


    }


    public int getMin_grade(){

        return min_grade;
    }

    public int getMax_grade(){

        return max_grade;
    }

    private void setConversionTable()
    {
        conversionTable.add(0,new Pair<Integer, Double>(1,1.0));
        conversionTable.add(1,new Pair<Integer, Double>(2,1.0));
        conversionTable.add(2,new Pair<Integer, Double>(3,1.0));
        conversionTable.add(3,new Pair<Integer, Double>(4,1.0));
        conversionTable.add(4,new Pair<Integer, Double>(5,1.0));
        conversionTable.add(5,new Pair<Integer, Double>(6,1.0));
        conversionTable.add(6,new Pair<Integer, Double>(7,1.0));
        conversionTable.add(7,new Pair<Integer, Double>(8,1.1));
        conversionTable.add(8,new Pair<Integer, Double>(9,1.2));
        conversionTable.add(9,new Pair<Integer, Double>(10,1.3));
        conversionTable.add(10,new Pair<Integer, Double>(11,1.4));
        conversionTable.add(11,new Pair<Integer, Double>(12,1.5));
        conversionTable.add(12,new Pair<Integer, Double>(13,1.6));
        conversionTable.add(13,new Pair<Integer, Double>(14,1.7));
        conversionTable.add(14,new Pair<Integer, Double>(15,1.8));
        conversionTable.add(15,new Pair<Integer, Double>(16,1.9));
        conversionTable.add(16,new Pair<Integer, Double>(17,2.0));
        conversionTable.add(17,new Pair<Integer, Double>(18,2.0));
        conversionTable.add(18,new Pair<Integer, Double>(19,2.1));
        conversionTable.add(19,new Pair<Integer, Double>(20,2.2));
        conversionTable.add(20,new Pair<Integer, Double>(21,2.3));
        conversionTable.add(21,new Pair<Integer, Double>(22,2.4));
        conversionTable.add(22,new Pair<Integer, Double>(23,2.5));
        conversionTable.add(23,new Pair<Integer, Double>(24,2.6));
        conversionTable.add(24,new Pair<Integer, Double>(25,2.7));
        conversionTable.add(25,new Pair<Integer, Double>(26,2.8));
        conversionTable.add(26,new Pair<Integer, Double>(27,2.9));
        conversionTable.add(27,new Pair<Integer, Double>(28,3.0));
        conversionTable.add(28,new Pair<Integer, Double>(29,3.0));
        conversionTable.add(29,new Pair<Integer, Double>(30,3.1));
        conversionTable.add(30,new Pair<Integer, Double>(31,3.2));
        conversionTable.add(31,new Pair<Integer, Double>(32,3.3));
        conversionTable.add(32,new Pair<Integer, Double>(33,3.4));
        conversionTable.add(33,new Pair<Integer, Double>(34,3.5));
        conversionTable.add(34,new Pair<Integer, Double>(35,3.6));
        conversionTable.add(35,new Pair<Integer, Double>(36,3.7));
        conversionTable.add(36,new Pair<Integer, Double>(37,3.8));
        conversionTable.add(37,new Pair<Integer, Double>(38,3.9));
        conversionTable.add(38,new Pair<Integer, Double>(39,4.0));
        conversionTable.add(39,new Pair<Integer, Double>(40,4.0));
        conversionTable.add(40,new Pair<Integer, Double>(41,4.1));
        conversionTable.add(41,new Pair<Integer, Double>(42,4.2));
        conversionTable.add(42,new Pair<Integer, Double>(43,4.3));
        conversionTable.add(43,new Pair<Integer, Double>(44,4.4));
        conversionTable.add(44,new Pair<Integer, Double>(45,4.5));
        conversionTable.add(45,new Pair<Integer, Double>(46,4.6));
        conversionTable.add(46,new Pair<Integer, Double>(47,4.7));
        conversionTable.add(47,new Pair<Integer, Double>(48,4.8));
        conversionTable.add(48,new Pair<Integer, Double>(49,4.8));
        conversionTable.add(49,new Pair<Integer, Double>(50,4.9));
        conversionTable.add(50,new Pair<Integer, Double>(51,5.0));
        conversionTable.add(51,new Pair<Integer, Double>(52,5.1));
        conversionTable.add(52,new Pair<Integer, Double>(53,5.2));
        conversionTable.add(53,new Pair<Integer, Double>(54,5.3));
        conversionTable.add(54,new Pair<Integer, Double>(55,5.3));
        conversionTable.add(55,new Pair<Integer, Double>(56,5.4));
        conversionTable.add(56,new Pair<Integer, Double>(57,5.5));
        conversionTable.add(57,new Pair<Integer, Double>(58,5.6));
        conversionTable.add(58,new Pair<Integer, Double>(59,5.7));
        conversionTable.add(59,new Pair<Integer, Double>(60,5.8));
        conversionTable.add(60,new Pair<Integer, Double>(61,5.9));
        conversionTable.add(61,new Pair<Integer, Double>(62,6.0));
        conversionTable.add(62,new Pair<Integer, Double>(63,6.0));
        conversionTable.add(63,new Pair<Integer, Double>(64,6.1));
        conversionTable.add(64,new Pair<Integer, Double>(65,6.2));
        conversionTable.add(65,new Pair<Integer, Double>(66,6.3));
        conversionTable.add(66,new Pair<Integer, Double>(67,6.4));
        conversionTable.add(67,new Pair<Integer, Double>(68,6.5));
        conversionTable.add(68,new Pair<Integer, Double>(69,6.6));
        conversionTable.add(69,new Pair<Integer, Double>(70,6.7));
        conversionTable.add(70,new Pair<Integer, Double>(71,6.8));
        conversionTable.add(71,new Pair<Integer, Double>(72,6.9));
        conversionTable.add(72,new Pair<Integer, Double>(73,7.0));
        conversionTable.add(73,new Pair<Integer, Double>(74,7.0));
        conversionTable.add(74,new Pair<Integer, Double>(75,7.1));
        conversionTable.add(75,new Pair<Integer, Double>(76,7.2));
        conversionTable.add(76,new Pair<Integer, Double>(77,7.3));
        conversionTable.add(77,new Pair<Integer, Double>(78,7.4));
        conversionTable.add(78,new Pair<Integer, Double>(79,7.5));
        conversionTable.add(79,new Pair<Integer, Double>(80,7.6));
        conversionTable.add(80,new Pair<Integer, Double>(81,7.7));
        conversionTable.add(81,new Pair<Integer, Double>(82,7.8));
        conversionTable.add(82,new Pair<Integer, Double>(83,7.9));
        conversionTable.add(83,new Pair<Integer, Double>(84,8.0));
        conversionTable.add(84,new Pair<Integer, Double>(85,8.0));
        conversionTable.add(85,new Pair<Integer, Double>(86,8.1));
        conversionTable.add(86,new Pair<Integer, Double>(87,8.2));
        conversionTable.add(87,new Pair<Integer, Double>(88,8.3));
        conversionTable.add(88,new Pair<Integer, Double>(89,8.4));
        conversionTable.add(89,new Pair<Integer, Double>(90,8.5));
        conversionTable.add(90,new Pair<Integer, Double>(91,8.6));
        conversionTable.add(91,new Pair<Integer, Double>(92,8.7));
        conversionTable.add(92,new Pair<Integer, Double>(93,8.8));
        conversionTable.add(93,new Pair<Integer, Double>(94,8.9));
        conversionTable.add(94,new Pair<Integer, Double>(95,9.0));
        conversionTable.add(95,new Pair<Integer, Double>(96,9.0));
        conversionTable.add(96,new Pair<Integer, Double>(97,9.0));
        conversionTable.add(97,new Pair<Integer, Double>(98,9.0));
        conversionTable.add(98,new Pair<Integer, Double>(99,9.0));
        conversionTable.add(99,new Pair<Integer, Double>(100,9.0));
    }

    public Double getConversionOf(Integer g){

        for(Pair<Integer,Double> pair : conversionTable)
        {
            if(pair.first.equals(g))
            {
                return pair.second;
            }

        }

        return 0.0;
    }

    public Integer getConversionOf(Double c){

        for(Pair<Integer,Double> pair : conversionTable)
        {
            if(pair.second.equals(c))
            {
                return pair.first;
            }

        }

        return 0;
    }

}
