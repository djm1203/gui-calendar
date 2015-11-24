import java.util.Calendar;

/**

   COPYRIGHT (C) 2015 Scot Matson. All Rights Reserved.

   Class to manipulate Calendar Events.

   Solves CS151 homework assignment #4

   @author Scot Matson

   @version 1.00 2015/10/02
 */
public class Event implements Comparable<Event>
{
   private Calendar start;
   private Calendar end;
   private String title;
   
   /**
    * Constructor method.
    * @param start Starting date/time of the event.
    * @param end Ending date/time of the event.
    * @param title Title of the event.
    */
   public Event(Calendar start, Calendar end, String title)
   {
      this.start = start;
      this.end = end;
      this.title = title;
   }
   
   /**
    * Returns the start date/time of the event.
    * @return an event Date.
    */
   public Calendar getStart()
   {
      return this.start;
   }
   
   /**
    * Returns the end date/time of the event.
    * @return an event Date.
    */
   public Calendar getEnd()
   {
      return this.end;
   }
   
   /**
    * Returns the title of the event.
    * @return the event title.
    */
   public String getTitle()
   {
      return this.title;
   }

   /**
    * Compares two Event objects for sorted ordering.
    * @param c an Event object.
    */
   @Override
   public int compareTo(Event c)
   {
      return this.getStart().compareTo(c.getStart());
   }
}
