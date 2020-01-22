import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        try {
            johnson(convertListIntoTable(getDataFromFile("input.txt")),
                    new PrintWriter(new FileWriter("output.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void johnson(int[][][] t, PrintWriter printWriter) throws IOException {
        int[] temp = new int[t.length];
        sNode(t,temp);
        for(int j = 0; j < t.length; j++){
            printWriter.write(Integer.toString(temp[j]));
            printWriter.write(" ");
        }
        printWriter.write("\n");
        reweigh(t,temp);
        for(int i = 1; i < t.length; i++){
            printWriter.write("Node " + i + ": ");
            for(int j = 1; j < t.length; j++){
                printWriter.write(Integer.toString(t[i][j][0]));
                printWriter.write(" ");
            }
            printWriter.write("\n");
        }
        printWriter.write("\n");
        //dijkstra give 3 deimensional array third dim is 1 or 0 . 0 for not visited nodes
        for(int i = 1; i < t.length; i++){
            int[] output = setupTable(t.length,i);
            dijkstra(i, t, output);
            printWriter.write("Node " + i + ": ");
            for(int j = 0; j < t.length; j++){
                printWriter.write(Integer.toString(output[j]));
                printWriter.write(" ");
            }
            printWriter.write("\n");
        }
        printWriter.close();
    }

    public static void sNode(int[][][] t, int[] temp){
        for(int j = 0 ; j < temp.length; j++) {
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] > t[j][i][0] + temp[i])
                    temp[i] = t[j][i][0] + temp[i];
            }
        }
    }
    public static void reweigh(int[][][] t, int[] temp){
        for(int i = 1; i < temp.length; i++){
            for(int j = 1; j < temp.length; j++)
                if(t[i][j][1] != 1)
                    t[i][j][0]=t[i][j][0]+temp[i]-temp[j];
        }
    }
    public  static void dijkstra(int prevoiusNode, int t[][][], int[] temp){
        for(int i = 1; i < t.length; i++){
            if(t[prevoiusNode][i][1] != 1 && t[prevoiusNode][i][0] != Integer.MAX_VALUE){
                if(temp[i]> temp[prevoiusNode] + t[prevoiusNode][i][0]){
                    temp[i]=temp[prevoiusNode] + t[prevoiusNode][i][0];
                }
                t[prevoiusNode][i][1] = 1;
                t[i][prevoiusNode][1] = 1;
                dijkstra(i,t,temp);
                t[prevoiusNode][i][1] = 0;
                t[i][prevoiusNode][1] = 0;
            }
        }
    }
    public static List<String> getDataFromFile(String filename){
        List<String> data = new ArrayList<>();
        try {
            data = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static int[][][] convertListIntoTable(List<String> data){

        int[][][] t = new int[data.size()+1][data.size()+1][2];
        StringBuilder strInteger = new StringBuilder();
        for(String str : data) {
            int sourceNode = 0;
            int goalNode = 0;
            int distance = 0;
            for (int i = 0; i < str.length();) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    if (str.charAt(i) == '[') {
                        i++;
                        while (str.charAt(i) != ']' && i < str.length()) {
                            strInteger.append(str.charAt(i));
                            i++;
                        }
                        sourceNode = Integer.valueOf(strInteger.toString());
                        strInteger = new StringBuilder();
                    } else if (str.charAt(i) == '(') {
                        i++;
                        while (str.charAt(i) != ')' && i < str.length()) {
                            strInteger.append(str.charAt(i));
                            i++;
                        }
                        distance = Integer.valueOf(strInteger.toString());
                        strInteger = new StringBuilder();
                        t[sourceNode][goalNode][0] = distance;
                    } else if(str.charAt(i) != '{' && str.charAt(i) != ']' && str.charAt(i) != '(' && str.charAt(i) != ')' && !Character.isWhitespace(str.charAt(i)) && i < str.length()){
                        while (!Character.isWhitespace(str.charAt(i)) && i < str.length() &&
                                (str.charAt(i) != '{' && str.charAt(i) != ']' && str.charAt(i) != '(' && str.charAt(i) != ')')) {
                            strInteger.append(str.charAt(i));
                            i++;
                        }
                        goalNode = Integer.valueOf(strInteger.toString());
                        strInteger = new StringBuilder();
                    } else i++;
                }else i++;
            }
        }

        for(int i = 0; i < t.length;i++){
            for(int j = 0; j < t.length; j++){
                if(t[i][j][0] == 0) {
                    t[i][j][1] = 1;
                    t[i][j][0] = Integer.MAX_VALUE;
                }
            }
        }
        return t;
    }
    public static int[] setupTable(int lenght, int startingNode){
        int[] temp = new int[lenght];
        for(int i = 0; i < lenght; i++){
            if(i==startingNode)
                temp[startingNode] = 0;
            else
                temp[i] = Integer.MAX_VALUE;
        }
        return temp;
    }
}
