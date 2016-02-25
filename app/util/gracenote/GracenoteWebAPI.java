package util.gracenote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class GracenoteWebAPI
{
    private String _clientID  = "";
    private String _clientTag = "";
    private String _userID    = "";
    private String _apiURL    = "https://[[CLID]].web.cddbp.net/webapi/xml/1.0/";

    public GracenoteWebAPI(String clientID, String clientTag) throws GracenoteException
    {
        this(clientID, clientTag, "");
    }

    public GracenoteWebAPI(String clientID, String clientTag, String userID) throws GracenoteException
    {
        //サニティチェック
        if (clientID.equals(""))  { throw new GracenoteException("Invalid input specified: clientID."); }
        if (clientTag.equals("")) { throw new GracenoteException("Invalid input specified: clientTag."); }

        this._clientID  = clientID;
        this._clientTag = clientTag;
        this._userID    = userID;
        this._apiURL    = this._apiURL.replace("[[CLID]]", clientID);
    }

    //グレースノートAPIのユーザ登録用メソッド
    public String register()
    {
        return this.register(this._clientID + "-" + this._clientTag);
    }

    public String register(String clientID)
    {
        if (!this._userID.equals(""))
        {
            System.out.println("Warning: You already have a userID, no need to register another. Using current ID.");
            return this._userID;
        }

        String request = "<QUERIES>"
                           + "<QUERY CMD=\"REGISTER\">"
                              + "<CLIENT>" + clientID + "</CLIENT>"
                           + "</QUERY>"
                       + "</QUERIES>";

        String response = this._httpPostRequest(this._apiURL, request);
        Document xml = this._checkResponse(response);

        this._userID = xml.getDocumentElement().getElementsByTagName("USER").item(0).getFirstChild().getNodeValue();
        return this._userID;
    }

    public GracenoteMetadata searchTrack(String artistName, String albumTitle, String trackTitle)
    {
        //サニティチェック
        if (this._userID.equals("")) { this.register(); }

        String body = this._constructQueryBody(artistName, albumTitle, trackTitle);
        String data = this._constructQueryRequest(body);
        return this._execute(data);
    }
   /**
    * 範囲指定可能楽曲検索メソッド
    * @author sumi
    * @param artistName
    * @param albumTitle
    * @param trackTitle
    * @param pageLength
    * @param pageNumber
    * @return
    */
    public GracenoteMetadata searchTrackRanged(String artistName, String albumTitle, String trackTitle, Integer pageLength, Integer pageNumber)//pageNumber...0 start
    {
        //サニティチェック
        if (this._userID.equals("")) { this.register(); }

        String body = this._constructQueryBodyRanged(artistName, albumTitle, trackTitle,pageLength,pageNumber);
        String data = this._constructQueryRequest(body);
        return this._execute(data);
    }

    public GracenoteMetadata searchArtist(String artistName)
    {
        return this.searchTrack(artistName,  "", "");
    }

    public GracenoteMetadata searchAlbum(String artistName, String albumTitle){
        return this.searchTrack(artistName, albumTitle, "");
    }
    public GracenoteMetadata fetchAlbum(String gn_id)
    {
        if (this._userID.equals("")) { this.register(); }
        String body = this._constructQueryBody("", "", "", gn_id, "ALBUM_FETCH");
        String data = this._constructQueryRequest(body, "ALBUM_FETCH");
        return this._execute(data);
    }

    //Utils
    public Document fetchAlbumWithoutParsing(String gn_id)
    {
        // サニティチェック
        if (this._userID.equals("")) { this.register(); }

        String body = this._constructQueryBody("", "", "", gn_id, "ALBUM_FETCH");
        String data = this._constructQueryRequest(body, "ALBUM_FETCH");
        String response = this._httpPostRequest(this._apiURL, data);
        return this._checkResponse(response);
    }
    protected GracenoteMetadata _execute(String data)
    {
        String response = this._httpPostRequest(this._apiURL, data);
        return this._parseResponse(response);
    }
    protected String _httpPostRequest(String url, String data)
    {
        try
        {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
            connection.setUseCaches (false);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            wr.write(data);
            wr.flush(); wr.close();

            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) { output.append(line); }

            reader.close();
            connection.disconnect();

            return output.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author sumi
     * @param artist
     * @param album
     * @param track
     * @param pageLength
     * @param pageNumber
     * @return
     */
    protected String _constructQueryBodyRanged(String artist, String album, String track, int pageLength, int pageNumber) { return this._constructQueryBodyRanged(artist, album, track, "", "ALBUM_SEARCH",pageLength,pageNumber); }
    protected String _constructQueryBodyRanged(String artist, String album, String track, String gn_id, String command, int pageLength, int pageNumber)
    {
        String body = "";

        //アルバム情報取得の場合
        if (command.equals("ALBUM_FETCH"))
        {
            body += "<GN_ID>" + gn_id + "</GN_ID>";

            // Include extended data.
            body += "<OPTION>"
                         + "<PARAMETER>SELECT_EXTENDED</PARAMETER>"
                         + "<VALUE>COVER,REVIEW,ARTIST_BIOGRAPHY,ARTIST_IMAGE,ARTIST_OET,MOOD,TEMPO</VALUE>"
                    + "</OPTION>";

            // Include more detailed responses.
            body += "<OPTION>"
                         + "<PARAMETER>SELECT_DETAIL</PARAMETER>"
                         + "<VALUE>GENRE:3LEVEL,MOOD:2LEVEL,TEMPO:3LEVEL,ARTIST_ORIGIN:4LEVEL,ARTIST_ERA:2LEVEL,ARTIST_TYPE:2LEVEL</VALUE>"
                     + "</OPTION>";

            // Only want the thumbnail cover art for now (LARGE,XLARGE,SMALL,MEDIUM,THUMBNAIL)
            body += "<OPTION>"
                         + "<PARAMETER>COVER_SIZE</PARAMETER>"
                         + "<VALUE>MEDIUM</VALUE>"
                     + "</OPTION>";
        }
        //楽曲サーチの場合
        else
        {
            // 一曲のみ取得したい場合
            //body += "<MODE>SINGLE_BEST</MODE>";

            if (!artist.equals("")) { body += "<TEXT TYPE=\"ARTIST\">" + artist + "</TEXT>"; }
            if (!track.equals(""))  { body += "<TEXT TYPE=\"TRACK_TITLE\">" + track + "</TEXT>"; }
            if (!album.equals(""))  { body += "<TEXT TYPE=\"ALBUM_TITLE\">" + album + "</TEXT>"; }
            //レンジ指定したいときはこれが必要
           body+="<RANGE>"
        		   +"<START>"+Integer.toString(pageNumber*pageLength+1)+"</START>"
        		   +"<END>"+Integer.toString(pageNumber*pageLength+pageLength)+"</END>"
        		   +"</RANGE>";
        }

        return body;
    }
    // This will construct the Gracenote query, adding in the authentication header, etc.
    protected String _constructQueryRequest(String body) { return this._constructQueryRequest(body, "ALBUM_SEARCH"); }
    protected String _constructQueryRequest(String body, String command)
    {
        return "<QUERIES>"
                   + "<AUTH>"
                       + "<CLIENT>" + this._clientID + "-" + this._clientTag + "</CLIENT>"
                       + "<USER>" + this._userID + "</USER>"
                   + "</AUTH>"
                   + "<QUERY CMD=\"" + command + "\">"
                       + body
                   + "</QUERY>"
               + "</QUERIES>";
    }
    protected String _constructQueryBody(String artist, String album, String track) { return this._constructQueryBody(artist, album, track, "", "ALBUM_SEARCH"); }
    protected String _constructQueryBody(String artist, String album, String track, String gn_id, String command)
    {
        String body = "";
        if (command.equals("ALBUM_FETCH"))
        {
            body += "<GN_ID>" + gn_id + "</GN_ID>";

            body += "<OPTION>"
                         + "<PARAMETER>SELECT_EXTENDED</PARAMETER>"
                         + "<VALUE>COVER,REVIEW,ARTIST_BIOGRAPHY,ARTIST_IMAGE,ARTIST_OET,MOOD,TEMPO</VALUE>"
                    + "</OPTION>";

            body += "<OPTION>"
                         + "<PARAMETER>SELECT_DETAIL</PARAMETER>"
                         + "<VALUE>GENRE:3LEVEL,MOOD:2LEVEL,TEMPO:3LEVEL,ARTIST_ORIGIN:4LEVEL,ARTIST_ERA:2LEVEL,ARTIST_TYPE:2LEVEL</VALUE>"
                     + "</OPTION>";

            body += "<OPTION>"
                         + "<PARAMETER>COVER_SIZE</PARAMETER>"
                         + "<VALUE>MEDIUM</VALUE>"
                     + "</OPTION>";
        }
        else
        {
            body += "<MODE>SINGLE_BEST</MODE>";

            if (!artist.equals("")) { body += "<TEXT TYPE=\"ARTIST\">" + artist + "</TEXT>"; }
            if (!track.equals(""))  { body += "<TEXT TYPE=\"TRACK_TITLE\">" + track + "</TEXT>"; }
            if (!album.equals(""))  { body += "<TEXT TYPE=\"ALBUM_TITLE\">" + album + "</TEXT>"; }
        }

        return body;
    }

    private Document _checkResponse(String response)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(response)));

            Element root = doc.getDocumentElement();
            NodeList nl = root.getElementsByTagName("RESPONSE");
            String status = "ERROR";
            if (nl != null && nl.getLength() > 0)
            {
                status = nl.item(0).getAttributes().getNamedItem("STATUS").getNodeValue();
            }

            if (status.equals("ERROR"))    { throw new GracenoteException("API response error."); }
            if (status.equals("NO_MATCH")) { throw new GracenoteException("No match response."); }
            if (!status.equals("OK"))      { throw new GracenoteException("Non-OK API response."); }

            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    protected GracenoteMetadata _parseResponse(String response)
    {
        Document xml = this._checkResponse(response);
        return new GracenoteMetadata(this, xml);
    }
}