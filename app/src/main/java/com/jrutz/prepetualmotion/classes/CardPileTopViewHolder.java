package com.jrutz.prepetualmotion.classes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jrutz.prepetualmotion.R;

public class CardPileTopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final CardView cv_pile_inner_Card;
    public final TextView tv_pile_card_rank_top, tv_pile_card_name_bottom,
            tv_pile_card_suit_center, tv_pile_card_cards_in_stack;
    public final CheckBox cb_pile_card_checkbox;

    public CardPileTopViewHolder(@NonNull View itemView) {
        super(itemView);

        cv_pile_inner_Card = itemView.findViewById (R.id.pile_card_inner_card);
        tv_pile_card_rank_top = itemView.findViewById (R.id.pile_card_rank_top);
        tv_pile_card_name_bottom = itemView.findViewById (R.id.pile_card_name_bottom);
        tv_pile_card_suit_center = itemView.findViewById (R.id.pile_card_suit_center);
        tv_pile_card_cards_in_stack = itemView.findViewById (R.id.pile_card_in_stack);
        cb_pile_card_checkbox = itemView.findViewById (R.id.pile_card_checkbox);
        cb_pile_card_checkbox.setClickable (false);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CardPilesAdapter.sAdapterOnItemClickListener.onItemClick(getAdapterPosition(), v);
    }
}
