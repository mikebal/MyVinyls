package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GenreAdapter  extends BaseAdapter{

    private LayoutInflater inflator;
    private ArrayList<String> genreCategories;
    LinearLayout selectedLayout;
    TextView selectedTextView;

    public GenreAdapter(Context context, ArrayList<String> data, LinearLayout selectedLayout, TextView slectedTextView) {
        inflator = LayoutInflater.from(context);
        this.genreCategories = data;
        this.selectedLayout = selectedLayout;
        this.selectedTextView = slectedTextView;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        final View customView = inflator.inflate(R.layout.genrelistview, parent, false);
        TextView category = (TextView) customView.findViewById(R.id.genreMainCat);
        String item = getItem(position);
        category.setText(item);

        ImageButton dropDown = (ImageButton) customView.findViewById(R.id.imageButton_genre_dropdown);

            dropDown.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                   selectedTextView.setText(genreCategories.get(position));
                   parent.setVisibility(View.GONE);
                   selectedLayout.setVisibility(View.VISIBLE);
                }
            });

            return customView;

    }
}
