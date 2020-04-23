public class ZegarLamporta {
    private int time;

    public ZegarLamporta() {
        time = 1;
    }

    public int getTime() {
        return time;
    }

    public void tick() {
        time++;
    }

    public void reviceAction(int obcyZegar) {
        time = Math.max(time, obcyZegar) + 1;
    }

}
