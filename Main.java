import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String [] args){

        Period workingHours1 = new Period(LocalTime.of(8,00), LocalTime.of(22, 00));

        List<Period> scheduledMeetings1 = new ArrayList<>();
        scheduledMeetings1.add(new Period(LocalTime.of(9,00), LocalTime.of(10, 30)));
        scheduledMeetings1.add(new Period(LocalTime.of(12,00), LocalTime.of(13, 00)));
        scheduledMeetings1.add(new Period(LocalTime.of(16,00), LocalTime.of(18, 30)));

        Schedule schedule1 = new Schedule(workingHours1, scheduledMeetings1);


        Period workingHours2 = new Period(LocalTime.of(7,00), LocalTime.of(21, 30));

        List<Period> scheduledMeetings2 = new ArrayList<>();
        scheduledMeetings2.add(new Period(LocalTime.of(10,00), LocalTime.of(11, 30)));
        scheduledMeetings2.add(new Period(LocalTime.of(12,30), LocalTime.of(14, 30)));
        scheduledMeetings2.add(new Period(LocalTime.of(14,30), LocalTime.of(15, 00)));
        scheduledMeetings2.add(new Period(LocalTime.of(16,00), LocalTime.of(17, 00)));

        Schedule schedule2 = new Schedule(workingHours2, scheduledMeetings2);


        System.out.println("Schedule 1: " + schedule1);
        System.out.println("Schedule 2: " + schedule2);

        List<Period> expectedValue = new ArrayList<>();
        expectedValue.add(new Period(LocalTime.of(8,00), LocalTime.of(9, 00)));
        expectedValue.add(new Period(LocalTime.of(15,00), LocalTime.of(16, 00)));
        expectedValue.add(new Period(LocalTime.of(18,30), LocalTime.of(21, 30)));


        List<Period> result = algorithm(schedule1, schedule2, Duration.ofMinutes(60));

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expectedValue);
        System.out.println("Result equals Expected ? : " + result.equals(expectedValue));
    }

    private static List<Period> algorithm(Schedule firstSchedule, Schedule secondSchedule, Duration meetingDuration){

        LocalTime possibleStart = firstSchedule.getWorkingHours().beginning().isAfter(secondSchedule.getWorkingHours().beginning())?
                                    firstSchedule.getWorkingHours().beginning() : secondSchedule.getWorkingHours().beginning();

        LocalTime possibleEnd = firstSchedule.getWorkingHours().end().isBefore(secondSchedule.getWorkingHours().end())?
                                    firstSchedule.getWorkingHours().end() : secondSchedule.getWorkingHours().end();

        System.out.println("possible start: " + possibleStart);
        System.out.println("possible end: " + possibleEnd);

        if(Duration.between(possibleStart, possibleEnd).compareTo(meetingDuration) == -1) {
            System.out.println("not enought time");
            return new ArrayList<>();
        }


        sortPeriodsAsc(firstSchedule.getScheduledMeetings());
        sortPeriodsAsc(secondSchedule.getScheduledMeetings());

        List<Period> flattenedPeriods = flattenPeriods(firstSchedule.getScheduledMeetings(),
                                                       secondSchedule.getScheduledMeetings());
        sortPeriodsAsc(flattenedPeriods);


        List<Period> possiblePeriod = new ArrayList<>();

        LocalTime start = possibleStart;
        LocalTime end;

        for(int i = 0; i < flattenedPeriods.size() + 1;  i++){

            if(i == flattenedPeriods.size())
                end = possibleEnd;
            else
                end = flattenedPeriods.get(i).beginning();

            if(start.isBefore(end) && Duration.between(start, end).compareTo(meetingDuration) != -1)
                possiblePeriod.add(new Period(start, end));

            if(i != flattenedPeriods.size())
                start = flattenedPeriods.get(i).end();
        }

        return possiblePeriod;
    }


    private static List<Period> flattenPeriods(List<Period> first, List<Period> second){

        List<Period> flattenedPeriods = new ArrayList<>();

        for(int j = 0; j < first.size(); j++){
            Period firstScheduleMeeting = first.get(j);

            for(int i = j; i < second.size(); i++){
                Period secondScheduleMeeting = second.get(i);

                if(secondScheduleMeeting.beginning().isBefore(firstScheduleMeeting.beginning())
                        && secondScheduleMeeting.end().isAfter(firstScheduleMeeting.end())){
                    flattenedPeriods.add(secondScheduleMeeting);
                    break;
                }
                else if(firstScheduleMeeting.beginning().isBefore(secondScheduleMeeting.beginning())
                        && firstScheduleMeeting.end().isAfter(secondScheduleMeeting.end())) {
                    flattenedPeriods.add(firstScheduleMeeting);
                    break;
                }
                else if((secondScheduleMeeting.beginning().isBefore(firstScheduleMeeting.end())
                        || secondScheduleMeeting.beginning().equals(firstScheduleMeeting.beginning()))
                        && (secondScheduleMeeting.end().isAfter(firstScheduleMeeting.end())
                        || secondScheduleMeeting.end().equals(firstScheduleMeeting.end()))){
                    flattenedPeriods.add(new Period(firstScheduleMeeting.beginning(), secondScheduleMeeting.end()));
                    break;
                }
                else if((secondScheduleMeeting.end().isAfter(firstScheduleMeeting.beginning())
                        || secondScheduleMeeting.end().equals(firstScheduleMeeting.end()))
                        && (secondScheduleMeeting.beginning().isBefore(firstScheduleMeeting.beginning())
                        || secondScheduleMeeting.beginning().equals(firstScheduleMeeting.beginning()))){
                    flattenedPeriods.add(new Period(secondScheduleMeeting.beginning(), firstScheduleMeeting.end()));
                    break;
                }
                else if(!flattenedPeriods.contains(secondScheduleMeeting)){
                    flattenedPeriods.add(secondScheduleMeeting);
                }
            }
        }

        return flattenedPeriods;
    }

    private static void sortPeriodsAsc(List<Period> periods){
        Collections.sort(periods, (o1, o2) ->  {
                if(o1.beginning().isBefore(o2.beginning()))
                    return -1;
                if(o1.beginning().isAfter((o2.beginning())))
                    return 1;
                return 0;
            });
    }

}
