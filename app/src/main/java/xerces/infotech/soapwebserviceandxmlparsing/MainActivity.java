package xerces.infotech.soapwebserviceandxmlparsing;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    EditText mCountryEdittext;
    TextView mResponseTextview;
    Button mGetCitiesButton;
    SoapObject result;
    String cities = "";
    private static String URL = "http://www.webservicex.com/globalweather.asmx?WSDL";
    private static String SOAPAction = "http://www.webserviceX.NET/GetCitiesByCountry";
    private static String NAMESPACE = "http://www.webserviceX.NET";
    private static String METHODNAME = "GetCitiesByCountry";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCountryEdittext = (EditText) findViewById(R.id.countryedittext);
        mGetCitiesButton = (Button) findViewById(R.id.getCitiesButton);
        mResponseTextview = (TextView) findViewById(R.id.ResponseTextview);
        mGetCitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }


        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://xerces.infotech.soapwebserviceandxmlparsing/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://xerces.infotech.soapwebserviceandxmlparsing/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class LoadDataTask extends AsyncTask<Void, Void, Void> {

        SoapObject request;
        String countryName;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (result != null)

            {

                //Get the first property and change the label text

                mResponseTextview.setText("" + cities);

            } else

            {

                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();

            }
            mProgressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Please Wait");
            mProgressDialog.show();
            countryName = mCountryEdittext.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            request = new SoapObject(NAMESPACE, METHODNAME);
            request.addProperty("CountryName", countryName);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);


            envelope.setOutputSoapObject(request);

            envelope.dotNet = true;


            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


                //this is the actual part that will call the webservice

                androidHttpTransport.call(SOAPAction, envelope);


                // Get the SoapResult from the envelope body.

                result = (SoapObject) envelope.bodyIn;
                parseXmlData(result.getProperty(0).toString());


            } catch (Exception e) {

                e.printStackTrace();

            }


            return null;
        }
    }

    private void parseXmlData(String response) {
        try {

            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();


            xmlPullParser.setInput(new StringReader(response));
            int event = xmlPullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xmlPullParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("City")) {
                            cities = cities + "\n" + xmlPullParser.getAttributeValue(null, "defaultvalue");
                        }
                        break;
                }
                event = xmlPullParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
