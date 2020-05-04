package data.transpool.structures;

import data.jaxb.PlannedTrips;
import data.transpool.map.Map;
import data.transpool.map.Path;
import data.transpool.trips.TransPoolTrip;
import exceptions.file.PathDoesNotExistException;
import exceptions.file.TransPoolDataFileException;
import exceptions.time.InvalidTimeException;

import java.util.ArrayList;
import java.util.List;

public class TransPoolTrips {
    private List<TransPoolTrip> transpoolTrips = new ArrayList<>();

    public TransPoolTrips(PlannedTrips JAXBTransPoolTrips) throws TransPoolDataFileException, InvalidTimeException {
        List<data.jaxb.TransPoolTrip> JAXBTrips = JAXBTransPoolTrips.getTransPoolTrip();
        for (data.jaxb.TransPoolTrip JAXBTrip : JAXBTrips) {
            addTransPoolTrip(new TransPoolTrip(JAXBTrip));
        }
    }

    private void addTransPoolTrip(TransPoolTrip transpoolTrip) throws PathDoesNotExistException {
        Route tripRoute = transpoolTrip.getRoute();
        for (int i = 0; i <  tripRoute.getNumberOfStops() - 1; i++) {
            String source = tripRoute.getStopNameByIndex(i);
            String destination = tripRoute.getStopNameByIndex(i + 1);
            Path thePath = Map
                    .getAllPaths()
                    .getPathBySourceAndDestination(source, destination);

            if (thePath == null) {
                throw new PathDoesNotExistException(source, destination);
            }
        }
        transpoolTrips.add(transpoolTrip);
    }

    public TransPoolTrip getTransPoolTripByID(int ID) {
        return transpoolTrips
                .stream()
                .filter(t -> t.getID() == ID)
                .findFirst()
                .orElse(null);
    }

    public List<TransPoolTrip> getTranspoolTrips() {
        return transpoolTrips;
    }

    @Override
    public String toString() {
        StringBuilder transPoolTripsString = new StringBuilder();
        for (TransPoolTrip trip : transpoolTrips) {
            transPoolTripsString.append(trip.toString());
            transPoolTripsString.append("\n");
        }
        return transPoolTripsString.toString();
    }
}
