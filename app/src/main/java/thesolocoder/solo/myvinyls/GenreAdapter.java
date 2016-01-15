package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

        import java.util.ArrayList;

public class GenreAdapter  extends BaseAdapter{

    private LayoutInflater inflator;
    private ArrayList<String> genreCategories;

    public GenreAdapter(Context context, ArrayList<String> data) {
        inflator = LayoutInflater.from(context);
        this.genreCategories = data;
    }

    public int getCount() {
        return genreCategories.size();
    }

    public String getItem(int position) {
        return genreCategories.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View customView = inflator.inflate(R.layout.genrelistview, parent, false);


        TextView category = (TextView) customView.findViewById(R.id.genreMainCat);
       // TextView body = (TextView) customView.findViewById(R.id.list_textview_DISC);
      //  TextView cost = (TextView) customView.findViewById(R.id.list_textview_COST);

        String item = getItem(position);

        category.setText(item);
        //body.setText(item.getDiscription());
      //  cost.setText(item.getCost());

        return customView;

    }
}
