package lolchamppools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.awt.Desktop;
import java.io.PrintWriter;
public class LOLChampPools {
    public static void main(String args[]) throws IOException {
        String[] mains = {"Illaoi", "Renekton"};
        String[] pool = {"Jax", "Wukong", "Darius"};
        String pos = "top";
        String rank = "iron";
        Champ[] champs = getChampList();
        setPool(champs, mains, pool);
        findCounters(champs, pos, rank);
        findPoolCounters(champs, pos, rank);
        getResults(champs);
    }
    public static Champ[] getChampList() throws IOException {
        URL u = new URL("https://leagueoflegends.fandom.com/wiki/List_of_champions");
        HttpURLConnection c = (HttpURLConnection)u.openConnection();
        c.setRequestProperty("User-Agent", "Mozilla/5.0");
        InputStream s = c.getInputStream();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        String line = br.readLine();
        Champ[] champs = new Champ[1];
        String num = "";
        String next = "";
        String champ ="";
        int a = 0;
        while (line != null) {
            line = line.replace("&#39;", "'");
            line = line.replace("&amp;", "&");
            if (line.contains("there are currently <b>")) {
                for (int i = (line.indexOf("there are currently <b>") + 23) ; !Character.toString(line.charAt(i)).equals(" ") ; i++) {
                    num = num.concat(Character.toString(line.charAt(i)));
                }
                champs = new Champ[Integer.parseInt(num)];
            }
            if (line.contains("The next champion to be released is <span class=\"inline-image label-after champion-icon\" data-param=\"\" data-champion=\"")) {
                for (int i = line.indexOf("The next champion to be released is <span class=\"inline-image label-after champion-icon\" data-param=\"\" data-champion=\"") + 118 ; !Character.toString(line.charAt(i)).equals("\"") ; i++) {
                    next = next.concat(Character.toString(line.charAt(i)));
                }
            }
            if (line.contains("<td style=\"text-align:left;\" data-sort-value=\"")) {
                for (int i = line.indexOf("<td style=\"text-align:left;\" data-sort-value=\"") + 46 ; !Character.toString(line.charAt(i)).equals("\"") ; i++) {
                    champ = champ.concat(Character.toString(line.charAt(i)));
                }
                if (!champ.equals(next)) {
                    champs[a] = new Champ(champ);
                    a++;
                }
                champ = "";
            }
            line = br.readLine();
        }
        return champs;
    }
    public static void setPool(Champ[] champs, String[] mains, String[] pool) {
        for (Champ champ : champs) {
            for (String name : mains) {
                if (champ.getName().equals(name)) {
                    champ.setMains(true);
                }
            }
            for (String name : pool) {
                if (champ.getName().equals(name)) {
                    champ.setPool(true);
                }
            }
        }
    }
    public static void findCounters(Champ[] champs, String pos, String rank) throws IOException {
        for (Champ champ : champs) {
            if (champ.getMains()) {
                String name = champ.getName().toLowerCase();
                name = name.replaceAll(" ", "");
                name = name.replaceAll("'", "");
                name = name.replaceAll("\\.", "");
                if (name.equals("wukong")) {
                    name = "monkeyking";
                }
                if (name.equals("nunu&willump")) {
                    name = "nunu";
                }
                URL u = new URL("https://www.leagueofgraphs.com/champions/counters/" + name + "/" + pos + "/" + rank);
                HttpURLConnection c = (HttpURLConnection)u.openConnection();
                c.setRequestProperty("User-Agent", "Mozilla/5.0");
                InputStream s = c.getInputStream();
                StringBuffer sb = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(s));
                String line = br.readLine();
                while (line != null) {
                    if (line.contains("<span id=\"matchesCountNumber\">")) {
                        String matches = "";
                        for (int i = (line.indexOf("<span id=\"matchesCountNumber\">") + 30) ; !Character.toString(line.charAt(i)).equals("<") ; i++) {
                            matches = matches.concat(Character.toString(line.charAt(i)));
                        }
                        matches = matches.replaceAll(",", "");
                        champ.setMatches(Integer.parseInt(matches));
                    }
                    if (line.contains("loses lane against")) {
                        List<String> bullies = new ArrayList<>();
                        line = br.readLine();
                        while (!line.contains("is best with")) {
                            if (line.contains("<span class=\"name\">")) {
                                name = "";
                                for (int i = (line.indexOf("<span class=\"name\">") + 19) ; !Character.toString(line.charAt(i)).equals("<") ; i++) {
                                    name = name.concat(Character.toString(line.charAt(i)));
                                }
                                bullies.add(name);
                            }
                            line = br.readLine();
                        }
                        if (!bullies.isEmpty()) {
                            String[] ba = new String[bullies.size()];
                            ba = bullies.toArray(ba);
                            champ.setBullies(ba);
                        }
                        else {
                            String[] ba = new String[1];
                            ba[0] = "none";
                            champ.setBullies(ba);
                        }
                    }
                    if (line.contains("loses more against")) {
                        List<String> counters = new ArrayList<>();
                        line = br.readLine();
                        while (line != null) {
                            if (line.contains("<span class=\"name\">")) {
                                name = "";
                                for (int i = (line.indexOf("<span class=\"name\">") + 19) ; !Character.toString(line.charAt(i)).equals("<") ; i++) {
                                    name = name.concat(Character.toString(line.charAt(i)));
                                }
                                counters.add(name);
                            }
                            line = br.readLine();
                        }
                        if (!counters.isEmpty()) {
                            String[] ca = new String[counters.size()];
                            ca = counters.toArray(ca);
                            champ.setCounters(ca);
                        }
                        else {
                            String[] ca = new String[1];
                            ca[0] = "none";
                            champ.setCounters(ca);
                        }
                    }
                    line = br.readLine();
                }
            }
        }
    }
    public static void findPoolCounters(Champ[] champs, String pos, String rank) throws IOException{
        List<String> counters = new ArrayList<>();
        List<String> pool = new ArrayList<>();
        for (Champ champ : champs) {
            if (champ.getMains()) {
                String[] b = champ.getBullies();
                String[] c = champ.getCounters();
                for (String s : b) {
                    counters.add(s);
                }
                for (String s : c) {
                    counters.add(s);
                }
                pool.add(champ.getName());
            }
            if (champ.getPool()) {
                pool.add(champ.getName());
            }
        }
        counters = new ArrayList<>(new HashSet<>(counters));
        counters.remove("none");
        for (Champ champ : champs) {
            for (String counter : counters) {
                if (champ.getName().equals(counter)) {
                    String name = champ.getName().toLowerCase();
                    name = name.replaceAll(" ", "");
                    name = name.replaceAll("'", "");
                    name = name.replaceAll("\\.", "");
                    if (name.equals("wukong")) {
                        name = "monkeyking";
                    }
                    if (name.equals("nunu&willump")) {
                        name = "nunu";
                    }
                    URL u = new URL("https://www.leagueofgraphs.com/champions/counters/" + name + "/" + pos + "/" + rank);
                    HttpURLConnection c = (HttpURLConnection)u.openConnection();
                    c.setRequestProperty("User-Agent", "Mozilla/5.0");
                    InputStream s = c.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = new BufferedReader(new InputStreamReader(s));
                    String line = br.readLine();
                    while (line != null) {
                        if (line.contains("loses lane against")) {
                            List<String> pb = new ArrayList<>();
                            line = br.readLine();
                            while (!line.contains("is best with")) {
                                if (line.contains("<span class=\"name\">")) {
                                    name = "";
                                    for (int i = (line.indexOf("<span class=\"name\">") + 19) ; !Character.toString(line.charAt(i)).equals("<") ; i++) {
                                        name = name.concat(Character.toString(line.charAt(i)));
                                    }
                                    if (pool.contains(name)) {
                                        pb.add(name);
                                    }
                                }
                                line = br.readLine();
                            }
                            if (!pb.isEmpty()) {
                                String[] poolBullies = new String[pb.size()];
                                poolBullies = pb.toArray(poolBullies);
                                champ.setPoolBullies(poolBullies);
                            }
                            else {
                                String[] poolBullies = new String[1];
                                poolBullies[0] = "none";
                                champ.setPoolBullies(poolBullies);
                            }
                        }
                        if (line.contains("loses more against")) {
                            List<String> pc = new ArrayList<>();
                            line = br.readLine();
                            while (line != null) {
                                if (line.contains("<span class=\"name\">")) {
                                    name = "";
                                    for (int i = (line.indexOf("<span class=\"name\">") + 19) ; !Character.toString(line.charAt(i)).equals("<") ; i++) {
                                        name = name.concat(Character.toString(line.charAt(i)));
                                    }
                                    if (pool.contains(name)) {
                                        pc.add(name);
                                    }
                                }
                                line = br.readLine();
                            }
                            if (!pc.isEmpty()) {
                                String[] poolCounters = new String[pc.size()];
                                poolCounters = pc.toArray(poolCounters);
                                champ.setPoolCounters(poolCounters);
                            }
                            else {
                                String[] poolCounters = new String[1];
                                poolCounters[0] = "none";
                                champ.setPoolCounters(poolCounters);
                            }
                        }
                        line = br.readLine();
                    }
                }
            }
        }
    }
    public static void getResults(Champ[] champs) throws IOException {
        File results = new File("results.txt");
        PrintWriter w = new PrintWriter(new FileWriter(results));
        for (Champ champ : champs) {
            if (champ.getMains()) {
                w.printf(champ.getName() + ":%n");
                if (champ.getMatches() < 100 * (champs.length - 1)) {
                    w.printf("data may be incomplete or inaccurate%n");
                }
                w.printf("bullies:%n");
                String[] bullies = champ.getBullies();
                for (String bully : bullies) {
                    w.printf("%-15s", bully + ":");
                    for (Champ enemy : champs) {
                        if (enemy.getName().equals(bully)) {
                            String[] poolBullies = enemy.getPoolBullies();
                            List<String> pb = new ArrayList<>();
                            for (String poolBully : poolBullies) {
                                pb.add(poolBully);
                            }
                            String[] poolCounters = enemy.getPoolCounters();
                            List<String> pc = new ArrayList<>();
                            for (String poolCounter : poolCounters) {
                                pc.add(poolCounter);
                            }
                            w.printf("%s%n%15s%s", "bullies:  " + pb, "", "counters: " + pc);
                        }
                    }
                    w.printf("%n");
                }
                w.printf("counters:%n");
                String[] counters = champ.getCounters();
                for (String counter : counters) {
                    w.printf("%-15s", counter + ":");
                    for (Champ enemy : champs) {
                        if (enemy.getName().equals(counter)) {
                            String[] poolBullies = enemy.getPoolBullies();
                            List<String> pb = new ArrayList<>();
                            for (String poolBully : poolBullies) {
                                pb.add(poolBully);
                            }
                            String[] poolCounters = enemy.getPoolCounters();
                            List<String> pc = new ArrayList<>();
                            for (String poolCounter : poolCounters) {
                                pc.add(poolCounter);
                            }
                            w.printf("%s%n%15s%s", "bullies:  " + pb, "", "counters: " + pc);
                        }
                    }
                    w.printf("%n");
                }
                w.printf("%n");
            }
        }
        w.close();
        Desktop desktop = Desktop.getDesktop();
        desktop.open(results);
    }
}