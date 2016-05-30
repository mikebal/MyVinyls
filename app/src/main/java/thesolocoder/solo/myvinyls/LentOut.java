package thesolocoder.solo.myvinyls;

import java.util.Calendar;

/**
 * Created by Michael on 5/23/2016.
 */
public class LentOut {
    String id;
    String name;
    Calendar    lentout = new Calendar() {
        @Override
        public void add(int field, int value) {

        }

        @Override
        protected void computeFields() {

        }

        @Override
        protected void computeTime() {

        }

        @Override
        public int getGreatestMinimum(int field) {
            return 0;
        }

        @Override
        public int getLeastMaximum(int field) {
            return 0;
        }

        @Override
        public int getMaximum(int field) {
            return 0;
        }

        @Override
        public int getMinimum(int field) {
            return 0;
        }

        @Override
        public void roll(int field, boolean increment) {

        }
    };
    Calendar    dueBack = new Calendar() {
        @Override
        public void add(int field, int value) {

        }

        @Override
        protected void computeFields() {

        }

        @Override
        protected void computeTime() {

        }

        @Override
        public int getGreatestMinimum(int field) {
            return 0;
        }

        @Override
        public int getLeastMaximum(int field) {
            return 0;
        }

        @Override
        public int getMaximum(int field) {
            return 0;
        }

        @Override
        public int getMinimum(int field) {
            return 0;
        }

        @Override
        public void roll(int field, boolean increment) {

        }
    };

  public boolean isOverDue(){
      boolean overDue = false;
      Calendar c = Calendar.getInstance();
      long dueBackTime = dueBack.getTimeInMillis();
      long otherTime = c.getTimeInMillis();
      if(dueBackTime < otherTime)
        overDue = true;
      return overDue;
    }

    public String getDateString(Calendar calendar)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(calendar.getTimeInMillis());

        String date;
        date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "/";
        date += String.valueOf(cal.get(Calendar.MONTH)) + "/";
        date += String.valueOf(cal.get(Calendar.YEAR));
        return date;
    }
}
