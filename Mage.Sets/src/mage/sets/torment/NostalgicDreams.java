/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.torment;

import mage.abilities.Ability;
import mage.abilities.costs.common.DiscardXTargetCost;
import mage.abilities.dynamicvalue.common.GetXValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.ExileSpellEffect;
import mage.abilities.effects.common.ReturnFromGraveyardToHandTargetEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.filter.FilterCard;
import mage.game.Game;
import mage.target.Target;
import mage.target.common.TargetCardInYourGraveyard;

import java.util.UUID;

/**
 *
 * @author LevelX2
 */
public class NostalgicDreams extends CardImpl {

    public NostalgicDreams(UUID ownerId) {
        super(ownerId, 135, "Nostalgic Dreams", Rarity.RARE, new CardType[]{CardType.SORCERY}, "{G}{G}");
        this.expansionSetCode = "TOR";


        // As an additional cost to cast Nostalgic Dreams, discard X cards.
        this.getSpellAbility().addCost(new DiscardXTargetCost(new FilterCard("cards"), true));
        // Return X target cards from your graveyard to your hand. Exile Nostalgic Dreams.
        Effect effect = new ReturnFromGraveyardToHandTargetEffect();
        effect.setText("Return X target cards from your graveyard to your hand");
        this.getSpellAbility().addEffect(effect);        
        
        this.getSpellAbility().addEffect(ExileSpellEffect.getInstance());

    }

    public NostalgicDreams(final NostalgicDreams card) {
        super(card);
    }

    @Override
    public void adjustTargets(Ability ability, Game game) {
        int xValue = new GetXValue().calculate(game, ability, null);
//        if (xValue > 0) {
            Target target = new TargetCardInYourGraveyard(xValue, new FilterCard("card from your graveyard"));
            ability.addTarget(target);
//        }
    }

    @Override
    public NostalgicDreams copy() {
        return new NostalgicDreams(this);
    }
}
