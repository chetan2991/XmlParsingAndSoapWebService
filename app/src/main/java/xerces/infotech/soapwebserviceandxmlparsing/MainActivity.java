package xerces.infotech.soapwebserviceandxmlparsing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    EditText mCountryEdittext;
    TextView mResponseTextview;
    Button mGetCitiesButton;
    private static String URL = "http://www.webservicex.com/globalweather.asmx?WSDL";
    private static String SOAPAction= "http://www.webserviceX.NET/GetCitiesByCountry";
    private static String NAMESPACE= "http://www.webserviceX.NET";
    private static String METHODNAME ="GetCitiesByCountry";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCountryEdittext = ( EditText )findViewById( R.id.countryedittext );
        mGetCitiesButton = ( Button ) findViewById( R.id.getCitiesButton );
        mResponseTextview  = ( TextView )findViewById( R.id.ResponseTextview );
        mGetCitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new LoadDataTask().execute();
            }


        });
    }
    class  LoadDataTask extends AsyncTask<Void, Void,Void>
    {
        SoapObject result;
        SoapObject request;
        String countryName;
        ProgressDialog mProgressDialog;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if(result != null)

            {

                //Get the first property and change the label text

                mResponseTextview.setText(result.getProperty(0).toString());

            }

            else

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
         countryName   = mCountryEdittext.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... voids) {



            request = new SoapObject(NAMESPACE, METHODNAME);
            request.addProperty("CountryName", countryName );


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);



            envelope.setOutputSoapObject(request);

            envelope.dotNet = true;



            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);



                //this is the actual part that will call the webservice

                androidHttpTransport.call(SOAPAction, envelope);



                // Get the SoapResult from the envelope body.

                result = (SoapObject)envelope.bodyIn;





            } catch (Exception e) {

                e.printStackTrace();

            }


            return null;
        }
    }

}
