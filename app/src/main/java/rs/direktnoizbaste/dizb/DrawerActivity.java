package rs.direktnoizbaste.dizb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import rs.direktnoizbaste.dizb.app.SessionManager;
import rs.direktnoizbaste.dizb.array_adapters.SensorListAdapter;
import rs.direktnoizbaste.dizb.callback_interfaces.WebRequestCallbackInterface;
import rs.direktnoizbaste.dizb.dialogs.SensorDeleteConfirmationDialog;
import rs.direktnoizbaste.dizb.web_requests.DeleteSensorRequest;
import rs.direktnoizbaste.dizb.web_requests.PullSensorListRequest;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    ListView listView;
    Toolbar toolbar;

    ActionMode actionBarReference;
    private SessionManager session;
    private PullSensorListRequest psl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        session = new SessionManager(getApplicationContext());

        //Pulling sensor list from server
       // pullSensorList("1"); /*TODO make pulling sensor list to depend on user*/
        if (psl == null)
            psl = new PullSensorListRequest(this);
        psl.pullSensorList("1");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

/*        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else*/
        if (id == R.id.nav_logout) {

            //If the session is logged in log out
            if (session.isLoggedIn()) {
                /*TODO make actual log out from the server */
                session.setLogin(false);
                Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.nav_add) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DrawerActivity.this, GraphActivity.class);
        startActivity(intent);
    }

    //Multichoice mode listener methods for list view

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Here you can do something when items are selected/de-selected,
        // such as update the title in the CAB
        ListAdapter la = listView.getAdapter();
        SensorListAdapter sla = (SensorListAdapter)la;
        sla.selectView(position, checked);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate the menu for the CAB
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sensor_list_cab, menu); /*TODO figure out the menu*/
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // Here you can perform updates to the CAB due to
        // an invalidate() request
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // Respond to clicks on the actions in the CAB
        switch (item.getItemId()) {
            case R.id.action_delete:
                //deleteSelectedItems();
                // create Alert dialog to confirm sensor delete
                SensorDeleteConfirmationDialog sensorDeleteConfirmationDialog = new SensorDeleteConfirmationDialog(DrawerActivity.this);
                sensorDeleteConfirmationDialog.setPositiveButtonListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     /*TODO implement delete action*/
                        DeleteSensorRequest dsr = new DeleteSensorRequest(DrawerActivity.this);
                        dsr.setCallbackListener(new WebRequestCallbackInterface() {
                            @Override
                            public void webRequestSuccess(boolean success) {
                                // refresh sensor list
                                psl.pullSensorList("1");
                            }

                            @Override
                            public void webRequestError(String error) {
                                // refresh sensor list
                                psl.pullSensorList("1");
                            }
                        });
                        dsr.deleteSensorRequest("1", "5CCF7F752BA");

                        if (actionBarReference != null)// Action picked, so close the CAB
                            actionBarReference.finish();
                    }
                });
                sensorDeleteConfirmationDialog.setNegativeButtonListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                sensorDeleteConfirmationDialog.create().show();
                actionBarReference = mode;
                //mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Here you can make any necessary updates to the activity when
        // the CAB is removed. By default, selected items are deselected/unchecked.
       // customArrayAdapter.removeSelection();
        ListAdapter la = listView.getAdapter();
        SensorListAdapter sla = (SensorListAdapter)la;
        sla.removeSelection();
    }
}
