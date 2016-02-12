package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class GenreAdapter  extends BaseAdapter {

    private LayoutInflater inflator;
    private ArrayList<String> genreCategories;
    LinearLayout selectedLayout;
    LinearLayout checkboxHolder;
    TextView selectedTextView;
    ArrayList<String> checkedItems  = new ArrayList<>();
  //  int lastExpanded;

    public GenreAdapter(Context context, ArrayList<String> data, LinearLayout selectedLayout, TextView slectedTextView,
                        LinearLayout checkboxArea) {
        inflator = LayoutInflater.from(context);
        this.genreCategories = data;
        this.selectedLayout = selectedLayout;
        this.selectedTextView = slectedTextView;
        this.checkboxHolder = checkboxArea;
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

    public ArrayList<String> getCheckedItems(){return checkedItems;}

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        final View customView = inflator.inflate(R.layout.genrelistview, parent, false);
        final TextView category = (TextView) customView.findViewById(R.id.genreMainCat);
        final CheckBox mainGenereCheckBox = (CheckBox) customView.findViewById(R.id.checkBox);
        mainGenereCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkedItem = (CheckBox)v;
                if(checkedItem.isChecked())
                    checkedItems.add(category.getText().toString());
                else
                    checkedItems.remove(category.getText().toString());
            }
        });
        String item = getItem(position);
        category.setText(item);

        ImageButton dropDown = (ImageButton) customView.findViewById(R.id.imageButton_genre_dropdown);

        dropDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean isChecked = mainGenereCheckBox.isChecked();
               // lastExpanded = position;
                handleCategoryExpansion(v, position, convertView, parent, isChecked);
            }
        });

        return customView;

    }


    public void handleCategoryExpansion(View v, final int position, final View convertView, final ViewGroup parent, boolean isMainCategoryChecked) {
        selectedTextView.setText(genreCategories.get(position));
        parent.setVisibility(View.GONE);
        selectedLayout.setVisibility(View.VISIBLE);

        CheckBox mainGenerCheckBox = (CheckBox) selectedLayout.findViewById(R.id.checkBox);
        mainGenerCheckBox.setChecked(false);
        if(isMainCategoryChecked)
            mainGenerCheckBox.setChecked(true);
        mainGenerCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkedItem = (CheckBox) v;
                if (checkedItem.isChecked()) {
                    checkedItems.add(selectedTextView.getText().toString());
                    CheckBox parentBox = (CheckBox) parent.getChildAt(position).findViewById(R.id.checkBox);
                    parentBox.setChecked(true);
                }
                else {
                    checkedItems.remove(selectedTextView.getText().toString());
                    checkedItems.add(selectedTextView.getText().toString());
                    CheckBox parentBox = (CheckBox) parent.getChildAt(position).findViewById(R.id.checkBox);
                    parentBox.setChecked(false);
                }
            }


        });

        LinearLayout checkboxArea1 = (LinearLayout) checkboxHolder.findViewById(R.id.LinearLayoutCheckboxArea1);
        LinearLayout checkboxArea2 = (LinearLayout) checkboxHolder.findViewById(R.id.LinearLayoutCheckboxArea2);

        String categoryFileName = "#" + genreCategories.get(position).toString() + ".txt";
        GenreFileManager fileManager = new GenreFileManager(parent.getContext());
        ArrayList<String> sub_genreElements = fileManager.readInGenres(categoryFileName);

        for (int i = 0; i < sub_genreElements.size(); i++) {
            CheckBox cb = new CheckBox(parent.getContext());
            cb.setText(sub_genreElements.get(i));
            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox checkedItem = (CheckBox)v;
                    if(checkedItem.isChecked())
                        checkedItems.add(checkedItem.getText().toString());
                    else
                        checkedItems.remove(checkedItem.getText().toString());
                }


            });
            if (i % 2 == 0)
                checkboxArea1.addView(cb);
            else
                checkboxArea2.addView(cb);
            checkboxHolder.setVisibility(View.VISIBLE);
        }
    }
}
