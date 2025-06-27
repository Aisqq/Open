package com.me.utils;

import java.util.*;

public class ModelUtils {
    /**
     * 获取判断和异常得分
     * 范围0-1
     * 分数越小越好
     * @param data
     * @param threshold
     * @return 数组第一个为是否异常  1异常 0 正常 第二个为得分
     */
    static public double[] analyseFunction(double[] data, double threshold){
        /*double[] data = {
                36.5, 36.8, 37.0, Double.NaN, Double.NaN, 36.7, 36.9, 37.1, 37.2, Double.NaN,
                36.9, 36.7, 36.5, 36.3, 36.2, 36.1, 36.0, 35.9, 36.2, 36.3,
                36.5, 36.7, 36.9, 37.0, 37.1, 37.2, 37.0, 36.8, Double.NaN, 37.4
        };*/
        // 获取最后一天的索引
        int lastDayIndex = data.length - 1;
        System.out.println("Last Day Index: " + lastDayIndex);
        // 保存最后一天的数据
        double lastDayValue = data[lastDayIndex];
        // 将原始数据的最后一天设为NaN，这样在IQR检测和填充时不会处理它
        data[lastDayIndex] = Double.NaN;
        // 使用IQR方法标记异常值
        double[] cleanedData = ModelUtils.detectOutliersIQR(data, 1.2);
        // 填充缺失值（NaN）通过插值
        double[] filledData = ModelUtils.fillMissingValues(cleanedData);
        // 将最后一天的数据恢复
        filledData[lastDayIndex] = lastDayValue;
        // 打印填充后的数据
        for (int i = 0; i < filledData.length; i++) {
            System.out.println("Filled Data[" + i + "]: " + filledData[i]);
        }
        // 异常检测（最后一天使用原始值，其他天使用修正值）
        double[] anomalyDetectionResult = ModelUtils.multiWindowAnomalyDetection(filledData, lastDayIndex, new double[]{0.5, 0.3, 0.2}, threshold);
        return anomalyDetectionResult;
    }

    // 改进的IQR异常检测
    private static double[] detectOutliersIQR(double[] data, double threshold) {
        if (data == null || data.length == 0) return new double[0];

        double[] result = Arrays.copyOf(data, data.length);
        List<Double> validValues = new ArrayList<>();

        // 收集所有有效值
        for (double value : data) {
            if (!Double.isNaN(value)) {
                validValues.add(value);
            }
        }

        // 数据太少时不进行异常检测
        if (validValues.size() < 4) return result;

        // 计算分位数
        Collections.sort(validValues);
        int n = validValues.size();
        double Q1 = validValues.get(n / 4);
        double Q3 = validValues.get((int) (n * 0.75));
        double IQR = Q3 - Q1;

        double lowerBound = Q1 - threshold * IQR;
        double upperBound = Q3 + threshold * IQR;

        // 标记异常值
        for (int i = 0; i < data.length; i++) {
            if (!Double.isNaN(data[i]) &&
                    (data[i] < lowerBound || data[i] > upperBound)) {
                result[i] = Double.NaN;
            }
        }
        return result;
    }

    // 线性插值方法
    private static double linearInterpolation(double x0, double y0, double x1, double y1, double x) {
        return y0 + (y1 - y0) * ((x - x0) / (x1 - x0));
    }

