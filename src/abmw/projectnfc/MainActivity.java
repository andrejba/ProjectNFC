package abmw.projectnfc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//      String SrcPath = "";
//      String video_url = "http://www.youtube.com/watch?v=JlJa4FC_ktc&feature=g-all-u";
//      video_url = "rtsp://v7.cache3.c.youtube.com/CjgLENy73wIaLwkeUryQ8ZkCqRMYJCAkFEIJbXYtZ29vZ2xlSARSB3Jlc3VsdHNg6KnB9MbH8sVODA==/0/0/0/video.3gp";
//  	final VideoView videoView = (VideoView) findViewById(R.id.VideoPlayer);
//  	
        String ServiceUrl = "http://gdata.youtube.com/feeds/api/videos/";
        
        setContentView(R.layout.activity_main);
        
        Bundle extras = getIntent().getExtras();
    	
        if (extras != null) {
	        
        	String url = extras.getString(Intent.EXTRA_TEXT);

	        if (url != null && url.length() > 0) {

		        String videoid = "";
		        
	        	int pos = url.toLowerCase().indexOf("v=");
	        	
	        	if (pos > 0) {
	        		videoid = url.substring(pos + 2);
	        		if (videoid.indexOf("&") > 0) videoid = videoid.substring(0,videoid.indexOf("&"));
	        		
	        		if (videoid.length() > 0) {
	        			try {
	        				URL urlVideo = new URL(ServiceUrl + videoid);
	        		    	new PlayVideoByVideoID().execute(urlVideo);

	        			} catch (MalformedURLException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	        		} else {
	        			TextView tv = (TextView) findViewById(R.id.txMain);
	        			tv.setText("Video ID not found!");
	        		}
	        	}
        }}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private class PlayVideoByVideoID extends AsyncTask<URL, Integer, String> {
    	
        protected String doInBackground(URL... urls) {
     
         	 try {
	                 URL url = urls[0];
	
		 			HttpGet httpGet = new HttpGet(url.toString());
		 			HttpClient httpclient = new DefaultHttpClient();
		 			// Execute HTTP Get Request
		 			HttpResponse response = httpclient.execute(httpGet);
		 			InputStream  content = response.getEntity().getContent();
		               
		            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		            DocumentBuilder db = dbf.newDocumentBuilder();
		
		               // Parse the earthquake feed.
		            Document dom = db.parse(content);      
		            Element docEle = dom.getDocumentElement();
	
				   // Get a list of each earthquake entry.
				   NodeList nl = docEle.getElementsByTagName("media:content");
				   if (nl != null && nl.getLength() > 0) {
				     for (int i = 0 ; i < nl.getLength(); i++) {
				       Element entry = (Element)nl.item(i);
				       //Element title = (Element)entry.getElementsByTagName("title").item(0);
					   String format = entry.getAttribute("yt:format");
					   
					   if (format.equals("1")) {
					   String vurl = entry.getAttribute("url");
					   return vurl;
			           }
			         }
			       }
			   } catch (MalformedURLException e) {
			     e.printStackTrace();
			   } catch (IOException e) {
			     e.printStackTrace();
			   } catch (ParserConfigurationException e) {
			     e.printStackTrace();
			   } catch (SAXException e) {
			     e.printStackTrace();
			   }
			   finally {
			   }
    	
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
	        VideoView vv = (VideoView) findViewById(R.id.VideoPlayer);
        	vv.setVideoURI(Uri.parse(result));
        	vv.setMediaController(new MediaController(getApplicationContext()));
        	vv.requestFocus();
        	vv.start();
        }
    }
}



