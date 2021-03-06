package thesolocoder.solo.myvinyls;

import java.util.ArrayList;

public class Records {

    private String _id;
    private String _bandname;
    private String _albumname;
    private String    _releaseyear;
    private ArrayList<String> _genre;
    private String _imageurl;
    private String _hasimage;
    private String _notes;
    private String _size;
    private static final String EMPTY_FIELD_DEFAULT = "#!NULL!#";

    public Records(){}

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_bandname(String _bandname) {
        this._bandname = _bandname;
    }

    public void set_releaseyear(String _releaseyear) {
        this._releaseyear = _releaseyear;
    }

    public void set_albumname(String _albumname) {
        this._albumname = _albumname;
        if(_albumname.equals(""))
            this._albumname = EMPTY_FIELD_DEFAULT;
    }

    public void set_genre(ArrayList<String> _genre) {
        this._genre = _genre;
    }
    public void set_hasimage(String hasImage){
        _hasimage = hasImage;
    }

    public void set_imageurl(String _imageurl){ this._imageurl = _imageurl; }

    public void set_notes(String noteData) { this._notes = noteData;}

    public void set_size(String size) {this._size = size; }

    public String get_id() { return _id; }

    public String get_bandname() { return _bandname; }

    public String get_albumname() {
        return _albumname;
    }

    public String get_releaseyear() {
        return _releaseyear;
    }

    public ArrayList<String> get_genre() { return _genre; }

    public String get_imageurl() { return _imageurl; }

    public String get_hasimage() { return  _hasimage;}

    public String get_notes() {return _notes;}

    public String get_size() { return  _size; }

}
