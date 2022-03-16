package org.graduate.book_service.constant;

/**
 * 定义的一些基本常量, 如 URL 参数： http://www.jd.com/search?keyWord=***&page=1
 *
 * @author : LiuXianghai on 2020/12/30
 * @Created : 2020/12/30 - 12:22
 * @Project : GetDataService
 */
public final class Const {
    // 添加的 URL 参数的名称
    public final static String PAGE_PARAMETER_KEY = "page";

    public final static Integer PARSE_COUNT = 3000;

    public final static String NEGATIVE_RESULT_REGEX = "负面";

    public final static String POSITIVE_RESULT_REGEX = "正面";

    // HanLP 情感分析 URL
    public final static String URL = "http://comdo.hanlp.com/hanlp/v1/textAnalysis/sentimentAnalysis";

    // HanLP 访问 Token
    public final static String token = "14492b4a3254400d9498f0af76cd91631618025478099token";

    public final static Double[][] mapTable = {
            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00},
            {0.00, 1.00, 0.55, 0.45, 0.67, 0.34, 0.42, 0.12, 0.71, 0.21, 0.11, 0.23, 0.31, 0.87, 0.07, 0.12, 0.23, 0.56, 0.24, 0.35, 0.27, 0.36},
            {0.00, 0.55, 1.00, 0.76, 0.54, 0.87, 0.34, 0.68, 0.45, 0.23, 0.12, 0.34, 0.67, 0.34, 0.07, 0.56, 0.45, 0.76, 0.34, 0.21, 0.12, 0.32},
            {0.00, 0.45, 0.76, 1.00, 0.56, 0.56, 0.23, 0.45, 0.76, 0.43, 0.12, 0.07, 0.34, 0.08, 0.38, 0.56, 0.11, 0.78, 0.13, 0.11, 0.16, 0.06},
            {0.00, 0.67, 0.54, 0.56, 1.00, 0.64, 0.45, 0.34, 0.65, 0.34, 0.03, 0.12, 0.31, 0.45, 0.56, 0.23, 0.46, 0.72, 1.72, 2.72, 3.72, 4.72},
            {0.00, 0.34, 0.87, 0.67, 0.64, 1.00, 0.75, 0.87, 0.34, 0.15, 0.56, 0.08, 0.34, 0.16, 0.41, 0.54, 0.10, 0.32, 0.13, 0.62, 0.31, 0.17},
            {0.00, 0.42, 0.34, 0.23, 0.45, 0.75, 1.00, 0.64, 0.24, 0.67, 0.23, 0.21, 0.14, 0.64, 0.34, 0.11, 0.54, 0.34, 0.69, 0.74, 0.52, 0.26},
            {0.00, 0.12, 0.68, 0.45, 0.34, 0.87, 0.64, 1.00, 0.22, 0.31, 0.64, 0.17, 0.52, 0.15, 0.09, 0.38, 0.26, 0.13, 0.24, 0.59, 0.41, 0.30},
            {0.00, 0.71, 0.45, 0.76, 0.65, 0.34, 0.24, 0.22, 1.00, 0.36, 0.13, 0.20, 0.05, 0.48, 0.60, 0.10, 0.21, 0.94, 0.64, 0.41, 0.26, 0.32},
            {0.00, 0.21, 0.23, 0.43, 0.34, 0.15, 0.67, 0.31, 0.36, 1.00, 0.48, 0.68, 0.13, 0.68, 0.45, 0.03, 0.76, 0.42, 0.69, 0.57, 0.46, 0.79},
            {0.00, 0.11, 0.12, 0.12, 0.03, 0.56, 0.23, 0.64, 0.13, 0.48, 1.00, 0.34, 0.12, 0.46, 0.21, 0.15, 0.42, 0.34, 0.21, 0.16, 0.08, 0.22},
            {0.00, 0.23, 0.34, 0.07, 0.12, 0.08, 0.21, 0.17, 0.20, 0.68, 0.34, 1.00, 0.11, 0.67, 0.31, 0.08, 0.78, 0.34, 0.64, 0.54, 0.22, 0.84},
            {0.00, 0.31, 0.67, 0.34, 0.31, 0.34, 0.14, 0.52, 0.05, 0.13, 0.12, 0.11, 1.00, 0.12, 0.56, 0.42, 0.11, 0.56, 0.07, 0.14, 0.09, 0.14},
            {0.00, 0.87, 0.34, 0.08, 0.45, 0.16, 0.64, 0.15, 0.48, 0.68, 0.46, 0.67, 0.12, 1.00, 0.64, 0.12, 0.45, 0.31, 0.16, 0.67, 0.78, 0.42},
            {0.00, 0.07, 0.07, 0.38, 0.56, 0.41, 0.34, 0.09, 0.60, 0.45, 0.21, 0.31, 0.56, 0.64, 1.00, 0.45, 0.36, 0.34, 0.45, 0.31, 0.22, 0.31},
            {0.00, 0.12, 0.56, 0.56, 0.23, 0.54, 0.11, 0.38, 0.10, 0.03, 0.15, 0.08, 0.42, 0.12, 0.45, 1.00, 0.14, 0.64, 0.21, 0.08, 0.06, 0.14},
            {0.00, 0.23, 0.45, 0.11, 0.46, 0.10, 0.54, 0.26, 0.21, 0.76, 0.42, 0.78, 0.11, 0.45, 0.36, 0.14, 1.00, 0.34, 0.46, 0.58, 0.12, 0.94},
            {0.00, 0.56, 0.76, 0.78, 0.72, 0.32, 0.34, 0.13, 0.94, 0.42, 0.34, 0.34, 0.56, 0.31, 0.34, 0.64, 0.34, 1.00, 0.71, 0.67, 0.34, 0.42},
            {0.00, 0.24, 0.34, 0.13, 1.72, 0.13, 0.69, 0.24, 0.64, 0.69, 0.21, 0.64, 0.07, 0.16, 0.45, 0.21, 0.46, 0.71, 1.00, 0.62, 0.41, 0.35},
            {0.00, 0.35, 0.21, 0.11, 2.72, 0.62, 0.74, 0.59, 0.41, 0.57, 0.16, 0.54, 0.14, 0.67, 0.31, 0.08, 0.58, 0.67, 0.62, 1.00, 0.26, 0.40},
            {0.00, 0.27, 0.12, 0.16, 3.72, 0.31, 0.52, 0.41, 0.26, 0.46, 0.08, 0.22, 0.09, 0.78, 0.22, 0.06, 0.12, 0.34, 0.41, 0.26, 1.00, 0.23},
            {0.00, 0.36, 0.32, 0.06, 4.72, 0.17, 0.26, 0.30, 0.32, 0.79, 0.22, 0.84, 0.14, 0.42, 0.31, 0.14, 0.94, 0.42, 0.35, 0.40, 0.23, 1.00}
    };
}
