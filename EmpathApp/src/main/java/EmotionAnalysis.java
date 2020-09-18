package main.java;

public class EmotionAnalysis {

    public static void main(String[] args) throws Exception {

        VoiceRecoder vr = new VoiceRecoder();
        EmpathApp ea = new EmpathApp();

        vr.main();
        ea.main();

    }
}
