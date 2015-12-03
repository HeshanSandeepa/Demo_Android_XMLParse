package heshansandeepa.demo_xmlparser;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    PlaceHolderFragment placeHolderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            placeHolderFragment = new PlaceHolderFragment();
            getSupportFragmentManager().beginTransaction().add(placeHolderFragment, "MyFragment").commit();
        } else {
            placeHolderFragment = (PlaceHolderFragment) getSupportFragmentManager().findFragmentByTag("MyFragment");
        }
        placeHolderFragment.startTask();
    }

    public static class PlaceHolderFragment extends Fragment {

        TechCrunchTask techCrunchTask;

        public PlaceHolderFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setRetainInstance(true);
        }

        public void startTask() {
            if (techCrunchTask != null) {
                techCrunchTask.cancel(true);
            } else {
                techCrunchTask = new TechCrunchTask();
                techCrunchTask.execute();
            }
        }
    }

    static class TechCrunchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String downloadUrl = "http://feeds.feedburner.com/TechCrunch";
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                processXML(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void processXML(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document =  documentBuilder.parse(inputStream);
            Element element = document.getDocumentElement();
            Log.d("Result", element.getTagName());

            NodeList list = element.getElementsByTagName("item");
            NodeList childList = null;
            Node currentItem = null;
            Node currentChild = null;
            for (int i = 0; i < list.getLength(); i ++) {
                currentItem = list.item(i);
                //get all child nodes
                childList = currentItem.getChildNodes();

                for (int j = 0; j < childList.getLength(); j ++) {
                    currentChild = childList.item(j);

                    if (currentChild.getNodeName().equalsIgnoreCase("title")) {
                        Log.d("currentChild", currentChild.getTextContent());
                    }
                }
            }
        }
    }

}
