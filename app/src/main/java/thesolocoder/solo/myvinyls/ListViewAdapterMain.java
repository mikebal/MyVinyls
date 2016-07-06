package thesolocoder.solo.myvinyls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

class ListViewAdapterMain extends BaseAdapter implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<Records> records;
    private ArrayList<LentOut> lentOut;
    private Context context;
    public String callingTable = "";
    public boolean isOnLendoutScreen = false;

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

    public void setIsOnLendOutScreen(boolean setTo)
    {
        isOnLendoutScreen = setTo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View customView = inflater.inflate(R.layout.albumlistview, parent, false);
        TextView albumName = (TextView) customView.findViewById(R.id.textViewAlbumName);
        TextView bandName = (TextView) customView.findViewById(R.id.textViewBandName);
        TextView albumYear = (TextView) customView.findViewById(R.id.textViewYear);
        ImageView albumCover = (ImageView) customView.findViewById(R.id.imageView2);
        ImageButton moreMenu = (ImageButton) customView.findViewById(R.id.imageButton3);
        moreMenu.setOnClickListener(expandMenuChoices);

        Records record = records.get(position);
        albumName.setText(record.get_albumname());
        bandName.setText(record.get_bandname());
        albumYear.setText(record.get_releaseyear());
        loadImage(albumCover, record.get_imageurl());
        moreMenu.setTag(record.get_imageurl());

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
        if(lentOut.get(position).isOverDue()){
            header.setTextColor(Color.parseColor("#990000"));
            lentOutToName.setTextColor(Color.parseColor("#990000"));
        }
    }

    private void loadImage(ImageView albumCover, String fileName) {
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/MyVinylsAlbumArt/" + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        if(bitmap != null)
            albumCover.setImageBitmap(bitmap);
        else
            albumCover.setImageResource(R.mipmap.default_noartwork);
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
    private View.OnClickListener expandMenuChoices = new View.OnClickListener() {
        public void onClick(View v) {
            final CharSequence[] items = {"Edit", "Lend out"};
            final String recordID = (String) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Record Options");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Intent startActivity = new Intent();
                    if(item == 0) {
                        startActivity.setClass(context, EditRecord.class);
                        startActivity.setAction(AddLentOut.class.getName());
                    }
                    else if(item == 1) {
                        if(!isOnLendoutScreen) {
                            startActivity.setClass(context, AddLentOut.class);
                            startActivity.setAction(AddLentOut.class.getName());
                        }
                        else{
                            startActivity.setClass(context, RecordReturn.class);
                            startActivity.setAction(RecordReturn.class.getName());
                        }
                    }
                    switchView(startActivity, recordID);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private void switchView(Intent startActivity, String recordID){

        startActivity.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity.putExtra("toEditID", recordID);
        startActivity.putExtra("callingTable", callingTable);
        context.startActivity(startActivity);
    }
}