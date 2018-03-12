package com.viewscenes.printexcel;

/**
 *
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
//import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.TextAnchor;
//import org.jfree.chart.labels.StandardPieItemLabelGenerator;



public class Chart {
    private Font subTitleFont = new Font("����", Font.BOLD, 13);
    private Font dateLabelFont = new Font("����", Font.PLAIN ,12);
    /**
     * ͼ��������
     */
    private String MainTitle;

    /**
     * ͼ������
     */
    private String SecondTitle;

    /**
     * ͼ��С����
     */
    private String ThirdTitle;


    /**
     * ������״ͼ��ֵ����Ƿ�Ϊ����
     */
    private boolean isYaxisInt = false;

    /**
     * ����X��������ʾ���� ������ʱ��ͼ
     */
    private String DateType;

    /**
     * ����Y�����ֵ
     */
    private double yMaxValue = 0;

    /**
     * ����Y����Сֵ
     */
    private double yMixValue = 0;


    /**
     * X����ʾ��ֵ
     */
    private ArrayList XCordValue = new ArrayList();

    /**
     * X��ı��� ����ֵʱ Ĭ����ʾʱ��
     */
    private String XCordUnitName = "ʱ��";

    /**
     * Y����� ����ֵʱ Ĭ����ʾ��ֵ
     */
    private String YCordUnitName = "��ֵ";


    /**
     * �����õ���һ��Y��ĵ�λ һ�㲻��
     */
    private String OthYUnitName = "";

    /**
     * ͼ�����е�����
     */
    private ArrayList SeriesName = new ArrayList();

    /**
     * ͼ����ֵ����
     */
    private ArrayList SeriesValue = new ArrayList();
    /**
     * ͼ���·���ͼ���Ƿ���ʾ
     */
    private boolean LegendVis = true;
    /**
     * ʱ��ͼ��Ҫ������dataset ����ʱ��Ҫ����dataset1
     */
    TimeSeriesCollection dataset = new TimeSeriesCollection();
    TimeSeriesCollection dataset1 = new TimeSeriesCollection();

    private DefaultCategoryDataset DataSet = new DefaultCategoryDataset();


    private DefaultCategoryDataset LineDataSet = new DefaultCategoryDataset();

    public DefaultPieDataset DataSet2 = new DefaultPieDataset(); // ��ͼ���ݼ�

    public Chart() {

    }

    /**
     *
     * @param maintitle
     *            ������
     */
    public Chart(String maintitle) {
        MainTitle = maintitle;
    }

    /**
     * // * ���ͼ����ʾ����ֵ����
     *
     * @param seriesName
     * @param seriesValue
     *            ��ֵ����
     */
    public void addSeries(String seriesName, ArrayList seriesValue) {
        // SeriesName = new ArrayList();
        // SeriesValue = new ArrayList();
        SeriesName.add(seriesName);
        SeriesValue.add(seriesValue);
    }

    /**
     * ����ͨ��DataSet
     *
     */
    private void createDataSet() {

        String seriesName;
        // ��������list
        ArrayList singleSeriesList = null;
        // ��������list��Ԫ��ֵ
        String singleSeriesValue;
        if (listIsNull(SeriesName, XCordValue, SeriesValue)) {
            // ѭ�����еĸ���
            for (int i = 0; i < SeriesValue.size(); i++) {
                seriesName = (String) SeriesName.get(i);
                singleSeriesList = (ArrayList) SeriesValue.get(i);
                if (singleSeriesList != null) {
                    // ѭ��ÿ������(X��)
                    for (int j = 0; j < XCordValue.size(); j++) {
                        if (j >= singleSeriesList.size()) {
                            singleSeriesValue = null;
                        } else {
                            singleSeriesValue = (String) singleSeriesList
                                    .get(j);
                        }

                        // �ж��Ƿ�Ϊ��ֵ��
                        if (isNull(singleSeriesValue)) {
                            DataSet.addValue(null, seriesName,
                                    (String) XCordValue.get(j));
                        } else {
                            DataSet.addValue(getStr2Double(singleSeriesValue),
                                    seriesName,
                                    (String) XCordValue.get(j));
                        }
                    }
                }
            }
        } else {
            if (SeriesName == null) {
                System.err.println("ͼ�����е�����Ϊnull!");
            } else if (SeriesValue == null) {
                System.err.println("ͼ����ֵ����Ϊnull!");
            } else if (SeriesName.size() != SeriesValue.size()) {
                System.err.println("ͼ�����е����ƺ�ͼ����ֵ���и��������!");
            }
        }

    }

    /**
     * ������ͼ��DataSet
     *
     */
    public void createPieDataSet() {
        ArrayList singleSeriesList = null;
        String singleSeriesValue;
        if (listIsNull(SeriesName, XCordValue, SeriesValue)) {

            for (int i = 0; i < SeriesValue.size(); i++) {
                singleSeriesList = (ArrayList) SeriesValue.get(i);
                if (singleSeriesList != null) {
                    for (int j = 0; j < XCordValue.size(); j++) {
                        if (singleSeriesList.get(j) == null) {
                            singleSeriesValue = new String("");
                        } else {
                            singleSeriesValue = new String(
                                    (String) singleSeriesList.get(j));
                        }

                        if (isNull(singleSeriesValue)) {
                            DataSet2.setValue((String) XCordValue.get(j), null);
                        } else {
                            DataSet2.setValue((String) XCordValue.get(j),
                                    getStr2Double(singleSeriesValue));
                        }
                    }
                }
            }
        } else {
            if (SeriesName == null) {
                System.err.println("ͼ�����е�����Ϊnull!");
            } else if (SeriesValue == null) {
                System.err.println("ͼ����ֵ����Ϊnull!");
            } else if (SeriesName.size() != SeriesValue.size()) {
                System.err.println("ͼ�����е����ƺ�ͼ����ֵ���и��������!");
            }
        }
    }

    /**
     * ��������ͼ
     *
     * @param width
     *            ���
     * @param height
     *            �߶�
     * @param info
     *            ͼ��������Ϣ
     * @param session
     * @return
     */
    public String createLineChart(int width, int height,
                                  ChartRenderingInfo info, HttpSession session) {
        try {
            JFreeChart chart = createLineChart();
            return ServletUtilities.saveChartAsPNG(chart, width, height, info,
                    session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ��������ͼ
     *
     * @return JFreeChart
     */
    public JFreeChart createLineChart() {
        createDataSet();

        // create the chart...
        JFreeChart chart = ChartFactory.createLineChart(MainTitle, // �����
                XCordUnitName, // X��ĵ�λ
                YCordUnitName, // Y��ĵ�λ
                DataSet, // ����
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        );
        // ��������ͼ��ı�����ɫ
        GradientPaint bgGP = new GradientPaint(0, 1000, Color.cyan, 0, 0,
                Color.WHITE, false);
        chart.setBackgroundPaint(bgGP);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        NumberAxis numberaxisbar = (NumberAxis) plot.getRangeAxis();
        if (yMaxValue == 0) {
            numberaxisbar.setAutoRange(true);
        } else {
            numberaxisbar.setUpperBound(yMaxValue);
        }
        numberaxisbar.setLowerBound(yMixValue);

        chart.setBorderVisible(true);
        GradientPaint bgBorderGP = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0,
                0, Color.LIGHT_GRAY, false);
        chart.setBorderPaint(bgBorderGP);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions
                .createUpRotationLabelPositions(
                        0.8));
        domainAxis.setMaximumCategoryLabelWidthRatio(20F);
        plot.setNoDataMessage("û�д�����Ӧ��������ʾͼƬ");
        if (SecondTitle != null) {
            chart.addSubtitle(new TextTitle(SecondTitle, subTitleFont)); // ��Ӹ�����
        }

        if (ThirdTitle != null) {
            chart.addSubtitle(new TextTitle(ThirdTitle, subTitleFont)); // ��Ӹ�����
        }

        return chart;
    }

    /**
     * ����ͼ
     *
     * @return JFreeChart
     */
    public String createPieChart(int width, int height,
                                 ChartRenderingInfo info, HttpSession session) {
        try {
            JFreeChart chart = createPieChart();
            return ServletUtilities.saveChartAsPNG(chart, width, height, info,
                    session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ��������ͼ
     *
     * @return
     */
    private JFreeChart createPieChart() {
        createPieDataSet();
        JFreeChart chart = ChartFactory.createPieChart(MainTitle, // �����
                DataSet2, // ����
                true, // include legend
                true, // tooltips
                false // urls
        );
        GradientPaint bgGP = new GradientPaint(0, 1000, Color.cyan, 0, 0,
                Color.WHITE, false);
        chart.setBackgroundPaint(bgGP);

        PiePlot pie = (PiePlot) chart.getPlot();
//        pie.setLabelGenerator(new StandardPieItemLabelGenerator("{0}={1}({2})",
//                NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
//        pie.setLegendLabelGenerator(new StandardPieItemLabelGenerator("{0}"));
        pie.setNoDataMessage("û�д�����Ӧ��������ʾͼƬ");
        if (SecondTitle != null) {
            chart.addSubtitle(new TextTitle(SecondTitle, subTitleFont)); // ��Ӹ�����
        }
        if (ThirdTitle != null) {
            chart.addSubtitle(new TextTitle(ThirdTitle, subTitleFont)); // ��Ӹ�����
        }
        return chart;
    }
    /**
     * 3D����ͼ
     *
     * @return JFreeChart
     */
    public String createPieChart3D(int width, int height,
                                   ChartRenderingInfo info, HttpSession session) {
        try {
            JFreeChart chart = createPieChart3D();
            return ServletUtilities.saveChartAsPNG(chart, width, height, info,
                    session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * ����3D����ͼ
     *
     * @return
     */
    private JFreeChart createPieChart3D() {
        createPieDataSet();
        JFreeChart chart = ChartFactory.createPieChart3D(MainTitle, // �����
                DataSet2, // ����
                true, // include legend
                true, // tooltips
                false // urls
        );
        GradientPaint bgGP = new GradientPaint(0, 1000, Color.cyan, 0, 0,
                Color.WHITE, false);
        chart.setBackgroundPaint(bgGP);

        PiePlot pie = (PiePlot) chart.getPlot();
//        pie.setLabelGenerator(new StandardPieItemLabelGenerator("{0}={1}({2})",
//                NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
//        pie.setLegendLabelGenerator(new StandardPieItemLabelGenerator("{0}"));
        pie.setNoDataMessage("û�д�����Ӧ��������ʾͼƬ");
        if (SecondTitle != null) {
            chart.addSubtitle(new TextTitle(SecondTitle, subTitleFont)); // ��Ӹ�����
        }
        if (ThirdTitle != null) {
            chart.addSubtitle(new TextTitle(ThirdTitle, subTitleFont)); // ��Ӹ�����
        }
        return chart;
    }

    /**
     * ������״ͼ
     *
     * @return JFreeChart
     */
    public String createBarChart(int width, int height,
                                 ChartRenderingInfo info, HttpSession session) {
        try {
            JFreeChart chart = createBarChart();
            return ServletUtilities.saveChartAsPNG(chart, width, height, info,
                    session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ������״ͼ
     *
     * @return JFreeChart
     */
    private JFreeChart createBarChart() {
        createDataSet();
        JFreeChart chart = ChartFactory.createBarChart3D(MainTitle, // �����
                XCordUnitName, // X��ĵ�λ
                YCordUnitName, // Y��ĵ�λ
                DataSet, // ����
                PlotOrientation.VERTICAL, true, true, false);

        // ��������ͼ��ı�����ɫ
        GradientPaint bgGP = new GradientPaint(0, 1000, Color.cyan, 0, 0,
                Color.WHITE, false);
        chart.setBackgroundPaint(bgGP);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        NumberAxis numberaxisbar = (NumberAxis) plot.getRangeAxis();
        if (yMaxValue == 0) {
            numberaxisbar.setAutoRange(true);
        } else {
            numberaxisbar.setUpperBound(yMaxValue);
        }
        numberaxisbar.setLowerBound(yMixValue);
        final BarRenderer render = (BarRenderer) plot.getRenderer();
//        render.setItemURLGenerator(new StandardCategoryURLGenerator("#"));
//        render.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        render.setItemLabelFont(dateLabelFont);
        render.setItemLabelsVisible(true);//��ÿ������������ʾ��ֵ
//        render.setMaxBarWidth(1.0);
        render.setItemLabelsVisible(true);
        render.setMinimumBarLength(0);
        render.setPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12,TextAnchor.BASELINE_LEFT));
        plot.setRenderer(render);

        chart.setBorderVisible(true);
        GradientPaint bgBorderGP = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0,
                0, Color.LIGHT_GRAY, false);
        chart.setBorderPaint(bgBorderGP);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions
                .createUpRotationLabelPositions(
                        0.8));
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions
        // .createDownRotationLabelPositions(1.2));
        domainAxis.setMaximumCategoryLabelWidthRatio(20F);

        ValueAxis valueaxis = plot.getRangeAxis();
        plot.setNoDataMessage("û�д�����Ӧ��������ʾͼƬ");
        if (isYaxisInt) {
            valueaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        } else {
            valueaxis.setAutoRange(true);
        }
        plot.setRangeAxis(valueaxis);


        if (SecondTitle != null) {
            chart.addSubtitle(new TextTitle(SecondTitle, subTitleFont)); // ��Ӹ�����
        }
        if (ThirdTitle != null) {
            chart.addSubtitle(new TextTitle(ThirdTitle, subTitleFont)); // ��Ӹ�����
        }

        return chart;
    }
    /**
     * ����ʱ��ͼ
     *
     * @param width
     *            ���
     * @param height
     *            �߶�
     * @param info
     *            ͼ��������Ϣ
     * @param session
     * @return
     */
    public String createTimeChart(int width, int height,
                                  ChartRenderingInfo info, HttpSession session) {
        try {
            JFreeChart chart = createTime();
            return ServletUtilities.saveChartAsPNG(chart, width, height, info,
                    session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * ����ʱ��ͼ,����ʱ��ͼ
     *
     * @return JFreeChart
     */
    private JFreeChart createTime() {
        createDataSetTime();
        JFreeChart chart = ChartFactory.createTimeSeriesChart(MainTitle,// �����
                XCordUnitName, // X��ĵ�λ
                YCordUnitName, // Y��ĵ�λ
                dataset, // ����
                LegendVis, // include legend
                true, // tooltips
                false // urls
        );
        GradientPaint bgGP = new GradientPaint(0, 1000, Color.cyan, 0, 0,
                Color.WHITE, false);
        chart.setBackgroundPaint(bgGP);
        chart.setBorderVisible(true);
        GradientPaint bgBorderGP = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0,
                0, Color.LIGHT_GRAY, false);
        chart.setBorderPaint(bgBorderGP);
        try {
            XYPlot plot = chart.getXYPlot();
            plot.setDomainGridlinesVisible(true);
            // XYItemRenderer renderer = plot.getRenderer();
            // if (renderer instanceof XYLineAndShapeRenderer)
            // {
            // XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
            // rr.setDefaultShapesVisible(true);
            // rr.setDefaultShapesFilled(true);
            //
            // }
            //
            DateAxis axis = (DateAxis) plot.getDomainAxis();
            if (DateType != null) {
                axis.setDateFormatOverride(new SimpleDateFormat(DateType));
            } else {
                axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
            }
            // axis.setVerticalTickLabels(true);
            axis.setAutoRange(true);
            ValueAxis valueaxis = plot.getRangeAxis();
            if (isYaxisInt) {
                valueaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            } else {
                valueaxis.setAutoRange(true);
            }
            plot.setRangeAxis(valueaxis);
            plot.setNoDataMessage("û�д�����Ӧ��������ʾͼƬ");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SecondTitle != null)
            // System.out.println("����");
            chart.addSubtitle(new TextTitle(SecondTitle, subTitleFont));// ��Ӹ�����
        if (ThirdTitle != null)
            chart.addSubtitle(new TextTitle(ThirdTitle, subTitleFont));// ��Ӹ�����

        return chart;
    }
    /**
     * ����ʱ��ͼ��dataset
     *
     */
    private void createDataSetTime() {
        String seriesName;
        ArrayList singleSeriesList = null;
        String singleSeriesValue = null;
        TimeSeries series = null;
        // ȷ��ÿ�β�������ж��Ǳ��ε�û����ǰ������������
        dataset.removeAllSeries();
        try {
            if (listIsNull(SeriesName, XCordValue, SeriesValue)) {
                for (int i = 0; i < SeriesValue.size(); i++) {
                    seriesName = (String) SeriesName.get(i);
                    singleSeriesList = (ArrayList) SeriesValue.get(i);
                    series = new TimeSeries(seriesName, Second.class);
                    for (int j = 0; j < XCordValue.size(); j++) {
                        if (j < singleSeriesList.size()) {
                            singleSeriesValue = (String) singleSeriesList
                                    .get(j);
                            String record = (String) XCordValue.get(j);
                            if (DateType == null) {
                                DateType = "yyyy-MM-dd HH:mm";
                            }
                            SimpleDateFormat format = new SimpleDateFormat(
                                    DateType);
                            Date date;
                            if (record != null) {
                                date = format.parse(record);
                                if (isNull(singleSeriesValue)) {
                                    series.addOrUpdate(new Second(date), null);
                                } else {
                                    series.addOrUpdate(new Second(date),
                                            getStr2Double(singleSeriesValue));
                                }
                            }
                        }
                    }
                    if (series != null) {
                        dataset.addSeries(series);
                    }
                }
            } else {
                if (SeriesName == null) {
                    System.err.println("ͼ�����е�����Ϊnull!");
                } else if (SeriesValue == null) {
                    System.err.println("ͼ����ֵ����Ϊnull!");
                } else if (SeriesName.size() != SeriesValue.size()) {
                    System.err.println("ͼ�����е����ƺ�ͼ����ֵ���и��������!");
                }
            }
        } catch (ParseException e) {
            System.err.println("���ڸ�ʽ����ȷ!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

//***********************************************************************************




    /**
     * �õ�������
     *
     * @return
     */
    public String getMainTitle() {
        return MainTitle;
    }

    /**
     * ����������
     *
     * @param mainTitle
     */
    public void setMainTitle(String mainTitle) {
        MainTitle = mainTitle;
    }

    /**
     * �õ�������
     *
     * @return
     */
    public String getSecondTitle() {
        return SecondTitle;
    }

    /**
     * ���ø�����
     *
     * @param secondTitle
     */
    public void setSecondTitle(String secondTitle) {
        SecondTitle = secondTitle;
    }

    /**
     * �õ�����������
     *
     * @return
     */
    public Font getSubTitleFont() {
        return subTitleFont;
    }

    /**
     * ���ø���������
     *
     * @param subTitleFont
     */
    public void setSubTitleFont(Font subTitleFont) {
        this.subTitleFont = subTitleFont;
    }

    /**
     * �õ�С����
     *
     * @return
     */
    public String getThirdTitle() {
        return ThirdTitle;
    }

    /**
     * ����С����
     *
     * @param thirdTitle
     */
    public void setThirdTitle(String thirdTitle) {
        ThirdTitle = thirdTitle;
    }

    /**
     * �õ�X�ᵥλ����
     *
     * @return
     */
    public String getXCordUnitName() {
        return XCordUnitName;
    }

    /**
     * ����X�ᵥλ����
     *
     * @param cordUnitName
     */
    public void setXCordUnitName(String cordUnitName) {
        XCordUnitName = cordUnitName;
    }

    /**
     * �õ�X����ֵ����
     *
     * @return
     */
    public ArrayList getXCordValue() {
        return XCordValue;
    }

    /**
     * ����X����ֵ����
     *
     * @param cordValue
     */
    public void setXCordValue(ArrayList cordValue) {
        XCordValue = cordValue;
    }

    /**
     * �õ�Y�ᵥλ����
     *
     * @return
     */
    public String getYCordUnitName() {
        return YCordUnitName;
    }

    /**
     * ����Y�ᵥλ����
     *
     * @param cordUnitName
     */
    public void setYCordUnitName(String cordUnitName) {
        YCordUnitName = cordUnitName;
    }


    /**
     * �õ���״ͼ��ֵ����Ƿ�Ϊ����
     *
     * @return
     */
    public boolean isYaxisInt() {
        return isYaxisInt;
    }

    /**
     * ������״ͼ��ֵ����Ƿ�Ϊ����
     *
     * @param isYaxisInt
     */
    public void setYaxisInt(boolean isYaxisInt) {
        this.isYaxisInt = isYaxisInt;
    }


    /**
     * ���ַ����н���double
     *
     * @param str
     * @return ������쳣�ַ�������Ϊ0
     */
    private double getStr2Double(String str) {
        double dou = 0;
        try {
            if (str != null || (str != null && str.trim().length() > 0)) {
                str = str.replaceAll(",", "");
                dou = Double.parseDouble(str);
            }
        } catch (Exception e) {
            System.err.println("���ܽ�����ֵ���ַ���ת������ֵ!");
        }
        return dou;
    }

    /**
     * �ж��ַ����Ƿ�Ϊ��:null,���ַ����Ϳո���ַ�����Ϊ��
     *
     * @param str
     * @return
     */
    private boolean isNull(String str) {
        boolean isnull = true;
        if (str == null || (str != null && str.trim().length() < 1)) {
            isnull = true;

        } else {
            isnull = false;
        }
        return isnull;
    }

    /**
     * �жϴ��������list�Ƿ�Ϊ�� �Լ�ͼ����������list�Ĵ�С�Ƿ����ͼ��������ֵlist�Ĵ�С
     *
     * @param seriesname
     * @param xcordvalue
     * @param seriesvalue
     * @return
     */
    private boolean listIsNull(ArrayList seriesname, ArrayList xcordvalue,
                               ArrayList seriesvalue) {
        boolean listisnull = true;
        if (seriesname != null && xcordvalue != null) {
            listisnull = true;
        } else {
            listisnull = false;
        }
        return listisnull;
    }


    public String getOthYUnitName() {
        return OthYUnitName;
    }

    public void setOthYUnitName(String othYUnitName) {
        OthYUnitName = othYUnitName;
    }

    public double getYMaxValue() {
        return yMaxValue;
    }

    public void setYMaxValue(double maxValue) {
        yMaxValue = maxValue;
    }

    public double getYMixValue() {
        return yMixValue;
    }

    public void setYMixValue(double mixValue) {
        yMixValue = mixValue;
    }

    public void setDateType(String dateType) {
        DateType = dateType;
    }


}
