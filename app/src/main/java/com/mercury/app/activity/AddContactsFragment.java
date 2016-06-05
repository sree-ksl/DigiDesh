package com.mercury.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mercury.app.R;

public class AddContactsFragment extends Fragment {

    Button setCntcts;

    String fContact;
    String sContact;
    String tContact;
    String secCode;

    protected Spinner setTimeInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_add_contacts, container, false);

        final EditText firstCntct = (EditText)rootView.findViewById(R.id.firstContact);
        final EditText secondCntct = (EditText)rootView.findViewById(R.id.secondContact);
        final EditText thirdCntct = (EditText)rootView.findViewById(R.id.thirdContact);
        final EditText secureCode = (EditText)rootView.findViewById(R.id.secureCode);

        setTimeInt = (Spinner)rootView.findViewById(R.id.timeSpin);
        //spinner for state
        ArrayAdapter<CharSequence> tAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.time, R.layout.spinner_item);
        tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setTimeInt.setAdapter(tAdapter);

        firstCntct.setText("+91");
        secondCntct.setText("+91");
        thirdCntct.setText("+91");

        Selection.setSelection(firstCntct.getText(), firstCntct.getText().length());
        firstCntct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*if(!editable.toString().contains("+91")){
                    firstCntct.setText("http://");
                    Selection.setSelection(firstCntct.getText(), firstCntct.getText().length());

                }*/

                String prefix = "+91";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    firstCntct.setText(prefix + cleanString);
                    firstCntct.setSelection(prefix.length());
                }
            }
        });

        Selection.setSelection(secondCntct.getText(), secondCntct.getText().length());
        firstCntct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*if(!editable.toString().contains("+91")){
                    firstCntct.setText("http://");
                    Selection.setSelection(firstCntct.getText(), firstCntct.getText().length());

                }*/

                String prefix = "+91";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    secondCntct.setText(prefix + cleanString);
                    secondCntct.setSelection(prefix.length());
                }
            }
        });

        Selection.setSelection(thirdCntct.getText(), thirdCntct.getText().length());
        firstCntct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*if(!editable.toString().contains("+91")){
                    firstCntct.setText("http://");
                    Selection.setSelection(firstCntct.getText(), firstCntct.getText().length());

                }*/

                String prefix = "+91";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    thirdCntct.setText(prefix + cleanString);
                    thirdCntct.setSelection(prefix.length());
                }
            }
        });

        setCntcts = (Button)rootView.findViewById(R.id.setContacts);

        setCntcts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fContact = firstCntct.getText().toString();

                /*String locFragB = ((SliderActivity)getActivity()).getLocFragB();

                HelpLocationFragment helpLocFrag = (HelpLocationFragment)getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(locFragB);

                helpLocFrag.updateText(fContact);*/

                sContact = secondCntct.getText().toString();
                tContact = thirdCntct.getText().toString();
                secCode = secureCode.getText().toString();

                Log.d("FirstContact", fContact);
                Log.d("SecondContact", sContact);
                Log.d("ThirdContact", tContact);
                Toast.makeText(getActivity(), "You have set the emergency contacts and secure code!", Toast.LENGTH_SHORT).show();

            }
        });
            return rootView;
        }

}
