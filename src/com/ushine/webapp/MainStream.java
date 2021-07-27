package com.ushine.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

public class MainStream {
    public static void main(String[] args) {
        System.out.println("Yeah, babe. I'm a mainstream");
        int[] test = new int[]{1, 6, 6, 8, 2, 8, 7, 3,7};

        List<Integer> list = Arrays.stream(test).boxed().collect(Collectors.toList());
        System.out.println();
        System.out.println(list);
        System.out.println("-----------Min number---------------");
        System.out.println(minValue(test));
        System.out.println("-----------List sum-----------------");
        System.out.println(list.stream().reduce(0, Integer::sum));
        System.out.println("----------Two streams, On-----------");
        System.out.println(oddOrEvenOn(list));
        System.out.println("----------One line, On2-------------");
        System.out.println(oddOrEvenOn2(list));
        System.out.println("----------One line, On--------------");
        System.out.println(oddOrEvenOnOneLine(list));
        System.out.println("----------One stream, On--------------");
        System.out.println(oddOrEven(list));

    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce(0, (result, next) -> result * 10 + next);
    }

    private static List<Integer> oddOrEvenOn(List<Integer> integers) {
        final int sum = integers.stream().reduce(0, Integer::sum);
        if (sum % 2 == 0) {
            return integers.stream().filter(i -> i % 2 != 0).collect(Collectors.toList());
        } else return integers.stream().filter(i -> i % 2 == 0).collect(Collectors.toList());
    }

    private static List<Integer> oddOrEvenOn2(List<Integer> integers) {
        return integers
                .stream()
                .filter(i -> i % 2 != 0 && integers.stream().reduce(0, Integer::sum) % 2 == 0 ||
                        i % 2 == 0 && integers.stream().reduce(0, Integer::sum) % 2 != 0)
                .collect(Collectors.toList());
    }

    private static List<Integer> oddOrEvenOnOneLine(List<Integer> integers) {
        return integers.stream().collect(Collectors.groupingBy(i -> i % 2 == 0)).get(integers.stream().reduce(0, Integer::sum)%2 !=0);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream().collect(partitioningBy(i -> i % 2 == 0));
        List<Integer> list = map.get(false);
        return list.size() % 2 == 0 ? list : map.get(true);
    }
}
