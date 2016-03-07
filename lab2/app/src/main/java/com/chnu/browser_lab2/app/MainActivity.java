package com.chnu.browser_lab2.app;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.support.v4.widget.DrawerLayout;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.chnu.browser_lab2.app.dummy.Bookmark;
import com.chnu.browser_lab2.app.dummy.History;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HistoryFragment.OnFragmentInteractionListener,
        BookmarksFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private WebView webView;

    private EditText editText;

    private RelativeLayout browserLayout;

    private MenuItem backward;

    private MenuItem forward;

    private Menu mainMenu;

    private ImageButton bookmarked;

    private ImageButton unbookmarked;

    private ImageButton clearURI;

    private int whatIsOpenedNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        initializeComponents();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position){

            case 0:
                if ( browserLayout != null )
                    if ( browserLayout.getVisibility() == View.INVISIBLE )
                        browserLayout.setVisibility(View.VISIBLE);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();

                whatIsOpenedNow = position;
                break;

            case 1:
                browserLayout.setVisibility(View.INVISIBLE);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new BookmarksFragment())
                        .commit();
                mTitle = getString(R.string.action_bookmarks);

                whatIsOpenedNow = position;
                break;

            case 2:
                browserLayout.setVisibility(View.INVISIBLE);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HistoryFragment())
                        .commit();
                mTitle = getString(R.string.action_history);

                whatIsOpenedNow = position;
                break;

        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.action_browser);
                break;
            case 2:
                mTitle = getString(R.string.action_bookmarks);
                break;
            case 3:
                mTitle = getString(R.string.action_history);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainMenu = menu;
        if ( !mNavigationDrawerFragment.isDrawerOpen() ) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){

            case R.id.action_backward : {
                if ( webView.canGoBack() ){
                    webView.goBack();
                    return true;
                } else Toast.makeText(getApplicationContext(), "Cannot go back", Toast.LENGTH_SHORT).show();
                return false;
            }

            case R.id.action_forward : {
                if ( webView.canGoForward() ){
                    webView.goForward();
                    return true;
                } else Toast.makeText(getApplicationContext(), "Cannot go forward", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    private void initializeComponents(){

        webView = (WebView) findViewById(R.id.webView);
        editText = (EditText) findViewById(R.id.uri);
        browserLayout = (RelativeLayout) findViewById(R.id.browser_layout);
        bookmarked = (ImageButton) findViewById(R.id.bookmarked);
        unbookmarked = (ImageButton) findViewById(R.id.unbookmarked);
        clearURI = (ImageButton) findViewById(R.id.clearURI);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    if (!isNetworkAvailable())
                        Toast.makeText(getApplicationContext(), "Error: No Internet Connection", Toast.LENGTH_SHORT).show();
                    else
                        openPage(editText.getText().toString());
                }
                return false;
            }
        });

        bookmarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bookmark.ITEMS.size() > 0
                        && Bookmark.ITEMS.get(0).content.equals("Empty now")) Bookmark.ITEMS.clear();

                if (!editText.getText().toString().equals("")) {
                    int index = getPageInList(editText.getText().toString());
                    if (index != -1) {
                        Bookmark.ITEMS.remove(index);

                        bookmarked.setVisibility(View.INVISIBLE);
                        unbookmarked.setVisibility(View.VISIBLE);
                    }
                } else if (editText.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "No link", Toast.LENGTH_SHORT).show();

            }
        });

        unbookmarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bookmark.ITEMS.size() > 0
                        && Bookmark.ITEMS.get(0).content.equals("Empty now")) Bookmark.ITEMS.clear();

                if (!editText.getText().toString().equals("")) {

                    Bookmark.WebPage webPage = new Bookmark.WebPage(editText.getText().toString());
                    Bookmark.addItem(webPage);

                    bookmarked.setVisibility(View.VISIBLE);
                    unbookmarked.setVisibility(View.INVISIBLE);
                } else if (editText.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "No link", Toast.LENGTH_SHORT).show();
            }
        });

        // Override method here because webView.loadURL() opens default browser
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url){
                if ( editText.getText().toString() != view.getUrl() )
                    editText.setText(view.getUrl());
                updateBookmarkButtonsStatus(editText.getText().toString());
            }
        });

        clearURI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void openPage(String uri){
        String link = uri.trim();

        if ( !link.equals("") ){
            if ( link.contains("http://") ) link = uri;
            else link = "http://www." + uri;

            webView.loadUrl(link);


            if ( !historyListContainsItem(link) ){

                if ( History.ITEMS.get(0).content.equals("Empty now") )
                    History.ITEMS.clear();

                History.WebPage webPage = new History.WebPage(link);
                History.addItem(webPage);
            }

            updateBookmarkButtonsStatus(link);

        }
        else Toast.makeText(getApplicationContext(), "URI is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        backward = menu.findItem(R.id.action_backward);
        forward = menu.findItem(R.id.action_forward);

        if ( backward != null && forward != null )
            switch (whatIsOpenedNow){
                case 0:
                    backward.setVisible(true);
                    forward.setVisible(true);
                    break;

                case 1:
                    backward.setVisible(false);
                    forward.setVisible(false);
                    break;

                case 2:
                    backward.setVisible(false);
                    forward.setVisible(false);
                    break;
            }

        super.onPrepareOptionsMenu(menu);
        return true;
    }


    private boolean historyListContainsItem(String link){
        for ( History.WebPage currentItem : History.ITEMS )
            if ( currentItem.content.equals(link) )
                return true;

        return false;
    }

    private boolean bookmarksListContainsItem(String link){
        for ( Bookmark.WebPage currentItem : Bookmark.ITEMS )
            if ( currentItem.content.equals(link) )
                return true;

        return false;
    }

    private void updateBookmarkButtonsStatus(String link){
        if ( bookmarksListContainsItem(link) ){
            bookmarked.setVisibility(View.VISIBLE);
            unbookmarked.setVisibility(View.INVISIBLE);
        } else {
            bookmarked.setVisibility(View.INVISIBLE);
            unbookmarked.setVisibility(View.VISIBLE);
        }
    }

    private int getPageInList(String page){
        for (int i = 0; i < Bookmark.ITEMS.size(); i++){
            if ( Bookmark.ITEMS.get(i).content.equals(page) ) {
                return i;
            }
        }
        return -1;
    }
}
