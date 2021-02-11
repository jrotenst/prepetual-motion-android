package com.jrutz.prepetualmotion.classes;

import android.animation.Animator;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrutz.prepetualmotion.R;
import com.jrutz.prepetualmotion.interfaces.AdapterOnItemClickListener;
import com.mintedtech.perpetual_motion.pm_game.Card;

import java.util.Locale;

public class CardPilesAdapter extends RecyclerView.Adapter<CardPileTopViewHolder> {

    // Listener class to pass click event and data back to Activity
    static AdapterOnItemClickListener sAdapterOnItemClickListener;

    // Adapter Primary Data Source
    private final Card[] mPILE_TOPS;

    // Arrays parallel to Adapter Primary Data Source
    // 1. which piles are checked
    private final boolean[] mCHECKED_PILES;
    // 2. how many total cards are in each pile
    private final int[] mNUMBER_OF_CARDS_IN_PILE;

    // The message to show on the bottom of the outer card (e.g "Cards in Stack:  ")
    private final String mMSG_CARDS_IN_STACK;

    /**
     * Sets the on item click
     * @param adapterOnItemClickListener listener object
     */
    public void setOnItemClickListener (AdapterOnItemClickListener adapterOnItemClickListener)
    {
        sAdapterOnItemClickListener = adapterOnItemClickListener;
    }

    public CardPilesAdapter(String mMSG_cards_in_stack) {
        final int NUMBER_OF_PILES = 4;
        mPILE_TOPS = new Card[NUMBER_OF_PILES];
        mCHECKED_PILES = new boolean[NUMBER_OF_PILES];
        mNUMBER_OF_CARDS_IN_PILE = new int[NUMBER_OF_PILES];

        mMSG_CARDS_IN_STACK = mMSG_cards_in_stack;
    }

    @NonNull
    @Override
    public CardPileTopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext ()).inflate (
                R.layout.rv_item_pile_top_card, parent, false);
        return new CardPileTopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardPileTopViewHolder holder, int position) {
        updateOuterCard(holder, position);
        updateInnerCard(holder, position);
    }

    private void updateOuterCard (CardPileTopViewHolder holder, int position)
    {
        holder.tv_pile_card_cards_in_stack.setText (
                mMSG_CARDS_IN_STACK.concat (Integer.toString (mNUMBER_OF_CARDS_IN_PILE[position])));
    }

    private void updateInnerCard (CardPileTopViewHolder holder, int position)
    {
        if (mPILE_TOPS[position] != null) {
            populateAndShowInnerCard (holder, position);
        }
        else {
            clearAndHideInnerCard (holder);
        }
    }

    private void populateAndShowInnerCard (CardPileTopViewHolder holder, int position)
    {
        Card currentCard = mPILE_TOPS[position];

        setTextOfEachTextView (holder, currentCard);
        setColorOfAllTextViews (holder, currentCard);
        holder.cb_pile_card_checkbox.setChecked (mCHECKED_PILES[position]);

        holder.cv_pile_inner_Card.setVisibility (View.VISIBLE);
    }

    private void setTextOfEachTextView (CardPileTopViewHolder holder, Card currentCard)
    {
        String rankValue = String.format(Locale.getDefault (), "%d", currentCard.getRank ().getValue ());
        holder.tv_pile_card_rank_top.setText (rankValue);
        holder.tv_pile_card_suit_center.setText (Character.toString (currentCard.getSuit ().getCharacter ()));
        holder.tv_pile_card_name_bottom.setText (currentCard.getRank ().toString ());
    }

    private void setColorOfAllTextViews (CardPileTopViewHolder holder, Card currentCard)
    {
        int currentColor = currentCard.getSuit ().getColor ();
        holder.tv_pile_card_rank_top.setTextColor (currentColor);
        holder.tv_pile_card_suit_center.setTextColor (currentColor);
        holder.tv_pile_card_name_bottom.setTextColor (currentColor);
    }

    private void clearAndHideInnerCard (CardPileTopViewHolder holder)
    {
        holder.tv_pile_card_rank_top.setText("");
        holder.tv_pile_card_suit_center.setText("");
        holder.tv_pile_card_name_bottom.setText("");
        holder.cb_pile_card_checkbox.setChecked (false);

        holder.cv_pile_inner_Card.setVisibility (View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mPILE_TOPS.length;
    }

    public void updatePile (int pileNumber, Card card, int numberOfCardsInStack)
    {
        mPILE_TOPS[pileNumber] = card;
        mNUMBER_OF_CARDS_IN_PILE[pileNumber] = numberOfCardsInStack;

        notifyItemChanged (pileNumber);
    }

    public Card getCardAt (int position)
    {
        return mPILE_TOPS[position] == null ? null : mPILE_TOPS[position].copy ();
    }

    public void clearCheck (int position)
    {
        if (mCHECKED_PILES[position]) {
            mCHECKED_PILES[position] = false;

            notifyItemChanged (position);
        }
    }

    public void toggleCheck (int position)
    {
        mCHECKED_PILES[position] = !mCHECKED_PILES[position];
        notifyItemChanged (position);
    }

    public void overwriteChecksFrom (boolean[] newChecksSet)
    {
        System.arraycopy (newChecksSet, 0, mCHECKED_PILES, 0, mCHECKED_PILES.length);
    }

    public boolean[] getCheckedPiles ()
    {
        return mCHECKED_PILES.clone ();
    }

    @Override public void onViewAttachedToWindow (@NonNull CardPileTopViewHolder holder)
    {
        super.onViewAttachedToWindow (holder);
        animateCard (holder.cv_pile_inner_Card);
    }

    private void animateCard (View view)
    {
        if (Build.VERSION.SDK_INT >= 21) {
            int centerX = 0, centerY = 0, startRadius = 0;
            int endRadius = Math.max (view.getWidth (), view.getHeight ());
            Animator circularRevealAnimationOfCard = ViewAnimationUtils.createCircularReveal
                    (view, centerX, centerY, startRadius, endRadius);
            circularRevealAnimationOfCard.start ();
        }
    }
}
