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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 2/25/2016.
 */
class ListViewAdapterMain extends BaseAdapter implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<Records> records;
    private ArrayList<LentOut> lentOut;
    private Context context;

    public ListViewAdapterMain(Context context, ArrayList<Records> data, ArrayList<LentOut> dataLentOut){
        inflater = LayoutInflater.from(context);
        this.records = data;
        this.lentOut = dataLentOut;
        this.context = context;
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
        View customView = inflater.inflate(R.layout.albumlistview, parent, false);
        TextView albumName = (TextView) customView.findViewById(R.id.textViewAlbumName);
        TextView bandName = (TextView) customView.findViewById(R.id.textViewBandName);
        TextView albumYear = (TextView) customView.findViewById(R.id.textViewYear);
        ImageView albumCover = (ImageView) customView.findViewById(R.id.imageView2);

        Records record = records.get(position);
        albumName.setText(record.get_albumname());
        bandName.setText(record.get_bandname());
        albumYear.setText(record.get_releaseyear());
        loadImage(albumCover, record.get_imageurl());

        if(lentOut != null)
            loadLentOutData(customView, position);

        return customView;
    }

    private void loadLentOutData(View customView, int position){
        TextView header = (TextView) customView.findViewById(R.id.textViewLentOutHeader);
        TextView lentOutToName = (TextView) customView.findViewById(R.id.textViewLentOutName);
        header.setVisibility(View.VISIBLE);
        lentOutToName.setVisibility(View.VISIBLE);
        lentOutToName.setText(lentOut.get(position).name);
       /* if(lentOut.get(position).isOverDue()){
            header.setTextColor(context.getResources().getColor(R.color.darkRed));
            lentOutToName.setTextColor(context.getResources().getColor(R.color.darkRed));
        }*/
    }

    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        albumCover.setImageBitmap(bitmap);
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                records = (ArrayList<Records>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new Filter.FilterResults();
                ArrayList<Records> FilteredArrayNames = new ArrayList<>();
                ArrayList<String> mDatabaseOfAlbums = new ArrayList<>();
                ArrayList<String> mDatabaseOfBands = new ArrayList<>();
                for(int i = 0; i < records.size(); i++)
                {
                    mDatabaseOfAlbums.add(records.get(i).get_albumname());
                    mDatabaseOfBands.add(records.get(i).get_bandname());
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
    }
}