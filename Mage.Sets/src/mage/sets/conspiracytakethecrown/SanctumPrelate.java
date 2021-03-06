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
package mage.sets.conspiracytakethecrown;

import java.util.HashSet;
import java.util.UUID;
import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.common.AsEntersBattlefieldAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ContinuousRuleModifyingEffectImpl;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.choices.Choice;
import mage.choices.ChoiceImpl;
import mage.constants.*;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.game.stack.Spell;
import mage.players.Player;
import org.apache.log4j.Logger;

/**
 *
 * @author maxlebedev
 */
public class SanctumPrelate extends CardImpl {

    public SanctumPrelate(UUID ownerId) {
        super(ownerId, 23, "Sanctum Prelate", Rarity.MYTHIC, new CardType[]{CardType.CREATURE}, "{1}{W}{W}");
        this.expansionSetCode = "CN2";
        this.subtype.add("Human");
        this.subtype.add("Cleric");
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // As Sanctum Prelate enters the battlefield, choose a number.
        this.addAbility(new AsEntersBattlefieldAbility(new ChooseNumberEffect()));

        // Noncreature spells with converted mana cost equal to the chosen number can't be cast.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new SanctumPrelateReplacementEffect()));
    }

    public SanctumPrelate(final SanctumPrelate card) {
        super(card);
    }

    @Override
    public SanctumPrelate copy() {
        return new SanctumPrelate(this);
    }
}

class ChooseNumberEffect extends OneShotEffect {

    public ChooseNumberEffect() {
        super(Outcome.Detriment);
        staticText = setText();
    }

    public ChooseNumberEffect(final ChooseNumberEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());

        int numberChoice = controller.announceXMana(0, Integer.MAX_VALUE, "Choose a number. Noncreature spells with the chosen converted mana cost can't be cast", game, source);
        game.getState().setValue(source.getSourceId().toString(), numberChoice);

        Permanent permanent = game.getPermanentEntering(source.getSourceId());
        permanent.addInfo("chosen players", "<font color = 'blue'>Chosen Number: "+ numberChoice +"</font>", game);

        game.informPlayers(permanent.getLogName() + ", chosen number: "+numberChoice);

        return true;
    }

    @Override
    public ChooseNumberEffect copy() {
        return new ChooseNumberEffect(this);
    }

    private String setText() {
        return "Choose a number. Noncreature spells with the chosen converted mana cost can't be cast";
    }
}

class SanctumPrelateReplacementEffect extends ContinuousRuleModifyingEffectImpl {

    Integer choiceValue;
    public SanctumPrelateReplacementEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Detriment);
        staticText = "Noncreature spells with the chosen converted mana cost can't be cast";
    }

    public SanctumPrelateReplacementEffect(final SanctumPrelateReplacementEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public SanctumPrelateReplacementEffect copy() {
        return new SanctumPrelateReplacementEffect(this);
    }

    @Override
    public String getInfoMessage(Ability source, GameEvent event, Game game) {
        MageObject mageObject = game.getObject(source.getSourceId());
        if (mageObject != null) {
            return "You can't cast a noncreature card with that converted mana cost (" + mageObject.getIdName() + " in play).";
        }
        return null;
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.CAST_SPELL_LATE;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        choiceValue = (Integer) game.getState().getValue(source.getSourceId().toString());
        Spell spell = game.getStack().getSpell(event.getTargetId());
        
        if (spell != null && !spell.getCardType().contains(CardType.CREATURE)){
            return spell.getConvertedManaCost() == choiceValue;
        }
        return false;
    }

}