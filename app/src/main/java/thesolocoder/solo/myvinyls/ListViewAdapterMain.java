package thesolocoder.solo.myvinyls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 2/25/2016.
 */
class ListViewAdapterMain extends BaseAdapter {
    private LayoutInflater inflator;
    private ArrayList<Records> records;

    public ListViewAdapterMain(Context context, ArrayList<Records> data){
       inflator = LayoutInflater.from(context);
       this.records = data;
    }

    public int getCount() {
        return records.size();
    }
    public Records getItem(int position) {
        return records.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View customView = inflator.inflate(R.layout.albumlistview, parent, false);

        TextView albumName = (TextView) customView.findViewById(R.id.textViewAlbumName);
        TextView bandName = (TextView) customView.findViewById(R.id.textViewBandName);
        TextView albumYear = (TextView) customView.findViewById(R.id.textViewYear);
        ImageView albumCover = (ImageView) customView.findViewById(R.id.imageView2);

        Records record = records.get(position);
        albumName.setText(record.get_albumname());
        bandName.setText(record.get_bandname());
        albumYear.setText(record.get_releaseyear());
        loadImage(albumCover, record.get_imageurl());

        return customView;
    }
    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        albumCover.setImageBitmap(bitmap);
    }
}
