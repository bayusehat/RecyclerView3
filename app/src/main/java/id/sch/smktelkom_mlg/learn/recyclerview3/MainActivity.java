package id.sch.smktelkom_mlg.learn.recyclerview3;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.learn.recyclerview3.adapter.HotelAdapter;
import id.sch.smktelkom_mlg.learn.recyclerview3.model.Hotel;

public class MainActivity extends AppCompatActivity {
    ArrayList<Hotel> mListAll = new ArrayList<>();
    boolean isFiltered;
    ArrayList<Integer> mListMapFilter = new ArrayList<>();
    String mQuery;
    int itemPos;
    public static final int REQUEST_CODE_ADD = 88;
    public static final String HOTEL = "hotel";
    public static final int REQUEST_CODE_EDIT = 99;
    ArrayList<Hotel> mlist = new ArrayList<>();
    HotelAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAdd();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HotelAdapter(this.mlist);
        recyclerView.setAdapter(mAdapter);

        fillData();

    }

    private void goAdd()
    {
        startActivityForResult(new Intent(this, InputActivity.class),REQUEST_CODE_ADD);
    }

    private void fillData() {
        Resources resources = getResources();
        String[] arjudul = resources.getStringArray(R.array.places);
        String[] arDeskripsi = resources.getStringArray(R.array.place_desc);
        String[] arDetail = resources.getStringArray(R.array.place_detail);
        String[] arLokasi = resources.getStringArray(R.array.place_Locations);
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        Drawable[] arFoto = new Drawable[a.length()];

        for (int i = 0; i < arFoto.length; i++) {
            int id=a.getResourceId(1,0);
            arFoto[i] = Drawable.createFromPath(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"
                    +resources.getResourcePackageName(id)+'/'
                    +resources.getResourcePackageName(id)+'/'
                    +resources.getResourcePackageName(id));
        }

        a.recycle();

        for (int i = 0; i < arjudul.length; i++) {
            mlist.add(new Hotel(arjudul[i], arDeskripsi[i],arDetail[i],arLokasi[i], arFoto[i]));
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id ==R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class MainActivity extends AppCompatActivity.implements HotelAdapter.IHotelAdapter{
        public static final String HOTEL = "hotel";

        @Override
        public void doClick(int pos)
        {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(HOTEL,mlist.get(pos));
            startActivity(intent);

        }

        @Override
        public void doEdit(int pos) {
            itemPos = pos;
            Intent intent = new Intent(this, InputActivity.class);
            intent.putExtra(HOTEL,mlist.get(pos));
            startActivityForResult(intent,REQUEST_CODE_EDIT);

        }

        @Override
        public void doDelete(int pos) {
            itemPos = pos;
            final Hotel hotel = mlist.get(pos);
            mlist.remove(itemPos);
            mAdapter.notifyDataSetChanged();
            Snackbar.make(findViewById(R.id.fab),hotel.judul+"Terhapus",Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mlist.add(itemPos,hotel);
                            if(isFiltered) mListAll.add(mListMapFilter.get(itemPos),hotel);
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK)
        {
            Hotel hotel = (Hotel) data.getSerializableExtra(HOTEL);
            mlist.add(hotel);
            if(isFiltered) mListAll.add(hotel);
            doFilter(mQuery);
        }
        else if(requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK)
        {
            Hotel hotel = (Hotel) data.getSerializableExtra(HOTEL);
            mlist.remove(itemPos);
            if(isFiltered) mListAll.remove(mListMapFilter.get(itemPos).intValue());
            mlist.add(itemPos, hotel);
            if(isFiltered) mListAll.add(mListMapFilter.get(itemPos), hotel);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        mQuery = newText.toLowerCase();
                        doFilter(mQuery);
                        return true;
                    }
                });
        return true;
    }

    private void doFilter(String mQuery)
    {
        if(!isFiltered)
        {
            mListAll.clear();
            mListAll.addAll(mlist);
            isFiltered=true;
        }
        mlist.clear();
        if(mQuery.isEmpty())
        {
            mlist.addAll(mListAll);
            isFiltered=false;
        }
        else
        {
            mListMapFilter.clear();
            for(int i=0;i<=mListAll.size();i++)
            {
                Hotel hotel = mListAll.get(i);
                if(hotel.judul.toLowerCase().contains(query) ||
                        hotel.deskripsi.toLowerCase().contains(query) ||
                        hotel.detail.toLowerCase().contains(query))
                {
                    mlist.add(hotel);
                    mListMapFilter.add(i);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
