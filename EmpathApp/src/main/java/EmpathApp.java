package main.java;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org. apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


public class EmpathApp {

    public static final String API_ENDPOINT = "https://api.webempath.net/v2/analyzeWav";

    public static void main() throws Exception {

    final String ANSI_RED = "\u001B[31m";
    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_YELLOW = "\u001B[33m";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
        .setMode(HttpMultipartMode.STRICT)
        .setContentType(ContentType.MULTIPART_FORM_DATA)
       .addTextBody("apikey", "sbSn3fuSS8KVyTZFHhrIiTbFJJrDlRIQZC-QIELDKKU")
        .addBinaryBody("wav", new java.io.File("assetforceData.wav"));

        HttpPost httpPost = new HttpPost(API_ENDPOINT);
         httpPost.setEntity(builder.build());
        try (CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse resp = client.execute(httpPost)) {
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝感情数値結果＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
                System.out.println("感情係数は０−５０の範囲で測定されています。\n");
                System.out.println("発表準備中....\n");
                sleep(3000);

                final String result = EntityUtils.toString(resp.getEntity());

                final JSONObject obj = new JSONObject(result);

                int error = (Integer) obj.get("error");
                int calm = 0;
                int anger = 0;
                int joy = 0;
                int sorrow = 0;
                int energy = 0;

                if(error == 0) {
                    calm = (Integer) obj.get("calm");
                    anger = (Integer) obj.get("anger");
                    joy = (Integer)  obj.get("joy");
                    sorrow = (Integer) obj.get("sorrow");
                    energy = (Integer) obj.get("energy");

                    System.out.println("落ち着き: " + calm);
                    System.out.println("怒り: " + anger);
                    System.out.println("楽しい: " + joy);
                    System.out.println("悲しい: " + sorrow);
                    System.out.println("やる気: " + energy);
                }else{
                    System.out.println("エラーが発生しました。でも大丈夫！Davidがすぐ直してくれます");
                    System.out.println("エラーログ：" + obj);
                }


                System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝感情分析結果＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");

                  String max = findMax(calm,joy,anger,sorrow,energy);
                  String min = findMin(calm,joy,anger,sorrow,energy);
                  Map<String, String> maxMap = new HashMap<>();
                  maxMap.put("calm","落ち着き");
                  maxMap.put("joy","楽しい");
                  maxMap.put("anger","怒り");
                  maxMap.put("sorrow","悲しみ");
                  maxMap.put("energy","やる気");

                  System.out.println(ANSI_RED+"\n分析の結果、あなたは"+maxMap.get(max)+"の感情特徴が一番強く現れています。\n");
                  System.out.println(ANSI_GREEN+""+maxMap.get(min)+"の感情特徴が一番弱く現れています。\n");

                switch (max) {
                    case "joy":
                        System.out.println(ANSI_YELLOW + "ところで何がこんなに楽しいの？");
                        break;
                    case "energy":
                        System.out.println(ANSI_YELLOW + "なんでそんなにやる気があるの？");
                        break;
                    case "sorrow":
                        System.out.println(ANSI_YELLOW + "何がそんなに悲しいの？");
                        break;
                }

                }
            }
        }




        public static String findMax(int calm, int joy, int anger, int sorrow, int energy) {

            int[] emotions = {calm, joy, anger, sorrow, energy};
            int max = emotions[0];

            for (int i = 1; i < emotions.length; i ++) {
                max = Math.max(max, emotions[i]);
            }
            if(max == calm){
                return "calm";
            }else if (max == joy){
                return "joy";
            }else if (max == anger){
                return "anger";
            }else if( max == sorrow){
                return "sorrow";
            }else{
                return "energy";
            }
        }

    public static String findMin(int calm, int joy, int anger, int sorrow, int energy) {

        int[] emotions = {calm, joy, anger, sorrow, energy};
        int min = emotions[0];

        for (int i = 1; i < emotions.length; i ++) {
            min = Math.min(min, emotions[i]);
        }
        if(min == calm){
            return "calm";
        }else if (min == joy){
            return "joy";
        }else if (min == anger){
            return "anger";
        }else if( min == sorrow){
            return "sorrow";
        }else{
            return "energy";
        }
    }



}