    // 改进的缺失值填充
    private static double[] fillMissingValues(double[] data) {
        if (data == null || data.length == 0) return new double[0];

        double[] filled = Arrays.copyOf(data, data.length);
        List<Integer> knownIndices = new ArrayList<>();

        // 收集已知点的索引
        for (int i = 0; i < data.length; i++) {
            if (!Double.isNaN(data[i])) {
                knownIndices.add(i);
            }
        }

        // 没有已知点时返回原数组
        if (knownIndices.isEmpty()) return filled;

        // 处理每个缺失点
        for (int i = 0; i < filled.length; i++) {
            if (Double.isNaN(filled[i])) {
                int leftIdx = -1;
                int rightIdx = -1;

                // 向左找最近的已知点
                for (int j = i - 1; j >= 0; j--) {
                    if (knownIndices.contains(j)) {
                        leftIdx = j;
                        break;
                    }
                }

                // 向右找最近的已知点
                for (int j = i + 1; j < filled.length; j++) {
                    if (knownIndices.contains(j)) {
                        rightIdx = j;
                        break;
                    }
                }

                // 根据找到的点进行填充
                if (leftIdx >= 0 && rightIdx >= 0) {
                    // 两侧都有点 - 线性插值
                    filled[i] = linearInterpolation(
                            leftIdx, data[leftIdx],
                            rightIdx, data[rightIdx],
                            i
                    );
                } else if (leftIdx >= 0) {
                    // 只有左侧点 - 使用左侧值
                    filled[i] = data[leftIdx];
                } else if (rightIdx >= 0) {
                    // 只有右侧点 - 使用右侧值
                    filled[i] = data[rightIdx];
                } else {
                    // 没有参考点 - 使用全局平均值
                    filled[i] = calculateGlobalAverage(data);
                }
            }
        }
        return filled;
    }

    // 计算全局平均值
    private static double calculateGlobalAverage(double[] data) {
        double sum = 0;
        int count = 0;
        for (double value : data) {
            if (!Double.isNaN(value)) {
                sum += value;
                count++;
            }
        }
        return (count > 0) ? sum / count : Double.NaN;
    }

    // 改进的多窗口异常检测
    private static double[] multiWindowAnomalyDetection(double[] filledData, int dayIndex,
                                                       double[] weights, double threshold) {
        // 输入验证
        if (filledData == null || filledData.length == 0 ||
                dayIndex < 0 || dayIndex >= filledData.length) {
            return new double[]{0, 0};
        }

        // 默认权重设置
        if (weights == null || weights.length != 3) {
            weights = new double[]{0.5, 0.3, 0.2};
        }

        int[] windows = {7, 15, 30};
        List<Double> scores = new ArrayList<>();
        double currentValue = filledData[dayIndex];

        for (int windowSize : windows) {
            int startIdx = Math.max(0, dayIndex - windowSize + 1);
            int endIdx = dayIndex + 1;
            double[] windowData = Arrays.copyOfRange(filledData, startIdx, endIdx);

            // 窗口数据不足
            if (windowData.length < 3) {
                scores.add(0.0);
                continue;
            }

            // 计算统计量
            double mean = Arrays.stream(windowData).average().orElse(currentValue);
            double std = Math.sqrt(Arrays.stream(windowData)
                    .map(v -> Math.pow(v - mean, 2))
                    .average()
                    .orElse(0));

            // 计算Z-score
            double zScore = (std > 1e-6) ? Math.abs(currentValue - mean) / std : 0.0;

            // 计算IQR范围
            Arrays.sort(windowData);
            int n = windowData.length;
            double q1 = windowData[n / 4];
            double q3 = windowData[(int) (n * 0.75)];
            double iqr = q3 - q1;

            // IQR评分
            double iqrScore = 0;
            if (iqr > 1e-6) {
                double lowerBound = q1 - 1.5 * iqr;
                double upperBound = q3 + 1.5 * iqr;
                iqrScore = (currentValue < lowerBound || currentValue > upperBound) ? 1.0 : 0.0;
            }

            // 组合评分
            double windowScore = 0.7 * Math.min(zScore / 3.0, 1.0) + 0.3 * iqrScore;
            scores.add(windowScore);
        }

        // 加权平均计算最终评分
        double totalScore = 0;
        int minSize = Math.min(scores.size(), weights.length);
        for (int i = 0; i < minSize; i++) {
            totalScore += weights[i] * scores.get(i);
        }

        // 返回结果 [异常标志, 异常评分]
        return new double[]{
                (totalScore > threshold) ? 1 : 0,
                totalScore
        };
    }
}