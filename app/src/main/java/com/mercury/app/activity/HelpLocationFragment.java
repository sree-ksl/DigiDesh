package com.mercury.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mercury.app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class HelpLocationFragment extends Fragment {

    ImageView btnGPSShowLocation;
    String result;
    String link;
    Button bookOla;

    private PopupWindow pw;
    private View popupView;
    private LayoutInflater popupLay;

    private PopupWindow intervalWin;
    private View intervalView;
    private LayoutInflater intervalLay;

    //public static final String apiURL = "http://sandbox-t.olacabs.com/v1/bookings/create?pickup_lat=12.950072&pickup_lng=77.642 684&pickup_mode=NOW&category=sedan";
    ConnectCustomerToFlow connectCustomer;
    /*//JSON node names
    private static final String TAG_NAME = "driver_name";
    private static final String TAG_MOBILE = "driver_number";

    // contacts JSONArray
    JSONArray contacts = null;*/

    // Hashmap for ListView
    //ArrayList<HashMap<String, String>> contactList;

    //TextView firstContactNumber;

    AppLocationService appLocationService;

    //AddContactsFragment addContactsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_info,container,false);

        appLocationService = new AppLocationService(getActivity());

        /*firstContactNumber = (TextView)v.findViewById(R.id.frstCntct);
        String myTag = getTag();
        ((SliderActivity)getActivity()).setLocFragB(myTag);

        addContactsFragment = new AddContactsFragment();*/
        bookOla = (Button)v.findViewById(R.id.olaCab);

        bookOla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"You are safe with Thea Assist!", Toast.LENGTH_SHORT).show();
            }
        });


        btnGPSShowLocation = (ImageView)v.findViewById(R.id.sosBtn1);
        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
                if (gpsLocation != null) {
                    double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    result = "Lat:" + latitude + " Longi:" + longitude;
                    link = "http://maps.google.com/maps?q=loc:" + String.format("%f,%f", latitude, longitude);
                    Log.d("Location:", result);
                    Log.d("MapsLink:", link);
                    sendMessage();
                    Toast.makeText(getActivity(), "Help on the way!", Toast.LENGTH_SHORT).show();

                } else {
                    showSettingsAlert();
                }
            }
        });

        return v;
    }
    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = (ListView)getActivity().findViewById(R.id.driverList);

    }*/


    /*public void updateText(String t){
        firstContactNumber.setText(t);
    }*/

    private void sendMessage() {
        //Send help message
        StringBuilder sosMsg = new StringBuilder();
        sosMsg.append("I'm in danger.Please help..\n");

        //TO DO: send this result to google maps
        sosMsg.append(link);

        //Log.d("FirstContact", addContactsFragment.fContact);
        //change this to the numbers the user specified emergency contacts
        String[] numbers = new String[]{"+919591200619"};
        for(int i = 0; i < numbers.length; i++){
            SmsManager sosHelpMsg = SmsManager.getDefault();
            //TO DO: Set the phone number to list of numbers based on nearest located people available
            sosHelpMsg.sendTextMessage(numbers[i], null, sosMsg.toString(), null, null);
        }

        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        //call
        try{
            TelephonyManager telephonyManager = (TelephonyManager) appLocationService.getSystemService(Context.TELEPHONY_SERVICE);
            // receive notifications of telephony state changes
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            String helpline = "+918030853000";  //change this to the user selected contact
            Intent callIntent= new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + helpline));
            startActivity(callIntent);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private class MyPhoneListener extends PhoneStateListener {
        private boolean onCall = false;

        @Override
        public void  onCallStateChanged(int state, String incomingNumber){
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    //phone ringing
                    Toast.makeText(getActivity(), incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(getActivity(), "on call...",
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
                        Toast.makeText(getActivity(), "restart app after call", Toast.LENGTH_LONG).show();
                        // restart our application
                        Intent restart = getActivity().getPackageManager().
                                getLaunchIntentForPackage(getActivity().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);
                        onCall = false;
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Settings");
        alertDialog.setMessage("Enable Location provider.Go to Settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();
    }

    /*public void CallToOla(){
        //Make restful webservice call
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(apiURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    //TO-DO OLA booking
                    final String jsonStr = "{\n" +
                            "  \"crn\": \"1630\",\n" +
                            "  \"driver_name\": \"Phonenix D253\",\n" +
                            "  \"driver_number\": \"4567894253\",\n" +
                            "  \"cab_type\": \"sedan\",\n" +
                            "  \"cab_number\": \"KA 25  3\",\n" +
                            "  \"car_model\": \"Toyota Corolla\",\n" +
                            "  \"eta\": 2,\n" +
                            "  \"driver_lat\": 12.950074,\n" +
                            "  \"driver_lng\": 77.641727\n" +
                            "}";
                    JSONObject jsonResponse = new JSONObject(jsonStr);
                    int crn = Integer.parseInt(jsonResponse.optString("crn").toString());
                    String driverName = jsonResponse.optString("driver_name").toString();
                    String driverNumber = jsonResponse.optString("driver_number").toString();
                    Log.d("DriverName", driverName);
                    Log.d("DriverNumber", driverNumber);

                    StringBuilder cabMsg = new StringBuilder();
                    cabMsg.append("Say Ola to your driver" + driverName + "\ncontact number" + driverNumber);
                    SmsManager olaMsg = SmsManager.getDefault();
                    olaMsg.sendTextMessage("+919591200619", null, cabMsg.toString(),null, null );

                    //sms user that cab is booked

                    //JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"Failure", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getActivity(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    */

/*
    private class GetDrivers extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apiURL, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null){
                try{
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // looping through All Contacts

                        String name = jsonObj.getString(TAG_NAME);
                        String mobile = jsonObj.getString(TAG_MOBILE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_MOBILE, mobile)

                        // adding contact to contact list
                        contactList.add(contact);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.activity_info, new String[] { TAG_NAME,
                    TAG_MOBILE }, new int[] { R.id.name,
                    R.id.mobile });

            lv.setListAdapter(adapter);

        }
    }
*/
}
