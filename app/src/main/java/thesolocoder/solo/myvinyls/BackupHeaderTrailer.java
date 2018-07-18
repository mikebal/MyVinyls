package thesolocoder.solo.myvinyls;

public class BackupHeaderTrailer {

    private final String _header;
    private final String _trailer;

    public BackupHeaderTrailer(String tag){
        _header = "<" + tag + "\\>";
        _trailer = "</" + tag + ">";
    }

    public String getHeaderTag(){
        return _header;
    }

    public String getTrailerTag() {
        return _trailer;
    }
}
