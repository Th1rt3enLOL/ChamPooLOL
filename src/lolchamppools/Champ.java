package lolchamppools;
public class Champ {
    private final String name;
    private boolean mains;
    private boolean pool;
    private int matches;
    private String[] bullies;
    private String[] counters;
    private String[] poolBullies;
    private String[] poolCounters;

    public Champ(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setMains(boolean mains) {
        this.mains = mains;
    }
    public boolean getMains() {
        return mains;
    }
    public void setPool(boolean pool) {
        this.pool = pool;
    }
    public boolean getPool() {
        return pool;
    }
    public void setMatches(int matches) {
        this.matches = matches;
    }
    public int getMatches() {
        return matches;
    }
    public void setBullies(String[] bullies) {
        this.bullies = bullies;
    }
    public String[] getBullies() {
        return bullies;
    }
    public void setCounters(String[] counters) {
        this.counters = counters;
    }
    public String[] getCounters() {
        return counters;
    }
    public void setPoolBullies(String[] poolBullies) {
        this.poolBullies = poolBullies;
    }
    public String[] getPoolBullies() {
        return poolBullies;
    }
    public void setPoolCounters(String[] poolCounters) {
        this.poolCounters = poolCounters;
    }
    public String[] getPoolCounters() {
        return poolCounters;
    }
}