package tfg.dam.socialsports.fragments.newevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;

public class NewEventInvite extends Fragment {

    private ArrayList<Long> listInviteFriends;
    private TextView descriptionTextView;
    private LinearLayout listFriendsView;
    private BottomNavigationView navigationView;

    public NewEventInvite() {
        listInviteFriends = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event_invite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        descriptionTextView = getActivity().findViewById(R.id.textInviteDescription);
        listFriendsView = getActivity().findViewById(R.id.viewListaAmigos);
        navigationView = getActivity().findViewById(R.id.navigationNewEventInvite);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemInviteAllMenu:
                        selectAllFriends(true);
                        break;
                    case R.id.itemInviteNoneMenu:
                        selectAllFriends(false);
                        break;
                }
                return true;
            }
        });
        for (int i = 0; i< LoginActivity.user.getFriends().size(); i++) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(i);
            checkBox.setText(LoginActivity.user.getFriends().get(i).getFirstName());
            checkBox.setHint(String.valueOf(LoginActivity.user.getFriends().get(i).getId()));
            checkBox.setTextColor(getResources().getColor(R.color.colorElements));
            checkBox.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            checkBox.setButtonTintList(getResources().getColorStateList(R.color.colorElements));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setButtonTintList(getResources().getColorStateList(R.color.colorAccent));
                        if (!listInviteFriends.contains(buttonView.getHint().toString()))
                            listInviteFriends.add(Long.parseLong(buttonView.getHint().toString()));
                    }
                    else {
                        buttonView.setButtonTintList(getResources().getColorStateList(R.color.colorElements));
                        if (listInviteFriends.contains(buttonView.getHint().toString()))
                            listInviteFriends.remove(buttonView.getHint().toString());
                    }
                }
            });
            listFriendsView.addView(checkBox);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        descriptionTextView.setFocusable(true);
        descriptionTextView.setFocusableInTouchMode(true);
        descriptionTextView.requestFocus();
        descriptionTextView.setFocusable(false);
        descriptionTextView.setFocusableInTouchMode(false);
    }

    public void selectAllFriends(boolean selec) {
        for (int i = 0; i < LoginActivity.user.getFriends().size(); i++) {
            CheckBox checkBox = getActivity().findViewById(i);
            checkBox.setChecked(selec);
            if (selec)
                checkBox.setButtonTintList(getResources().getColorStateList(R.color.colorAccent));
            else
                checkBox.setButtonTintList(getResources().getColorStateList(R.color.colorElements));
        }
    }

    public ArrayList<Long> getListInviteFriends() {
        return listInviteFriends;
    }

}
