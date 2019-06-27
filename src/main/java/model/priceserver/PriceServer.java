package main.java.model.priceserver;


import main.java.model.livemarketdata.OutsideTradingSoftwareAPIConnection;

/**
 * Provides priceserver bid/ask volumes for a single instrument.
 * Prices can be one of three: live, manual or historical
 * One PriceServer per one instrument to be traded.
 * So one for SPY, another one for DIA etc.
 */
public class PriceServer {

    private OutsideTradingSoftwareAPIConnection outsideTradingSoftwareAPIConnection = null;

    //simulatad manual prices and volumes:
    private double bidPrice = 27100;
    private double askPrice = 27101;
    private double bidVolume = -5;
    private double askVolume = -5;

    private Integer experimentalNumber = 0;

    public double getBidPrice() {
        return bidPrice;
    }

    synchronized public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
        System.out.println("Bid price set to: " + bidPrice);
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
        System.out.println("Ask price set to: " + askPrice);
    }

    public double getBidVolume() {
        return bidVolume;
    }

    public void setBidVolume(double bidVolume) {
        this.bidVolume = bidVolume;
    }

    public double getAskVolume() {
        return askVolume;
    }

    public void setAskVolume(double askVolume) {
        this.askVolume = askVolume;
    }

    //Use below methods to get real market data from outside trading software API:

    public void setRealTimeDataSource(OutsideTradingSoftwareAPIConnection outsideTradingSoftwareAPIConnection) {
        this.outsideTradingSoftwareAPIConnection = outsideTradingSoftwareAPIConnection;
    }

    public double getRealTimeBidPrice(){
        if(!(outsideTradingSoftwareAPIConnection == null)){
            return outsideTradingSoftwareAPIConnection.getBidPrice();
        }
        return -5;
    }

    public double getRealTimeAskPrice(){
        if(!(outsideTradingSoftwareAPIConnection == null)){
            return outsideTradingSoftwareAPIConnection.getAskPrice();
        }
        return -5;
    }
    public Integer getExperimentalNumber() {
        return experimentalNumber;
    }

    public void setExperimentalNumber(int experimentalNumber) {
        this.experimentalNumber = experimentalNumber;
    }
}