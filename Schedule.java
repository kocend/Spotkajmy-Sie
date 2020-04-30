import java.time.LocalTime;
import java.util.List;

public class Schedule {

    private Period workingHours;
    private List<Period> scheduledMeetings;

    public Schedule(Period workingHours, List<Period> scheduledMeetings) {
        this.workingHours = workingHours;
        this.scheduledMeetings = scheduledMeetings;
    }

    public Period getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Period workingHours) {
        this.workingHours = workingHours;
    }

    public List<Period> getScheduledMeetings() {
        return scheduledMeetings;
    }

    public void setScheduledMeetings(List<Period> scheduledMeetings) {
        this.scheduledMeetings = scheduledMeetings;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "workingHours=" + workingHours +
                ", scheduledMeetings=" + scheduledMeetings +
                '}';
    }
}
