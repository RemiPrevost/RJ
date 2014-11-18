package com.google.riosport.webservice;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import com.google.riosport.elements.*;
/**
 * Created by Rémi Prévost on 30/10/2014.
 */
public final class WebServiceInterface {

    private static final String NAMESPACE = "https://www.etud.insa-toulouse.fr/~pamaury/webService/";
    private static final String MAIN_REQUEST_URL = "https://www.etud.insa-toulouse.fr/~pamaury/webService/server.php?asmx";

    public static void test1() throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#sayHello";
        String METHODNAME = "sayHello";

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("input", new String("asshole"));

        Object results = getResult(request,SOAP_ACTION);

        String data = results.toString();
        Log.v("DEBUG", "result = " + data.toString());
        //Log.v("DEBUG", "date = " + (String) ((SoapObject) results.get(0)).getProperty("date_of_birth"));

    }

    public static void test2() throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#Square";
        String METHODNAME = "Square";

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("input", 3);

        Object results = getResult(request,SOAP_ACTION);

        String data = results.toString();
        Log.v("DEBUG", "result = " + data.toString());
        //Log.v("DEBUG", "date = " + (String) ((SoapObject) results.get(0)).getProperty("date_of_birth"));

    }

    public static int addEvent(Event event) throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#AddEvent";
        String METHODNAME = "AddEvent";

        SoapObject request = new SoapObject(NAMESPACE,METHODNAME);
        request.addProperty("sport", event.getSport());
        request.addProperty("id_user", event.getOwner());
        request.addProperty("visibility",event.getVisibility());
        request.addProperty("date_time",event.getDateTime());
        request.addProperty("duration",event.getDuration());
        request.addProperty("location",event.getLocation());
        request.addProperty("longitude",event.getLongitude());
        request.addProperty("latitude",event.getLatitude());
        request.addProperty("description",event.getDescription());

        Integer result = (Integer)getResult(request,SOAP_ACTION);
        return result.intValue();
    }

    public static int addUser(String external_id, String log_manager, User user) throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#AddUser";
        String METHODNAME = "AddUser";

        SoapObject request = new SoapObject(NAMESPACE,METHODNAME);
        request.addProperty("external_id",external_id);
        request.addProperty("log_manager", log_manager);
        request.addProperty("full_name",user.getFull_name());
        request.addProperty("url_avatar",user.getUrl_avatar());
        request.addProperty("gender",user.getGender().toString());
        request.addProperty("date_of_birth",user.getDate_of_birth());

        Integer result = (Integer)getResult(request,SOAP_ACTION);
        return result.intValue();
    }

    public static User getUser(int id_user) throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#GetUser";
        String METHODNAME = "GetUser";

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("id_user", id_user);


        SoapObject result = (SoapObject)getResult(request,SOAP_ACTION);
        User user = new User();
        try {
            user.setId_user((Integer) result.getProperty("id_user"));
            user.setFull_name((String) result.getProperty("full_name"));
            user.setUrl_avatar((String) result.getProperty("url_avatar"));
            String gender = (String) result.getProperty("gender");
            if (gender.equals(Gender.male.toString()))
                user.setGender(Gender.male);
            else if (gender.equals(Gender.female.toString()))
                user.setGender(Gender.female);
            else
                user.setGender(Gender.undefined);
            user.setDate_of_birth((String) result.getProperty("date_of_birth"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static int isUser(String log_manager, String external_id) throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#IsUser";
        String METHODNAME = "IsUser";

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("log_manager", log_manager);
        request.addProperty("external_id", external_id);

        Integer result = (Integer)getResult(request,SOAP_ACTION);

        return result.intValue();
    }

    public static ArrayList<User> getFriendsOf(int id_user) throws WebServiceException{
        ArrayList<User> friends_list = new ArrayList<User>();

        ArrayList<Integer> id_friends_list = getIdFriendsOf(id_user);

        for (int id : id_friends_list)
            friends_list.add(getUser(id));

        return friends_list;
    }

    public static ArrayList<Practiced> getPracticedSports(int id_user) throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#GetPracticedSports";
        String METHODNAME = "GetPracticedSports";
        ArrayList<Practiced> practiced_list = new ArrayList<Practiced>();

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("id_user", id_user);

        Vector result = (Vector)getResult(request,SOAP_ACTION);

        for (int i = 0; i < result.size(); i = i + 2) {
            practiced_list.add(new Practiced((String)result.get(i),(String)result.get(i+1)));
        }

        Log.v("DEBUG", "result = " + practiced_list.toString());

        return practiced_list;
    }

    public static int getInternalId(String log_manager, String external_id) throws WebServiceException{
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#GetInternalId";
        String METHODNAME = "GetInternalId";

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("log_manager", log_manager);
        request.addProperty("external_id", external_id);

        Integer result = (Integer)getResult(request,SOAP_ACTION);

        return result;
    }

    public static ArrayList<String> getSports() throws WebServiceException {
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#GetSports";
        String METHODNAME = "GetSports";
        ArrayList<String> sports_list = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);

        Vector result = (Vector)getResult(request,SOAP_ACTION);

        for (int i = 0; i < result.size(); i++) {
            sports_list.add((String)result.get(i));
        }

        return sports_list;
    }

    public static ArrayList<String> getLevels() throws WebServiceException {
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#GetLevels";
        String METHODNAME = "GetLevels";
        ArrayList<String> levels_list = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);

        Vector result = (Vector)getResult(request,SOAP_ACTION);

        for (int i = 0; i < result.size(); i++) {
            levels_list.add((String) result.get(i));
        }

        return levels_list;
    }

    private static SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    private static HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(MAIN_REQUEST_URL);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\\\"1.0\\\" encoding= \\\"UTF-8\\\" ?-->");

        return ht;
    }

    private static Object getResult(SoapObject request, String SOAP_ACTION) throws WebServiceException{
        try {
            SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
            HttpTransportSE ht = getHttpTransportSE();

            MarshalDouble md = new MarshalDouble();
            md.register(envelope);

            ht.call(SOAP_ACTION, envelope);
            Object resultsString = envelope.getResponse();

            return resultsString;
        } catch (java.net.SocketTimeoutException e) {
            throw new WebServiceException("Failed to connect to web service");
        }catch (java.net.UnknownHostException e) {
            throw new WebServiceException("Failed to connect to web service");
        } catch (Exception e) {
            throw new WebServiceException("Failed to get result from Web Service for SOAP_ACTION : "+SOAP_ACTION);
        }
    }

    private static ArrayList<Integer> getIdFriendsOf(int id_user) throws WebServiceException {
        String SOAP_ACTION = "https://www.etud.insa-toulouse.fr/~pamaury/webService#GetFriendsOf";
        String METHODNAME = "GetFriendsOf";
        ArrayList<Integer> friends_id_list = new ArrayList<Integer>();

        SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
        request.addProperty("id_user", id_user);

        Vector result = (Vector)getResult(request,SOAP_ACTION);

        for (int i = 0; i < result.size(); i++) {
            friends_id_list.add(Integer.parseInt((String)result.get(i)));
        }

        return friends_id_list;
    }

    private static class MarshalDouble implements Marshal{
        public Object readInstance(XmlPullParser parser, String namespace, String name,PropertyInfo expected) throws IOException, XmlPullParserException {
            return Double.parseDouble(parser.nextText());
        }

        public void register(SoapSerializationEnvelope cm) {
            cm.addMapping(cm.xsd, "double", Double.class, this);
        }

        public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
            writer.text(obj.toString());
        }
    }
}
