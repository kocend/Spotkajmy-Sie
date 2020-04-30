import java.time.LocalTime;

public class Period {

    private LocalTime beginning;
    private LocalTime end;

    public Period(LocalTime beginning, LocalTime end) {
        this.beginning = beginning;
        this.end = end;
    }

    public LocalTime beginning() {
        return beginning;
    }

    public void setBeginning(LocalTime beginning) {
        this.beginning = beginning;
    }

    public LocalTime end() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Period{" +
                "start=" + beginning +
                ", end=" + end +
                '}';
    }
}
