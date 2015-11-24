import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**

 COPYRIGHT (C) 2015 Scot Matson. All Rights Reserved.

 The model for the calendar application.
 Stores and fetches data and performs basic
 business logic pertaining to these tasks.

 Solves CS151 homework assignment #4

 @author Scot Matson

 @version 1.01 2015/11/23

 */
public class CalendarModel extends Observable
{
   // Viewing calendar
   private int currentDay;
   private int currentMonth;
   private int currentYear;
   // Month stats
   private int firstDayOfMonth;
   private int daysInMonth;
   // Selected calendar
   private int selectedDay;
   private int selectedMonth;
   private int selectedYear;
   // Events
   private Map<Date, List<Event>> events;

   /**
    Constructor method for CalendarModel
    @param c the current date
    */
   public CalendarModel(Calendar c)
   {
      Calendar calendar = c;
      int realDay;
      selectedDay = currentDay = realDay = calendar.get(GregorianCalendar.DAY_OF_MONTH);
      int realMonth;
      selectedMonth = currentMonth = realMonth = calendar.get(GregorianCalendar.MONTH);
      int realYear;
      selectedYear = currentYear = realYear = calendar.get(GregorianCalendar.YEAR);

      GregorianCalendar calTemp = new GregorianCalendar(realYear, realMonth, 1);
      daysInMonth = calTemp.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
      firstDayOfMonth = calTemp.get(GregorianCalendar.DAY_OF_WEEK);

      events = new TreeMap<>();
   }

   /**
    Gets the current day
    @return the current day as an integer
    */
   public int getCurrentDay()
   {
      return currentDay;
   }

   /**
    Sets the current day
    @param d the current day as an integer
    */
   public void setCurrentDay(int d)
   {
      currentDay = d;
      setChanged();
   }

   /**
    Gets the current month
    @return the current month as an integer
    */
   public int getCurrentMonth()
   {
      return currentMonth;
   }

   /**
    Sets the current month
    @param m the current month as an integer
    */
   public void setCurrentMonth(int m)
   {
      currentMonth = m;
      setChanged();
   }

   /**
    Gets the current year
    @return the current year as a 4-digit integer
    */
   public int getCurrentYear()
   {
      return currentYear;
   }

   /**
    Sets the current year
    @param y the current year as a 4-digit integer
    */
   public void setCurrentYear(int y)
   {
      currentYear = y;
      setChanged();
   }

   /**
    Returns the day of the week as an integer
    @return
    */
   public int getDayOfWeek()
   {
      GregorianCalendar calTemp = new GregorianCalendar(getSelectedYear(), getSelectedMonth(), getSelectedDay());
      return calTemp.get(GregorianCalendar.DAY_OF_WEEK);
   }

   /**
    Gets the start of the month
    @return the start of the month as an integer
    */
   public int getFirstDayOfMonth()
   {
      return firstDayOfMonth;
   }

   /**
    Sets the start of the month
    @param month the current month as an integer
    @param year the current year as an integer
    */
   public void setStartOfMonth(int month, int year)
   {
      GregorianCalendar calTemp = new GregorianCalendar(year, month, 1);
      firstDayOfMonth = calTemp.get(GregorianCalendar.DAY_OF_WEEK);
      setChanged();
   }

   /**
    Gets the number of days in the current month
    @return the number of days as an integer
    */
   public int getDaysInMonth()
   {
      return daysInMonth;
   }

   /**
    Sets the number of days in the current month
    @param month the current month as an integer
    @param year the current year as an integer
    */
   public void setDaysInMonth(int month, int year)
   {
      GregorianCalendar calTemp = new GregorianCalendar(year, month, 1);
      daysInMonth = calTemp.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
      setChanged();
   }

   /**
    Gets the selected day
    @return the selected day as an integer
    */
   public int getSelectedDay()
   {
      return selectedDay;
   }

   /**
    Sets the selected day
    @param sd the selected day as an integer
    */
   public void setSelectedDay(int sd)
   {
      selectedDay = sd;
      setChanged();
   }

   /**
    Gets the selected month
    @return the selected month as an integer
    */
   public int getSelectedMonth()
   {
      return selectedMonth;
   }

   /**
    Sets the selected month
    @param sm the selected month as an integer
    */
   public void setSelectedMonth(int sm)
   {
      selectedMonth = sm;
      setChanged();
   }

   /**
    Gets the selected year
    @return the selected year as an integer
    */
   public int getSelectedYear()
   {
      return selectedYear;
   }

