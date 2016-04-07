package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 4/7/2016.
 */
public class ListViewAdapterGenre extends BaseAdapter{
        private LayoutInflater inflator;
        private ArrayList<GenreListItem> genres;

        public ListViewAdapterGenre(Context context, ArrayList<GenreListItem> data){
        inflator = LayoutInflater.from(context);
        this.genres = data;
    }

    public int getCount() {
        return genres.size();
    }
    public GenreListItem getItem(int position) {
        return genres.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View customView = inflator.inflate(R.layout.genre_view_element, parent, false);

        TextView genreName = (TextView) customView.findViewById(R.id.textViewGenreName);
        TextView genreNameRight = (TextView) customView.findViewById(R.id.textViewGenreNameRight);
        ImageView albumCoverBig = (ImageView) customView.findViewById(R.id.imageViewGenreLarge);
        ImageView albumCoverMedium = (ImageView) customView.findViewById(R.id.imageViewGenreMedium);
        ImageView albumCoverSmall = (ImageView) customView.findViewById(R.id.imageViewGenreSmall);
        Button genreButton = (Button) customView.findViewById(R.id.ButtonGenereOverlay);
        Button genreButtonRight = (Button) customView.findViewById(R.id.ButtonGenereOverlayRight);

            GenreListItem genreItem = genres.get(position);
            genreName.setText(genreItem.genre);
            genreButton.setText(genreItem.genre);

        if(!genreItem.genreRight.equals("!N/A!"))
        {
            genreNameRight.setText(genreItem.genreRight);
        }
        else
        {
           customView.findViewById(R.id.genreLayoutRight).setVisibility(View.GONE);
        }
       // loadImage(albumCoverBig, genreItem.albumArt1);
       // loadImage(albumCoverMedium, genreItem.albumArt2);
       // loadImage(albumCoverSmall, genreItem.albumArt3);
        return customView;
    }
    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        albumCover.setImageBitmap(bitmap);
    }

   /* @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                genres = (ArrayList<GenreListItem>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new Filter.FilterResults();
                ArrayList<Records> FilteredArrayNames = new ArrayList<>();
                ArrayList<String> mDatabaseOfAlbums = new ArrayList<>();
                ArrayList<String> mDatabaseOfBands = new ArrayList<>();
                for(int i = 0; i < genres.size(); i++)
                {
                    mDatabaseOfAlbums.add(genres.get(i).genre());
                   // mDatabaseOfBands.add(records.get(i).get_bandname());
                }
                // perform your search here using the searchConstraint String.
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < mDatabaseOfAlbums.size(); i++) {
                    String dataNames = mDatabaseOfAlbums.get(i);
                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(records.get(i));
                    }
                    else
                    {
                        dataNames = mDatabaseOfBands.get(i);
                        if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrayNames.add(records.get(i));
                        }
                    }
                }
                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };
        return filter;
    }*/
}
