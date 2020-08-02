package data.transpool.trip.offer;

import data.transpool.map.BasicMap;
import data.transpool.map.component.Stop;
import data.transpool.time.component.TimeDay;
import data.transpool.trip.offer.component.TripOffer;
import data.transpool.trip.offer.component.TripOfferDTO;
import data.transpool.trip.offer.component.TripOfferPart;
import data.transpool.trip.matching.component.TripOffersGraph;
import data.transpool.trip.matching.component.PossibleRoutesList;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main class which holds the trip offer graph and all the trip offers data. Also holds the timed data
 * such as current trip offers and current sub trip offers which are happening.
 */
public class TripOffersEngineBase implements TripOffersEngine {
    private List<TripOffer> allTripOffers;
    private TripOffersGraph tripOffersGraph;

    //Live details
    private List<TripOffer> currentTripOffers;
    private List<TripOfferPart> currentTripOfferParts;


    public TripOffersEngineBase(BasicMap map) {
        this.allTripOffers = FXCollections.observableArrayList();
        this.currentTripOfferParts = new ArrayList<>();
        this.currentTripOffers = FXCollections.observableArrayList();
        update();

        this.tripOffersGraph = new TripOffersGraph(map.getNumberOfStops(), allTripOffers);
    }

    @Override
    public List<TripOfferDTO> getTripOffersDetails() {
        return allTripOffers
                .stream()
                .map(TripOffer::getDetails)
                .collect(Collectors.toList());
    }

    @Override
    public TripOffer getTripOffer(int ID) {
        return allTripOffers
                .stream()
                .filter(t -> t.getID() == ID)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addTripOffer(TripOffer tripOffer) {
        allTripOffers.add(tripOffer);
        tripOffersGraph.add(tripOffer);
    }

    @Override
    public List<TripOffer> getAllTripOffers() {
        return allTripOffers;
    }

    @Override
    public int getNumOfTripOffers() {
        return allTripOffers.size();
    }

    @Override
    public TripOffersGraph getTripOffersGraph() {
        return tripOffersGraph;
    }

    @Override
    public List<TripOffer> getCurrentOffers() {
        return currentTripOffers;
    }

    @Override
    public List<TripOfferPart> getCurrentTripOfferParts() {
        return currentTripOfferParts;
    }

    @Override
    public TripOfferPart getTripOfferPart(int tripOfferID, int subTripOfferID) {
        return getTripOffer(tripOfferID).getTripOfferPart(subTripOfferID);
    }

    //----------------------------------------------------------------------------------------------------------------//


    /**
     * This function updates the map every time the system's time is changed.
     * 1. Gets all the current tripoffers happening.
     * 2. Populates the list of current subTripOffers happening
     * 3. Updates the stops with the relevant details through the subTripOffers using subTripOffer.currentDetails() functions.
     *    The details are shown on the live map when a stop is clicked.
     */
    @Override
    public void update() {
        currentTripOffers.clear();
        currentTripOfferParts.clear();
        for (TripOffer tripOffer : allTripOffers) {
            if (tripOffer.isCurrentlyHappening()) {
                currentTripOffers.add(tripOffer);
                currentTripOfferParts.add(tripOffer.getOccurringTripOfferPart());
            }
        }
        for (TripOfferPart tripOfferPart : currentTripOfferParts) {
            if (tripOfferPart != null && tripOfferPart.isCurrentlyDeparting()) {
                //subTripOffer.getSourceStop().addDetails(subTripOffer.currentDetails());
            } else if (tripOfferPart != null && tripOfferPart.isCurrentlyArriving()) {
                //subTripOffer.getDestinationStop().addDetails(subTripOffer.currentDetails());
            }
        }
    }

    public PossibleRoutesList getAllPossibleRoutes(Stop source, Stop destination, TimeDay departureTime) {
        return tripOffersGraph.getAllPossibleRoutes(source, destination, departureTime);
    }

}
