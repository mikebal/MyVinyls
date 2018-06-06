package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GenreAdapter  extends BaseAdapter {

    private LayoutInflater inflator;
    private ArrayList<String> genreCategories;
    LinearLayout selectedLayout;
    LinearLayout checkboxHolder;
    TextView selectedTextView;
    ArrayList<String> checkedItems = new ArrayList<>();
    MyDBHandler dbHandler;
    String editAlbumID;

    public GenreAdapter(Context context, ArrayList<String> data, LinearLayout selectedLayout, TextView selectedTextView,
                        LinearLayout checkboxArea, String isEditID) {
        inflator = LayoutInflater.from(context);
        dbHandler = new MyDBHandler(context, null, null, 1);
        this.genreCategories = data;
        this.selectedLayout = selectedLayout;
        this.selectedTextView = selectedTextView;
        this.checkboxHolder = checkboxArea;
        this.editAlbumID = isEditID;
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

    public ArrayList<String> getCheckedItems() {
        return checkedItems;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View customView = inflator.inflate(R.layout.genrelistview, parent, false);
        final TextView category = (TextView) customView.findViewById(R.id.genreMainCat);
        final CheckBox mainGenreCheckBox = (CheckBox) customView.findViewById(R.id.checkBox);
        mainGenreCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkedItem = (CheckBox) v;
                if (checkedItem.isChecked()) {
                    handleEdit(true, category.getText().toString(), "");
                } else {
                    handleEdit(false, category.getText().toString(), "");
                }
            }
        });
        String item = getItem(position);
        category.setText(item); // Set the subgenre Header text
        mainGenreCheckBox.setChecked(shouldBeChecked(item,"")); // Check if the header should be checked off
      /*  ImageButton dropDown = (ImageButton) customView.findViewById(R.id.imageButton_genre_dropdown);
        dropDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isChecked = mainGenreCheckBox.isChecked();
                handleCategoryExpansion(v, position, convertView, parent, isChecked);
            }
        });*/
        return customView;
    }

 /*   public void handleCategoryExpansion(View v, final int position, final View convertView, final ViewGroup parent, boolean isMainCategoryChecked) {
        selectedTextView.setText(genreCategories.get(position));
        parent.setVisibility(View.GONE);
        selectedLayout.setVisibility(View.VISIBLE);

        CheckBox mainGenreCheckBox = (CheckBox) selectedLayout.findViewById(R.id.checkBox);
        mainGenreCheckBox.setChecked(false);
        if (isMainCategoryChecked)
            mainGenreCheckBox.setChecked(true);
        mainGenreCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkedItem = (CheckBox) v;
                if (checkedItem.isChecked()) {
                    handleEdit(true, selectedTextView.getText().toString(), "");
                    CheckBox parentBox = (CheckBox) parent.getChildAt(position).findViewById(R.id.checkBox);
                    parentBox.setChecked(true);

                } else {
                    checkedItems.remove(selectedTextView.getText().toString());
                    checkedItems.add(selectedTextView.getText().toString());
                    CheckBox parentBox = (CheckBox) parent.getChildAt(position).findViewById(R.id.checkBox);
                    parentBox.setChecked(false);
                }
            }


        });

        LinearLayout checkboxArea1 = (LinearLayout) checkboxHolder.findViewById(R.id.LinearLayoutCheckboxArea1);
        LinearLayout checkboxArea2 = (LinearLayout) checkboxHolder.findViewById(R.id.LinearLayoutCheckboxArea2);

        String genreName = genreCategories.get(position);
        String getSubGenres = "SELECT DISTINCT subgenre FROM recordsgenres WHERE genre='" + genreName + "';";
        ArrayList<String> sub_genreElements = dbHandler.dbReturnListStrings(getSubGenres, "subgenre");

        if (sub_genreElements.get(0).equals(""))
            sub_genreElements.remove(0);

        for (int i = 0; i < sub_genreElements.size(); i++) {
            CheckBox cb = new CheckBox(parent.getContext());
            cb.setText(sub_genreElements.get(i));
            cb.setChecked(shouldBeChecked(selectedTextView.getText().toString(),sub_genreElements.get(i)));
            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox checkedItem = (CheckBox) v;
                    if (checkedItem.isChecked()) {
                        handleEdit(true, selectedTextView.getText().toString(), checkedItem.getText().toString());
                    } else {
                        handleEdit(false, selectedTextView.getText().toString(), checkedItem.getText().toString());
                    }
                }
            });
            if (i % 2 == 0)
                checkboxArea1.addView(cb);
            else
                checkboxArea2.addView(cb);
            checkboxHolder.setVisibility(View.VISIBLE);
        }
    }*/

    private int findIndexOfString(ArrayList<String> list, String target) {
        int result = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                result = i;
                break;
            }
        }
        return result;
    }

    private void handleEdit(boolean isAdd, String genre, String subGenre)
    {
        if (!editAlbumID.equals("-1"))
            dbHandler.runRawQueryNoResult(getRawQuery(isAdd,genre,subGenre));
        else{
            if(isAdd){
                if(subGenre.equals(""))
                    subGenre = "###";
                checkedItems.add(genre);
                checkedItems.add(subGenre);
            }
            else{
                int listLocation = findIndexOfString(checkedItems, genre);
                checkedItems.remove(listLocation); // Remove Genre
                checkedItems.remove(listLocation); // Remove Sub-Genre
            }
        }
    }

    private String getRawQuery(boolean isAdd, String genre, String subGenre)
    {
        String addPart1 = "insert into recordsgenres (album_id,genre,subgenre) values ('" + editAlbumID + "','";
        String deletePart1 =  "DELETE FROM recordsgenres WHERE album_id='" + editAlbumID + "' AND genre='";
        String result;

        if(isAdd)
            result = addPart1 + genre + "','" + subGenre + "');";
        else
            result = deletePart1 + genre + "'";
        return result;
    }

    private boolean shouldBeChecked(String genre, String subGenre)
    {
        boolean mustBeChecked = false;
        if(!editAlbumID.equals("-1")) {
            String query = "SELECT * FROM recordsgenres WHERE album_id='" + editAlbumID + "' AND genre='" + genre + "'";
            String queryResult = dbHandler.runRawQueryIfExists(query, "album_id");
            if (queryResult.equals(editAlbumID))
                mustBeChecked = true;
        }
        return mustBeChecked;
    }
}
