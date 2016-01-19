package thesolocoder.solo.myvinyls;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class GenreFileManager {

    private Context appContext;

    public GenreFileManager(Context context)
    {
        appContext = context;
    }
    public ArrayList readInGenres(String filename)
    {
        String item;
        ArrayList<String> categories = new ArrayList<String>();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(appContext.openFileInput(filename)));
            while ((item = inputReader.readLine()) != null){
                categories.add(item);
            }
        }
        catch (IOException e) {
            if(filename.equals("#genre.txt")) {
                categories.add("Classical");
                categories.add("Electronica");
                categories.add("Hip-Hop");
                categories.add("Jazz");
                categories.add("Latin");
                categories.add("Metal");
                categories.add("Pop");
                categories.add("R&B");
                categories.add("Rock");
                categories.add("World");
            }
        }
        return categories;
    }

    private void writeArraysBackToFile(ArrayList<String> categories, String filename)
    {
        int arraySize =  categories.size();

        try{
            OutputStreamWriter out = new OutputStreamWriter(appContext.openFileOutput(filename, appContext.MODE_PRIVATE));
            for(int i = 0; i < arraySize; i++)
            {
                out.write(categories.get(i).toString() + "\n");
            }
            out.close();
        } catch (java.io.IOException e) {
            //do something if an IOException occurs.
        }
    }

    public void addAlphabetically(String newCategory, String fileName){

        ArrayList<String> categories = readInGenres(fileName);
        int index = 2;
        boolean added = false;

        while (index < categories.size()){
            if(newCategory.compareTo(categories.get(index)) == 0) {
                added =true;
                break;
            }
            if(newCategory.compareTo(categories.get(index)) < 0) {
                categories.add(index, newCategory);
                added = true;
                break;
            }
            index++;
        }
        if(!added)
            categories.add(newCategory);
        writeArraysBackToFile(categories, fileName);
    }
}

