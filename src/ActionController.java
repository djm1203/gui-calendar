import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 Created by scot on 11/21/15.
 */
public class ActionController
{
   CalendarModel model;
   ActionView view;

   public ActionController(CalendarModel cm, ActionView av)
   {
      model = cm;
      view = av;
      final String EVENTS_FILE = "events.txt";

      view.createButtonListener(e-> {
         JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(av);
         CreateDialog dialog = new CreateDialog(parentFrame, model);
         dialog.setSize(300, 150);
         model.notifyObservers();
      });

      view.deleteButtonListener(e-> {
         model.deleteEvents();
         model.notifyObservers();
      });

      view.exitButtonListener(e -> {
         FileWriter fw = null;
         try
         {
            fw = new FileWriter(new File(EVENTS_FILE));
            StringBuilder sb = new StringBuilder();
            Map<Date, List<Event>> events = model.getEvents();
            Iterator<Map.Entry<Date, List<Event>>> it = events.entrySet().iterator();
            while (it.hasNext())
            {
               Map.Entry<Date, List<Event>> pair = it.next();
               for (Event ev : pair.getValue())
               {
                  sb.append(ev.getStart().getTimeInMillis() + "\n");
                  sb.append(ev.getEnd().getTimeInMillis() + "\n");
                  sb.append(ev.getTitle());
                  if (pair.getValue().indexOf(ev) < pair.getValue().size() - 1 || it.hasNext())
                  {
                     sb.append("\n");
                  }
               }
               it.remove();
            }
            sb.trimToSize();
            fw.write(sb.toString());
         } catch (IOException ioe)
         {
            ioe.printStackTrace();
         } finally
         {
            try
            {
               fw.close();
            } catch (IOException ioe)
            {
               ioe.printStackTrace();
            }
         }
         System.exit(0);
      });
   }
}
