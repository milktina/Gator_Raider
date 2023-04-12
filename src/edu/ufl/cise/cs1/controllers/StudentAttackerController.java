package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import game.system._Defender;

import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController {
	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int update(Game game, long timeDue) {
		// initializes action
		int action = -1;

		// declarations
		List<Defender> defendersList = game.getDefenders();
		Defender closestDefender = (Defender) game.getAttacker().getTargetActor(defendersList, true);
		Node closestDefenderLocation = closestDefender.getLocation();
		Node attackerLocation = game.getAttacker().getLocation();

		// sets list of nodes for power pills and regular pills
		List<Node> powerPills = game.getPowerPillList();
		List<Node> smallPills = game.getPillList();

		// distances
		int closestDefendDist = attackerLocation.getPathDistance(closestDefenderLocation);

		// makes attacker flee from defenders if defender is close and not vulnerable
		if (closestDefendDist <= 4 && !closestDefender.isVulnerable()) {
			action = game.getAttacker().getNextDir(closestDefenderLocation, false);
			return action;
		}

		// sees if there are power pills left and acts accordingly
		if (!powerPills.isEmpty()) {
			Node closestPowerPill = game.getAttacker().getTargetNode(powerPills, true);
			int ppDistance = attackerLocation.getPathDistance(closestPowerPill);

			Node closestPill = game.getAttacker().getTargetNode(smallPills, true);
			int pDistance = attackerLocation.getPathDistance(closestPill);

			// in the case that a defender is not vulnerable, the attacker should continue eating power pills
			if (!closestDefender.isVulnerable()) {
					if (ppDistance <= 100) {
						action = game.getAttacker().getNextDir(closestPowerPill, true);
						return action;
					}
					if (pDistance <= 27) {
						action = game.getAttacker().getNextDir(closestPill, true);
						return action;
					}
				}

            // in the case that a defender is vulnerable, the attacker's main objective should be eating defenders
			if (closestDefender.isVulnerable()) {
				if (closestDefendDist <= 100) {
					action = game.getAttacker().getNextDir(closestDefenderLocation, true);
					return action;
				}
				if (closestDefendDist > 101) {
					action = game.getAttacker().getNextDir(closestPowerPill, true);
					return action;
				}
				if (pDistance <= 27) {
					action = game.getAttacker().getNextDir(closestPill, true);
					return action;
				}
			}
			}

		// if there are no power pills left; main objective is to eat smaller pills until it dies
			else {
				if (closestDefender.isVulnerable()) {
					if (closestDefendDist <= 200) {
						action = game.getAttacker().getNextDir(closestDefenderLocation, true);
						return action;
					}
				}
				Node closestPill = game.getAttacker().getTargetNode(smallPills, true);
				int pDistance = attackerLocation.getPathDistance(closestPill);

				if (pDistance <= 200) {
					action = game.getAttacker().getNextDir(closestPill, true);
					return action;
				}

			}
			return action;

	}
}



