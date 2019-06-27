package main.java.model.livemarketdata;

// InteractiveBrokersAPI.java
// Version 1.0
// 20141028
// R. Holowczak
//http://holowczak.com/ib-api-java-realtime/7/

//https://interactivebrokers.github.io/tws-api/interfaceIBApi_1_1EWrapper.html


import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.CommissionReport;
import com.ib.client.UnderComp;

/**
 * Experimental class used to familiarize with IB TWS API
 */
public class InteractiveBrokersAPI implements EWrapper, OutsideTradingSoftwareAPIConnection {

    //TODO: TickerID for development experiments set here:
    private final int defaulTickerID = 4;


    // Keep track of the next Order ID
    private int nextOrderID = 0;
    // The IB API Client Socket object
    private EClientSocket client = null;
    private boolean connectedToTWS = false;
    private boolean contractsAlreadySetupFlag = false;

    private double bidPrice = -5;
    private double askPrice = -5;
    private double bidSize = -5;
    private double askSize = -5;

    //OutsideTradingSoftwareAPIConnection interface implementaions:

    @Override
    public double getBidPrice() {
        return bidPrice;
    }

    @Override
    public double getAskPrice() {
        return askPrice;
    }

    @Override
    synchronized public boolean connect() {
        //return true is connection attempt and setting up contracts successful:
        if(connectToTWS()){return setUpContracts();}
        return false;
    }


    private boolean connectToTWS(){
        //check if already connected:
        if (connectedToTWS) {
            System.out.println("OutsideTradingSoftwareAPIConnection already connected to TWS!");
            return true;
        }else{
            // Create a new EClientSocket object
            client = new EClientSocket (this);

            try
            {
            // Connect to the TWS or IB Gateway application
            // Leave null for localhost
            // Port Number (should match TWS/IB Gateway configuration
            client.eConnect (null, 7496, 0);
                int connectionAttempts = 5;

                for (int i = 0; i < connectionAttempts; i++) {
                    if(client.isConnected()) {
                        connectedToTWS = true;
                        return true;
                    }
                    // Pause here for connection to complete
                    Thread.sleep(1000);
                }
            }
            catch (Exception e)
            {
                System.out.println("Error connecting to TWS!");
                return false;
            }

        }
        return false;
    }

    /**
     * Define tickerIDs of contracts here.
     * Use tickerID later to access data of contracts.
     * Currently:
     * TickerID 0 = SPY
     * TickerID 1 = DIA
     * TickerID 2 = IWM
     * TickerID 3 = QQQ
     * TickerID 4 = EUR (FOREX)
     */
    private boolean setUpContracts(){

        //check if connected to data:
        if(!connectToTWS()) return false;

        //if contracts had been previously set up, return true:
        if(contractsAlreadySetupFlag) {
            System.out.println("Contracts have already been set up!");
            return true;}

        try {
            //Procedure for setting up a new contract:
            // Create a new contract
            Contract contract = new Contract ();
            contract.m_symbol = "SPY";
            contract.m_exchange = "SMART";
            contract.m_secType = "STK";
            contract.m_currency = "USD";
            // Create a TagValue list
//            Vector<TagValue> mktDataOptions = new Vector<TagValue>();
            // Make a call to reqMktData to start off data retrieval with parameters:
            // ConID    - Connection Identifier.
            // Contract - The financial instrument we are requesting data on
            // Ticks    - Any custom tick values we are looking for (null in this case)
            // Snapshot - false give us streaming data, true gives one data snapshot
            // MarketDataOptions - tagValue list of additional options (API 9.71 and newer)

            client.reqMktData(0, contract, null, false);

            // For API Version 9.73 and higher, add one more parameter: regulatory snapshot
            // client.reqMktData(0, contract, null, false, false, mktDataOptions);

            // At this point our call is done and any market data events
            // will be returned via the tickPrice method

            //set up remaining contracts with respective TickerIDs:
            contract = new Contract();
            contract.m_symbol = "DIA";
            contract.m_exchange = "SMART";
            contract.m_secType = "STK";
            contract.m_currency = "USD";
            client.reqMktData(1, contract, null, false);

            contract = new Contract();
            contract.m_symbol = "IWM";
            contract.m_exchange = "SMART";
            contract.m_secType = "STK";
            contract.m_currency = "USD";
            client.reqMktData(2, contract, null, false);

            contract = new Contract();
            contract.m_symbol = "QQQ";
            contract.m_exchange = "SMART";
            contract.m_secType = "STK";
            contract.m_currency = "USD";
            client.reqMktData(3, contract, null, false);

            contract = new Contract();
            contract.m_symbol = "EUR";
            contract.m_exchange = "IDEALPRO";
            contract.m_secType = "CASH";
            contract.m_currency = "USD";
            client.reqMktData(4, contract, null, false);

        } catch (Exception e) {
            e.printStackTrace();
            contractsAlreadySetupFlag = false;
            return false;
        }

        //if everything successful, set the flag:
        System.out.println("Setting up contracts successful.");
        contractsAlreadySetupFlag = true;
        return true;

    }

