package api.components;

import data.transpool.TransPoolData;
import data.transpool.trip.offer.matching.PossibleRoutesList;
import data.transpool.trip.request.MatchedTripRequest;
import data.transpool.trip.offer.data.PossibleMatch;
import data.transpool.trip.request.TripRequest;
import exception.NoMatchesFoundException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The engine used to find a match for a trip request. Contains the list of all possible matches, the request to match
 * and the chosen TP trip to match.
 */
public class MatchingEngine {
    private TripRequest tripRequestToMatch;
    private ObservableList<PossibleMatch> possibleMatches;
    private BooleanProperty foundMatches;

    public MatchingEngine() {
        possibleMatches = FXCollections.observableArrayList();
        foundMatches = new SimpleBooleanProperty(false);
    }

    public void findPossibleMatches(TransPoolData data, TripRequest tripRequestToMatch, int maximumMatches) throws NoMatchesFoundException {
        this.tripRequestToMatch = tripRequestToMatch;


        PossibleRoutesList list = data.getAllPossibleRoutes(tripRequestToMatch, maximumMatches);


    }

    /**
     * Creates and adds a new match to data after a possible match was chosen.
     * Updates TransPoolData after a match was made.
     * @param chosenPossibleMatch - The chosen possible match in the possible matches list.
     * @param data                - The data to add to.
     */
    public void addNewMatch(TransPoolData data, PossibleMatch chosenPossibleMatch) {
        data.addMatchedRequest(new MatchedTripRequest(tripRequestToMatch, chosenPossibleMatch));
    }


    /**
     * @return the list of all possible matches.
     */
    public ObservableList<PossibleMatch> getPossibleMatches() {
        return possibleMatches;
    }

    public void clearPossibleMatches() {
        possibleMatches.clear();
        foundMatches.set(false);
    }

    public BooleanProperty foundMatchesProperty() {
        return foundMatches;
    }
}
