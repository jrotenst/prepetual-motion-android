package com.jrutz.prepetualmotion.activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jrutz.prepetualmotion.R;
import com.jrutz.prepetualmotion.classes.CardPilesAdapter;
import com.jrutz.prepetualmotion.interfaces.AdapterOnItemClickListener;
import com.mintedtech.perpetual_motion.pm_game.PMGame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Game (current game) object
    private PMGame mCurrentGame;

    // Adapter (current board) object
    private CardPilesAdapter mAdapter;

    // Status Bar and SnackBar View references
    private TextView mTv_cardsRemaining, mTv_cardsInDeck;
    private View mSbContainer;
    private Snackbar mSnackBar;

    // UI Strings
    private String mWINNER_MSG, mNON_WINNER_MSG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFAB();

        setFieldReferencesToViewsAndSnackBar();
        setupBoard();
    }

    private void setFieldReferencesToViewsAndSnackBar() {
        mSbContainer = findViewById(R.id.activity_main);
        mTv_cardsRemaining = findViewById(R.id.tv_cards_remaining_to_discard);
        mTv_cardsInDeck = findViewById(R.id.tv_cards_in_deck);
        mSnackBar = Snackbar.make(mSbContainer, R.string.welcome_new_game, Snackbar.LENGTH_SHORT);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupBoard() {
        // create the adapter which will drive the RecyclerView (Model portion of MVC)
        mAdapter = new CardPilesAdapter(getString(R.string.cards_in_stack));

        // Set the listener object to respond to clicks in the RecyclerView
        // clicks are forwarded to the listener by the VH via Adapter
        mAdapter.setOnItemClickListener(getNewOnItemClickListener());

        // get a reference to the RecyclerView - not a field because it's not needed elsewhere
        RecyclerView rvPiles = findViewById(R.id.rv_piles);

        // Please note the use of an xml integer here: portrait will be 2x2 and landscape 1x4; neat!
        final int RV_COLUMN_COUNT = getResources().getInteger(R.integer.rv_columns);

        // optimization setting - since there are always exactly four piles
        rvPiles.setHasFixedSize(true);

        // Create a new LayoutManager object to be used in the RecyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager
                (this, RV_COLUMN_COUNT);

        // apply the Layout Manager object just created to the RecyclerView
        rvPiles.setLayoutManager(layoutManager);

        // apply the adapter object to the RecyclerView
        rvPiles.setAdapter(mAdapter);
    }

    /**
     * This anon implementation of our Listener interface handles adapter events
     * The object created here is passed to the adapter via the adapter's setter method
     * This object's onItemClick() is called from the Adapter when the user clicks on the board.
     * This leaves the Adapter to handle only the RV's data, and MainActivity to MVC Control...
     *
     * @return an object that responds to clicks inside a RecyclerView whose ViewHolder implements this interface
     */
    private AdapterOnItemClickListener getNewOnItemClickListener() {
        return new AdapterOnItemClickListener() {
            public void onItemClick(int position, View view) {
                try {
                    if (mCurrentGame.getNumberOfCardsInStackAtPosition(position) > 0) {
                        if (mCurrentGame.isGameOver()) {
                            showSB_AlreadyGameOver();
                        } else {
                            dismissSnackBarIfShown();
                            mAdapter.toggleCheck(position);
                        }
                    }
                    // otherwise, if this stack is empty (and no card shown), then ignore the click
                } catch (Exception e) {
                    Log.d("STACK", "Toggle Crashed: " + e.getMessage());
                    // No reason for it to crash but if it did...
                }
            }

        };
    }

    private void showSB_AlreadyGameOver() {

    }

    private void dismissSnackBarIfShown() {
        if (mSnackBar.isShown())
            mSnackBar.dismiss();
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
        //if (id == R.id.action_settings) {
        //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public void undoLastMove(MenuItem item) {
    }

    public void startNewGame(MenuItem item) {
    }

    public void turn_action_discard(View view) {
    }

    public void turn_action_deal(View view) {
    }

    public void turn_action_undo(View view) {
    }
}