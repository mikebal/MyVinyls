package thesolocoder.solo.myvinyls;

public class Records {

    private int _id;
    private String _bandname;
    private String _albumname;
    private int    _releaseyear;
    private String _genre;

    public Records(String bandname){
        this._bandname = bandname;
    }

    public Records(){

    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_bandname(String _bandname) {
        this._bandname = _bandname;
    }

    public void set_releaseyear(int _releaseyear) {
        this._releaseyear = _releaseyear;
    }

    public void set_albumname(String _albumname) {
        this._albumname = _albumname;
    }

    public void set_genre(String _genre) {
        this._genre = _genre;
    }

    public int get_id() {

        return _id;
    }

    public String get_bandname() {
        return _bandname;
    }

    public String get_albumname() {
        return _albumname;
    }

    public int get_releaseyear() {
        return _releaseyear;
    }

    public String get_genre() {
        return _genre;
    }
}
