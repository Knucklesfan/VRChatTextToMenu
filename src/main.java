import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    private static int counter = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.nanoTime();
        if (args.length <= 1) {
            System.out.println("Avatars 3.0 TEXT TO SUBMENU Help:");
            System.out.println("Usage: text2submenu.jar (pre/post) (link-to-plaintext/link-to-folder) [biggest]");
            System.out.println("Run pre in a folder OUTSIDE of your unity project. Then, run post INSIDE your unity project INSIDE the folder. (READ MY DOCUMENTATION)");
            System.out.println("TXT file must be in plaintext, UTF8!");

        } else {
            if (args[0].equals("pre")) {
                System.out.println("Processing " + args[1] + "... please wait!");
                File file = new File(args[1]);
                Scanner s = new Scanner(file);
                List<String> textForward = new ArrayList<String>();
                while (s.hasNextLine()) {
                    String tmp = s.nextLine();
                    if (!tmp.replaceAll(" ", "").equals("")) {
                        textForward.add(tmp);
                    }
                }
                for (int x = textForward.size() - 1; x > 0; x--) {
                    x = generatePage(textForward, x);
                }
            }
            else {
                System.out.println("Processing folder... please wait!");
                int oldFolder = Integer.parseInt(args[2])-1;
                for(int x = oldFolder; x > 0; x--) {
                    File read = new File(args[1]+"/menu"+(x-1)+".asset.meta");
                    File write = new File(args[1]+"/menu"+(x)+".asset");
                    Scanner readReader = new Scanner(read);
                    readReader.nextLine();
                    String guid = readReader.nextLine().replaceAll("guid: ","");

                    String content = Files.readString(write.toPath());
                    content = content.replaceAll("FIXMEDRISCOLL", "{fileID: 11400000, guid: "+guid+", type: 2}");
                    Files.write(write.toPath(), content.getBytes(StandardCharsets.UTF_8));

                }

            }
    }
        long endTime   = System.nanoTime();
        long totalTime = (endTime - startTime);
        double seconds = (double)totalTime / 1_000_000_000.0;
        DecimalFormat time = new DecimalFormat("##0.000");
        System.out.println("Done. Took " + time.format(seconds) + " seconds.");

}

    private static int generatePage(List<String> textForward, int x) throws IOException {
        FileWriter writer = new FileWriter("menu" + counter + ".asset");
        writer.write("%YAML 1.1\n" +
                "%TAG !u! tag:unity3d.com,2011:\n" +
                "--- !u!114 &11400000\n" +
                "MonoBehaviour:\n" +
                "  m_ObjectHideFlags: 0\n" +
                "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                "  m_PrefabInstance: {fileID: 0}\n" +
                "  m_PrefabAsset: {fileID: 0}\n" +
                "  m_GameObject: {fileID: 0}\n" +
                "  m_Enabled: 1\n" +
                "  m_EditorHideFlags: 0\n" +
                "  m_Script: {fileID: -340790334, guid: 67cc4cb7839cd3741b63733d5adf0442, type: 3}\n" +
                "  m_Name: menu"+counter+"\n" +
                "  m_EditorClassIdentifier: \n" +
                "  controls:\n");
        for(int i = 0; i < 8; i++) {

            if(x >= 0) {
                writer.write("  - name: " + textForward.get(x).replaceAll("-","").replaceAll("'"," ").replaceAll("\""," ").replaceAll(":","") +"\n" +
                        "    icon: {fileID: 2800000, guid: 814c40cdd23a7fe488c8826cf7d9b315, type: 3}\n" +
                        "    type: 103\n" +
                        "    parameter:\n" +
                        "      name: \n" +
                        "    value: 1\n" +
                        "    style: 0\n" +
                        "    subMenu: FIXMEDRISCOLL\n" +
                        "    subParameters: []\n" +
                        "    labels: []\n");
                x--;
            }

        }
        writer.close();
        counter++;
        return x;
    }


}
