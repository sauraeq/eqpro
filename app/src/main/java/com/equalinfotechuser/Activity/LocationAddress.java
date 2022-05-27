package com.equalinfotechuser.Activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress {
    private final String TAG = "LocationAddress";


    public static void getAddressFromLocation(final LatLng latLng, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String
                        addressVal = "",
                        addressLine = "",
                        locality = "",
                        state = "",
                        postalCode = "",
                        subLocality = "";
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        addressLine = addresses.get(0).getAddressLine(0);
                        locality = addresses.get(0).getLocality();
                        subLocality = addresses.get(0).getSubLocality();
                        state = addresses.get(0).getAdminArea();
                        postalCode = addresses.get(0).getPostalCode();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if (addressLine != null)
                        addressVal += addressLine;
                    if (locality != null)
                        addressVal += " " + locality;
                    if (subLocality != null)
                        addressVal += " " + subLocality;
                    if (state != null)
                        addressVal += " " + state;
                    if (postalCode != null)
                        addressVal += ", " + postalCode;

                    Message message = Message.obtain();
                    message.setTarget(handler);
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("addressLine", addressLine);
                    bundle.putString("subLocality", subLocality);
                    bundle.putString("locality", locality);
                    bundle.putString("state", state);
                    bundle.putString("postalCode", postalCode);
                    bundle.putString("address", addressVal);
                    message.setData(bundle);
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static class GeocoderHandler extends Handler {
        private TextView address;

        public GeocoderHandler(TextView address) {
            this.address = address;
        }

        @Override
        public void handleMessage(Message message) {
            String addressVal = "";
            String localityVal = "";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    localityVal = bundle.getString("subLocality") + " " + bundle.getString("locality");
                    addressVal = bundle.getString("address");
                    break;

            }
            address.setText(addressVal);
        }
    }

}