    public void consolePrintRealTimeData(){

        if (!connectedToTWS){
            System.out.println("Not connected to live data!");
            return;
        }
        System.out.println("Bid Price: " + getBidPrice());
        System.out.println("Ask Price: " + getAskPrice());
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
        try
        {
            // Print out the current price.
            // field will provide the price type:
            // 1 = bid,  2 = ask, 4 = last
            // 6 = high, 7 = low, 9 = close

            if(tickerId == defaulTickerID){

                if(field ==1) bidPrice = price;
                if(field ==2) askPrice = price;
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR IN InteractiveBrokersAPI tickPrice method!");
            e.printStackTrace ();
        }
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {

    }

    @Override
    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {

    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {

    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {

    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {

    }

    @Override
    public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {

    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {

    }

    @Override
    public void openOrderEnd() {

    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {

    }

    @Override
    public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {

    }

    @Override
    public void updateAccountTime(String timeStamp) {

    }

    @Override
    public void accountDownloadEnd(String accountName) {

    }

    @Override
    public void nextValidId(int orderId) {
        // Return the next valid OrderID
        nextOrderID = orderId;
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {

    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {

    }

    @Override
    public void contractDetailsEnd(int reqId) {

    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {

    }

    @Override
    public void execDetailsEnd(int reqId) {

    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {

    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {

    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {

    }

    @Override
    public void managedAccounts(String accountsList) {

    }

    @Override
    public void receiveFA(int faDataType, String xml) {

    }

    @Override
    public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {

        // Display Historical data
        try
        {
            System.out.println("historicalData: " + reqId + "," + date + "," +
                    open + "," + high  + "," + low  + "," + close + "," +
                    volume);
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
    }

    @Override
    public void scannerParameters(String xml) {

    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {

    }

    @Override
    public void scannerDataEnd(int reqId) {

    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {

    }

    @Override
    public void currentTime(long time) {

    }

    @Override
    public void fundamentalData(int reqId, String data) {

    }

    @Override
    public void deltaNeutralValidation(int reqId, UnderComp underComp) {

    }

    @Override
    public void tickSnapshotEnd(int reqId) {

    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {

    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {

    }

    @Override
    public void position(String account, Contract contract, int pos, double avgCost) {

    }

    @Override
    public void positionEnd() {

    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {

    }

    @Override
    public void accountSummaryEnd(int reqId) {

    }

    @Override
    public void error(Exception e) {

    }

    @Override
    public void error(String str) {
        // Print out the error message
        System.err.println (str);

    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
        // Overloaded error event (from IB) with their own error
        // codes and messages
        System.err.println ("error: " + id + "," + errorCode + "," + errorMsg);
    }

    @Override
    public void connectionClosed() {

    }
}


class InteractiveBrokersAPITestDrive{

}