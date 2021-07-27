package com.ushine.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

public class MainStream {
    public static void main(String[] args) {
        System.out.println("Yeah, babe. I'm a mainstream");
        int[] test = new int[]{1, 5, 5, 8, 2, 8, 7, 3};

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
        if (isEven(sum)) {
            return integers.stream().filter(i -> !isEven(i)).collect(Collectors.toList());
        } else return integers.stream().filter(i -> isEven(i)).collect(Collectors.toList());
    }

    private static List<Integer> oddOrEvenOn2(List<Integer> integers) {
        return integers
                .stream()
                .filter(i -> !isEven(i) && isEven(integers.stream().reduce(0, Integer::sum))||
                        isEven(i) && !isEven(integers.stream().reduce(0, Integer::sum)))
                .collect(Collectors.toList());
    }

    private static List<Integer> oddOrEvenOnOneLine(List<Integer> integers) {
        return integers.stream().collect(Collectors.groupingBy(MainStream::isEven)).get(!isEven(integers.stream().reduce(0, Integer::sum)));
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream().collect(partitioningBy(MainStream::isEven));
        List<Integer> list = map.get(false);
        return isEven(list.size()) ? list : map.get(true);
    }

    private static Boolean isEven(Integer i) {
        return i % 2 == 0;
    }
}
