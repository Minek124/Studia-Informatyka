import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class FixationDetection extends Application {

    static XYChart.Series series = new XYChart.Series();
    static XYChart.Series series2 = new XYChart.Series();
    static XYChart.Series series3 = new XYChart.Series();
    static int imgToShow = -1;
    static int dispersion_threshold = 50;
    static int duration_threshold = 100;
    static int minimum_saccade_amplitude = 90;
    static int tolerance = 3;

    @Override
    public void start(Stage stage) {
        stage.setTitle("sss");
        final NumberAxis xAxis = new NumberAxis(-1200, 1200, 100);
        final NumberAxis yAxis = new NumberAxis(-1600, 1600, 100);
        final ScatterChart<Number, Number> sc = new ScatterChart<Number, Number>(xAxis, yAxis);
        stage.setMinWidth(1100);
        stage.setMinHeight(600);
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        sc.getStylesheets().add("chart.css");
        sc.getData().addAll(series,series2,series3);
        Scene scene = new Scene(sc, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    void showSubject(String subject[]){
        for (int i = 2; i < subject.length; i = i + 2) {
            double x = Double.parseDouble(subject[i]);
            double y = Double.parseDouble(subject[i + 1]);
            series.getData().add(new XYChart.Data(x, y));
        }
        launch();
    }

    class Fixation {
        public Fixation(Point2D p, int d, double s) {
            this.center = p;
            this.duration = d;
            this.size = s;
        }

        Point2D center;
        double size;
        int duration;
    }

    class Saccade {
        public Saccade(Point2D s, Point2D e, int d, double r) {
            start = new Point();
            end = new Point();
            this.start.setLocation(s);
            this.end.setLocation(e);
            this.duration = d;
            this.realDistance = r;
        }
        double amplitude(){
            return start.distance(end);
        }
        Point2D start;
        Point2D end;
        int duration;
        double realDistance;
    }

    class Result {
        public Result() {
            mfd_false = 0;
            mfd_true = 0;
            mfd_overall = 0;
            msa_false = 0;
            msa_true = 0;
            msa_overall = 0;
            mfd_sd_false = 0;
            mfd_sd_true = 0;
            mfd_sd_overall = 0;
            msa_sd_true = 0;
            msa_sd_false = 0;
            msa_sd_overall = 0;
            msa_num_true = 0;
            mfd_num_true = 0;
            msa_num_false = 0;
            mfd_num_false = 0;
        }
        public double mfd_false;
        public double mfd_true;
        public double mfd_overall;
        public double msa_false;
        public double msa_true;
        public double msa_overall;
        public double mfd_sd_false;
        public double mfd_sd_true;
        public double mfd_sd_overall;
        public double msa_sd_true;
        public double msa_sd_false;
        public double msa_sd_overall;
        public int msa_num_true;
        public int mfd_num_true;
        public int msa_num_false;
        public int mfd_num_false;
    }

    ArrayList<Fixation> dispersionThresholdAlgorithm(String[] subject, double threshold, int duration, int tolerance_threshold) {
        boolean reset = true;
        int count = 0;
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;
        double oldMinX = 0;
        double oldMaxX = 0;
        double oldMinY = 0;
        double oldMaxY = 0;
        int tolerance = 0;
        ArrayList<Fixation> fixations = new ArrayList<>();
        for (int i = 4; i < subject.length; i = i + 2) {
            count++;
            double x = Double.parseDouble(subject[i]);
            double y = Double.parseDouble(subject[i + 1]);
            //double prevX = Double.parseDouble(subject[i - 2]);
            //double prevY = Double.parseDouble(subject[i - 1]);
            if (reset) {
                minX = x;
                maxX = x;
                minY = y;
                maxY = y;
                oldMinX = x;
                oldMaxX = x;
                oldMinY = y;
                oldMaxY = y;
                reset = false;
            }
            if (x < minX) {
                oldMinX = minX;
                minX = x;
            }
            if (x > maxX) {
                oldMaxX = maxX;
                maxX = x;
            }
            if (y < minY) {
                oldMinY = minY;
                minY = y;
            }
            if (y > maxY) {
                oldMaxY = maxY;
                maxY = y;
            }
            //double velocity = Math.sqrt((x - prevX) * (x - prevX) + (y - prevY) * (y - prevY));
            double dispersion = Math.max(maxX - minX, maxY - minY); //very simplified
            //System.out.println(count + " " + x + " " + y + " " + dispersion + " " + velocity);
            if (dispersion > threshold) {
                tolerance++;
                minX = oldMinX;
                maxX = oldMaxX;
                minY = oldMinY;
                maxY = oldMaxY;
                if (count >= duration && tolerance >= tolerance_threshold) {
                    Point2D center = new Point();
                    center.setLocation((maxX + minX) / 2, (maxY + minY) / 2);
                    fixations.add(new Fixation(center, count, (maxX - minX > maxY - minY ? maxX - minX : maxY - minY)));
                    count = 0;
                    reset = true;
                } else if (tolerance >= tolerance_threshold) {
                    i = i - (count * 2) + 2;
                    count = 0;
                    reset = true;
                }
            } else {
                tolerance = 0;
            }
        }
        if (count >= duration) {
            Point2D center = new Point();
            center.setLocation((maxX + minX) / 2, (maxY + minY) / 2);
            fixations.add(new Fixation(center, count, (maxX - minX > maxY - minY ? maxX - minX : maxY - minY)));
        }
        return fixations;
    }

    ArrayList<Saccade> saccadeDetection(String[] subject, double threshold, double amplitude_threshold, int tolerance_threshold) {
        int count = 0;
        double realDistance = 0;
        Point2D start = new Point();
        Point2D current = new Point();
        Point2D prev = new Point();
        start.setLocation(Double.parseDouble(subject[2]), Double.parseDouble(subject[3]));
        int tolerance = 0;
        ArrayList<Saccade> saccades = new ArrayList<>();
        for (int i = 4; i < subject.length; i = i + 2) {
            count++;
            current.setLocation(Double.parseDouble(subject[i]), Double.parseDouble(subject[i + 1]));
            prev.setLocation(Double.parseDouble(subject[i - 2]), Double.parseDouble(subject[i - 1]));

            double velocity = current.distance(prev);
            double amplitude = current.distance(start);
            //System.out.println(count + " " + current.getX() + " " + current.getY() + " " + velocity);
            if (velocity < threshold) {
                tolerance++;
                if (amplitude >= amplitude_threshold && tolerance >= tolerance_threshold) {
                    saccades.add(new Saccade(start, current, count, realDistance));
                    count = 0;
                    start.setLocation(current);
                    realDistance = 0;
                } else if (tolerance >= tolerance_threshold) {
                    count = 0;
                    start.setLocation(current);
                    realDistance = 0;
                }
            } else {
                tolerance = 0;
                realDistance += velocity;
            }
        }
        if (current.distance(start) >= amplitude_threshold) {
            saccades.add(new Saccade(start, current, count, realDistance));
        }
        return saccades;
    }

    public void process(String[] args, ArrayList<String[]> data) throws IOException {
        FileWriter meansWriter = new FileWriter("means.csv");
        FileWriter saccadeWriter = new FileWriter("saccades.csv");
        FileWriter fixationWriter = new FileWriter("fixations.csv");
        saccadeWriter.append("subject,known,amplitude\n");
        fixationWriter.append("subject,known,amplitude,x,y\n");

        int it = 0;
        HashMap<String, Result> results = new HashMap<>();
        for (String[] subject : data) {
            for (int ii = 1; ii < args.length; ii++) {
                if (args[ii].equals(subject[0])) {
                    System.out.println(subject[0] + "[" + it + "][" + subject[1] + "] RESULTS:");
                    ArrayList<Fixation> fixations = dispersionThresholdAlgorithm(subject, dispersion_threshold, duration_threshold, tolerance);
                    ArrayList<Saccade> saccades = saccadeDetection(subject, 6, minimum_saccade_amplitude, tolerance);
                    double MFD = 0;
                    double MSA = 0;
                    double MFD_SD = 0;
                    double MSA_SD = 0;
                    for (Fixation f : fixations) { //fixations
                        MFD += f.duration;
                        MFD_SD += f.duration * f.duration;
                        System.out.println("Fixation(" + f.center.getX() + "," + f.center.getY() + ") duration:" + f.duration + " size:" + Math.round(f.size)/* + " saccade:" + Math.round(distance)*/);
                        fixationWriter.append(subject[0] + "," + subject[1] + "," + Double.toString(f.duration) + "," + Double.toString(f.center.getX()) + "," + Double.toString(f.center.getX()) + "\n");
                    }
                    for(Saccade s : saccades){
                        MSA += s.amplitude();
                        MSA_SD += s.amplitude() * s.amplitude();
                        System.out.println("Saccade:(" + s.start.getX() + "," + s.start.getY() + ")->(" + s.end.getX() + "," + s.end.getY() + ") amplitude:" + Math.round(s.amplitude()) + " pathLength:" + Math.round(s.realDistance));
                        saccadeWriter.append(subject[0] + "," + subject[1] + "," + Double.toString(s.amplitude()) + "\n");

                    }

                    System.out.println("TOTAL:" + subject.length / 2 + " FIXATE_TIME:" + Math.round(100 * MFD / (subject.length / 2)) + "% AVG_FIXATION:" + Math.round(MFD/fixations.size()) + " COUNT:" + fixations.size() + " MSA:" + Math.round(MSA/saccades.size()));
                    if(Boolean.parseBoolean(subject[1])){
                        Result r;
                        if(results.get(subject[0]) == null){
                            r = new Result();
                            results.put(subject[0], r);
                        }else{
                            r = results.get(subject[0]);
                        }
                        r.mfd_num_true += fixations.size();
                        r.msa_num_true += saccades.size();
                        r.mfd_true += MFD;
                        r.msa_true += MSA;
                        r.mfd_sd_true += MFD_SD;
                        r.msa_sd_true += MSA_SD;
                    }else{
                        Result r;
                        if(results.get(subject[0]) == null){
                            r = new Result();
                            results.put(subject[0], r);
                        }else{
                            r = results.get(subject[0]);
                        }
                        r.mfd_num_false += fixations.size();
                        r.msa_num_false += saccades.size();
                        r.mfd_false += MFD;
                        r.msa_false += MSA;
                        r.mfd_sd_false += MFD_SD;
                        r.msa_sd_false += MSA_SD;
                    }
                    if(it == imgToShow){
                        for (Fixation f : fixations) {
                            series2.getData().add(new XYChart.Data(f.center.getX(), f.center.getY()));
                        }
                        /*for (Saccade s : saccades) {
                            series3.getData().add(new LineChart.Data(s.start.getX(), s.start.getY()));
                            series3.getData().add(new LineChart.Data(s.end.getX(), s.end.getY()));
                        }*/
                        showSubject(subject);

                    }
                    it++;
                }
            }
        }
        
        meansWriter.append("Subject,MFD_true,MFD_SD_true,MFD_false,MFD_SD_false,MSA_true,MSA_SD_true,MSA_false,MSA_SD_false,MFD_overall,MFD_overall_SD,MSA_overall,MSA_overall_SD\n");
        System.out.println("subject MFD_true MFD_false MSA_true MSA_false");
        for(int i = 1;i< args.length;i++){
            Result r = results.get(args[i]);
            if(r == null){
                continue;
            }
            int mfd_num_overall = (r.mfd_num_true + r.mfd_num_false);
            int msa_num_overall = (r.msa_num_true + r.msa_num_false);
            r.mfd_overall = r.mfd_true + r.mfd_false;
            r.msa_overall = r.msa_true + r.msa_false;
            r.mfd_sd_overall = r.mfd_sd_true + r.mfd_sd_false;
            r.msa_sd_overall = r.msa_sd_true + r.msa_sd_false;
            r.mfd_true /= r.mfd_num_true;
            r.msa_true /= r.msa_num_true;
            r.mfd_false /= r.mfd_num_false;
            r.msa_false /= r.msa_num_false;
            r.mfd_overall /= mfd_num_overall;
            r.msa_overall /= msa_num_overall;

            r.msa_sd_false = Math.sqrt((r.msa_sd_false / r.msa_num_false) - (r.msa_false * r.msa_false));
            r.msa_sd_true = Math.sqrt((r.msa_sd_true / r.msa_num_true) - (r.msa_true * r.msa_true));
            r.msa_sd_overall = Math.sqrt((r.msa_sd_overall / msa_num_overall) - (r.msa_overall * r.msa_overall));

            r.mfd_sd_false = Math.sqrt((r.mfd_sd_false / r.mfd_num_false) - (r.mfd_false * r.mfd_false));
            r.mfd_sd_true = Math.sqrt((r.mfd_sd_true / r.mfd_num_true) - (r.mfd_true * r.mfd_true));
            r.mfd_sd_overall = Math.sqrt((r.mfd_sd_overall / mfd_num_overall) - (r.mfd_overall * r.mfd_overall));

            System.out.println(args[i] + " " + Math.round(r.mfd_true) + " " + Math.round(r.mfd_false) + " " + Math.round(r.msa_true) + " " + Math.round(r.msa_false));

            meansWriter.append(args[i] + "," + Double.toString(r.mfd_true) + "," + Double.toString(r.mfd_sd_true) + "," + Double.toString(r.mfd_false) + "," + Double.toString(r.mfd_sd_false) + "," + Double.toString(r.msa_true) + "," + Double.toString(r.msa_sd_true) + "," + Double.toString(r.msa_false) + "," + Double.toString(r.msa_sd_false) + "," + Double.toString(r.mfd_overall) + "," + Double.toString(r.mfd_sd_overall) + "," + Double.toString(r.msa_overall) + "," + Double.toString(r.msa_sd_overall) + "\n");
        }
        meansWriter.flush();
        meansWriter.close();
        saccadeWriter.flush();
        saccadeWriter.close();
        fixationWriter.flush();
        fixationWriter.close();
    }


    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java FixationDetection sample_file subjects+ [options]");
            System.out.println("Example: java FixationDetection train.csv s8 s18 s28 s4 s14 s24");
            System.out.println("Options: --show=subject_number eg --show=0 - show eye path with fixations");
            System.out.println("         --dt=? default: --dt=70 - dispersion threshold");
            System.out.println("         --min=? default: --min=100 - minimum fixation duration");
            System.out.println("         --s=? default: --s=90 - minimum saccade amplitude");
            System.out.println("         --tolerance=? default: --tolerance=3 - noise tolerance");
            return;
        }
        for(String a : args){
            if(a.startsWith("--show=")){
                imgToShow = Integer.parseInt(a.substring(7));
            }
            if(a.startsWith("--dt=")){
                dispersion_threshold = Integer.parseInt(a.substring(5));
            }
            if(a.startsWith("--min=")){
                duration_threshold = Integer.parseInt(a.substring(6));
            }
            if(a.startsWith("--tolerance=")){
                tolerance = Integer.parseInt(a.substring(12));
            }
            if(a.startsWith("--s=")){
                minimum_saccade_amplitude = Integer.parseInt(a.substring(4));
            }
        }
        System.out.println("processing data...");
        System.out.println("Dispersion threshold:" + dispersion_threshold);
        System.out.println("Minimum duration threshold:" + duration_threshold);
        System.out.println("Noise tolerance:" + tolerance);
        System.out.println("Sample to show:" + (imgToShow >=0 ? imgToShow : "none"));
        ArrayList<String[]> data = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(args[0]));
            String line = "";
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        new FixationDetection().process(args, data);
    }
}