   /**
    Sets the selected year
    @param sy the selected year as an integer
    */
   public void setSelectedYear(int sy)
   {
      selectedYear = sy;
      setChanged();
   }

   /**
    Adds a new event to the model
    @param start the starting datetime
    @param end the ending datetime
    @param title the title of the event
    @return true if successful, otherwise false
    */
   public boolean addNewEvent(Calendar start, Calendar end, String title)
   {
      boolean eventAdded = false;
      if (start.before(end))
      {
         eventAdded = true;
         Event newEvent = new Event(start, end, title);

         // Format used for storing events [KEY]
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
         Date eventDate = null;
         try
         {
            // The start date defines the position in the events HashMap
            eventDate = sdf.parse(sdf.format(newEvent.getStart().getTime()));
         } catch (ParseException pe)
         {
            pe.printStackTrace();
         }

         // Locate the [KEY] position if it exists, otherwise create a new position.
         if (events.containsKey(eventDate))
         {
            // Retrieve the list of events for this date and add the new event.
            List<Event> eventList = events.get(eventDate);
            for (Event oldEvent : eventList)
            {
               // New event start is inside of old event
               if (newEvent.getStart().after(oldEvent.getStart()) &&
                  newEvent.getStart().before(oldEvent.getEnd()))
               {
                  eventAdded = false;
               }

               // New event end is inside of old event
               if (newEvent.getEnd().after(oldEvent.getStart()) &&
                  newEvent.getEnd().before(oldEvent.getEnd()))
               {
                  eventAdded = false;
               }
            }
            if (eventAdded)// Check for overlapping times here
            {
               eventList.add(newEvent);
               eventAdded = true;
               // Resort the list.
               Collections.sort(eventList, Event::compareTo);
            }
         } else
         {
            // If the key does not exist, we need a new event list to hold
            // events for this date.
            List<Event> eventList = new ArrayList<>();
            eventList.add(newEvent);
            events.put(eventDate, eventList);
            eventAdded = true;
         }
         setChanged();
      }
      return eventAdded;
   }

   /**
    Determines if an event has been scheduled for the given Date
    @param date date to check for events
    @return true if has events, otherwise false
    */
   public boolean eventsScheduled(Date date)
   {
      boolean hasEvents = false;
      if (date != null)
      {
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
         try
         {
            // Ensure Date object is in the proper format to
            // compare against the map [KEY].
            date = sdf.parse(sdf.format(date));
         } catch (ParseException pe)
         {
            pe.printStackTrace();
         }
         if (events.containsKey(date))
         {
            hasEvents = true;
         }
      }
      return hasEvents;
   }

   /**
    @param date the Date of events to get
    */
   public ArrayList<Event> getDayEvents(Date date)
   {
      ArrayList<Event> todaysEvents = new ArrayList<>();
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      try
      {
         // Ensure Date object is in the proper format to
         // compare against the map [KEY].
         date = sdf.parse(sdf.format(date));
      }
      catch (ParseException pe)
      {
         pe.printStackTrace();
      }

      sdf.applyPattern("HH:mm");
      if (events.containsKey(date))
      {
         List<Event> eventList = events.get(date);
         for (Event e : eventList)
         {
            todaysEvents.add(e);
            //String start = sdf.format(e.getStart().getTime());
            //String end = sdf.format(e.getEnd().getTime());
            //System.out.printf("%s %s - %s\n", e.getTitle(), start, end);
         }
         //System.out.printf("\n");
      }
      else
      {
         todaysEvents = null;
      }
      return todaysEvents;
   }

   /**
    Returns the events Map
    @return the events map
    */
   public Map<Date, List<Event>> getEvents()
   {
      return events;
   }

   /**
    Deletes the events scheduled for a given date
    @param d the date to be deleted
    */
   public void deleteDayEvents(Date d)
   {
      if (events.containsKey(d) && d != null)
      {
         events.remove(d);
         setChanged();
      }
   }

   /**
    Adds an observer to be observed
    @param o an observer
    */
   @Override
   public synchronized void addObserver(Observer o)
   {
      super.addObserver(o);
   }

   /**
    Remove an observed observer
    @param o an observer
    */
   @Override
   public synchronized void deleteObserver(Observer o)
   {
      super.deleteObserver(o);
   }

   /**
    Notifies observers of changes to the model
    */
   @Override
   public void notifyObservers()
   {
      super.notifyObservers();
   }
}