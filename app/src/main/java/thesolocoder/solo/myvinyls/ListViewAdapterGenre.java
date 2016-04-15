package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = inflator.inflate(R.layout.genre_view_element, parent, false);

        TextView genreName = (TextView) customView.findViewById(R.id.textViewGenreName);
        TextView genreNameRight = (TextView) customView.findViewById(R.id.textViewGenreNameRight);
        ImageView albumCoverArray[] = new ImageView[3];
        albumCoverArray[0] = (ImageView) customView.findViewById(R.id.imageViewGenreLarge);
        albumCoverArray[1] = (ImageView) customView.findViewById(R.id.imageViewGenreMedium);
        albumCoverArray[2] = (ImageView) customView.findViewById(R.id.imageViewGenreSmall);
        Button genreButton = (Button) customView.findViewById(R.id.ButtonGenereOverlay);
        Button genreButtonRight = (Button) customView.findViewById(R.id.ButtonGenereOverlayRight);

        GenreListItem genreItem = genres.get(position);
        genreName.setText(genreItem.genre);
        genreButton.setText(genreItem.genre);
        genreButton.setTag(genreItem.genre);
        for (int i = 0; i < genreItem.albumArt.size(); i++)
            loadImage(albumCoverArray[i], genreItem.albumArt.get(i));

        if(!genreItem.genreRight.equals("!N/A!"))
        {
            albumCoverArray[0] = (ImageView) customView.findViewById(R.id.imageViewGenreLargeRight);
            albumCoverArray[1] = (ImageView) customView.findViewById(R.id.imageViewGenreMediumRight);
            albumCoverArray[2] = (ImageView) customView.findViewById(R.id.imageViewGenreSmallRight);
            genreNameRight.setText(genreItem.genreRight);
            genreButtonRight.setText(genreItem.genreRight);
            genreButtonRight.setTag(genreItem.genreRight);
            for (int i = 0; i < genreItem.albumArtRight.size(); i++)
                loadImage(albumCoverArray[i], genreItem.albumArtRight.get(i));
        }
        else
        {
           customView.findViewById(R.id.genreLayoutRight).setVisibility(View.GONE);
        }
        return customView;
    }
    private void loadImage(ImageView albumCover, String fileName) {
        albumCover.setVisibility(View.VISIBLE);
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        albumCover.setImageBitmap(bitmap);
    }
}
