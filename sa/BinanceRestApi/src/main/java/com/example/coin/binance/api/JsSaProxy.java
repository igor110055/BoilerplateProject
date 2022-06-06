package com.example.coin.binance.api;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3AvgPrice;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3CompressedAggreateTradeList;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3ExchangeInfo;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3KlineCandlestickData;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3OldTradeLookup;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3OrderBook;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Ping;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3RecentTradeList;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3SymbolOrderBookTicker;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3SymbolPriceTicker;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24;
import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeDeleteApiV3CancelAllOPenOrderOnSymbol;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeDeleteApiV3CancelOco;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeDeleteApiV3CancelOrder;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3AccountInformation;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3AccountTradeList;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3AllOrders;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3CurrentOpenOrders;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3QueryAllOco;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3QueryOco;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3QueryOpenOco;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradeGetApiV3QueryOrder;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradePostApiV3NewOco;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradePostApiV3NewOrder;
import com.example.coin.binance.spotAccountTrade.SpotAccountTradePostApiV3TestNewOrder;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1AccountApiTradingStatus;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1AccountStatus;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1AllCoinsInformation;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1AssetDetail;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1AssetDividendRecord;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1DailyAccountSnapshotFUTURES;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1DailyAccountSnapshotMARGIN;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1DailyAccountSnapshotSPOT;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1DepositAddress;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1DepositHistory;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1DustLog;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1GetApiKeyPermission;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1QueryUserUniversalTransferHistory;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1SystemStatus;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1TradeFee;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1WithdrawHistory;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1DisableFastWithdrawSwitch;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1DustTransfer;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1EnableFastWithdrawSwitch;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1FundingWallet;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1UserUniversalTransfer;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1Withdraw;

import dataset.DataSet;
import running.common.SAProxy;

public class JsSaProxy extends SAProxy {

    public DataSet MarketDataEndPointsGetApiV3Time(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
        MarketDataEndPointsGetApiV3Time tmp = new MarketDataEndPointsGetApiV3Time();
        DataSet OUT_DS = tmp.GetApiV3Time(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3Ping(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
        MarketDataEndPointsGetApiV3Ping tmp = new MarketDataEndPointsGetApiV3Ping();
        DataSet OUT_DS = tmp.GetApiV3Ping(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3ExchangeInfo(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3ExchangeInfo tmp = new MarketDataEndPointsGetApiV3ExchangeInfo();
        DataSet OUT_DS = tmp.GetApiV3ExchangeInfo(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3OrderBook(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3OrderBook tmp = new MarketDataEndPointsGetApiV3OrderBook();
        DataSet OUT_DS = tmp.GetApiV3OrderBook(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3RecentTradeList(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3RecentTradeList tmp = new MarketDataEndPointsGetApiV3RecentTradeList();
        DataSet OUT_DS = tmp.GetApiV3RecentTradeList(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24(DataSet InDs, String InDsNames,
            String outDsNames) throws Exception {
        MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24 tmp = new MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24();
        DataSet OUT_DS = tmp.GetApiV3TickerPriceChangeStatistics24(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3OldTradeLookup(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3OldTradeLookup tmp = new MarketDataEndPointsGetApiV3OldTradeLookup();
        DataSet OUT_DS = tmp.GetApiV3OldTradeLookup(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3CompressedAggreateTradeList(DataSet InDs, String InDsNames,
            String outDsNames) throws Exception {
        MarketDataEndPointsGetApiV3CompressedAggreateTradeList tmp = new MarketDataEndPointsGetApiV3CompressedAggreateTradeList();
        DataSet OUT_DS = tmp.GetApiV3CompressedAggreateTradeList(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3KlineCandlestickData(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3KlineCandlestickData tmp = new MarketDataEndPointsGetApiV3KlineCandlestickData();
        DataSet OUT_DS = tmp.GetApiV3KlineCandlestickData(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3SymbolPriceTicker(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3SymbolPriceTicker tmp = new MarketDataEndPointsGetApiV3SymbolPriceTicker();
        DataSet OUT_DS = tmp.GetApiV3SymbolPriceTicker(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3AvgPrice(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3AvgPrice tmp = new MarketDataEndPointsGetApiV3AvgPrice();
        DataSet OUT_DS = tmp.GetApiV3AvgPrice(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet MarketDataEndPointsGetApiV3SymbolOrderBookTicker(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        MarketDataEndPointsGetApiV3SymbolOrderBookTicker tmp = new MarketDataEndPointsGetApiV3SymbolOrderBookTicker();
        DataSet OUT_DS = tmp.GetApiV3SymbolOrderBookTicker(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1AllCoinsInformation(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1AllCoinsInformation tmp = new WalletEndPointsGetSapiV1AllCoinsInformation();
        DataSet OUT_DS = tmp.GetSapiV1AllCoinsInformation(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1SystemStatus(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1SystemStatus tmp = new WalletEndPointsGetSapiV1SystemStatus();
        DataSet OUT_DS = tmp.GetSapiV1SystemStatus(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1DailyAccountSnapshotFUTURES(DataSet InDs, String InDsNames,
            String outDsNames) throws Exception {
        WalletEndPointsGetSapiV1DailyAccountSnapshotFUTURES tmp = new WalletEndPointsGetSapiV1DailyAccountSnapshotFUTURES();
        DataSet OUT_DS = tmp.GetSapiV1DailyAccountSnapshotFUTURES(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1DailyAccountSnapshotMARGIN(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1DailyAccountSnapshotMARGIN tmp = new WalletEndPointsGetSapiV1DailyAccountSnapshotMARGIN();
        DataSet OUT_DS = tmp.GetSapiV1DailyAccountSnapshotMARGIN(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1DailyAccountSnapshotSPOT(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1DailyAccountSnapshotSPOT tmp = new WalletEndPointsGetSapiV1DailyAccountSnapshotSPOT();
        DataSet OUT_DS = tmp.GetSapiV1DailyAccountSnapshotSPOT(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsPostSapiV1DisableFastWithdrawSwitch(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsPostSapiV1DisableFastWithdrawSwitch tmp = new WalletEndPointsPostSapiV1DisableFastWithdrawSwitch();
        DataSet OUT_DS = tmp.PostSapiV1DisableFastWithdrawSwitch(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsPostSapiV1EnableFastWithdrawSwitch(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsPostSapiV1EnableFastWithdrawSwitch tmp = new WalletEndPointsPostSapiV1EnableFastWithdrawSwitch();
        DataSet OUT_DS = tmp.PostSapiV1EnableFastWithdrawSwitch(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsPostSapiV1Withdraw(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsPostSapiV1Withdraw tmp = new WalletEndPointsPostSapiV1Withdraw();
        DataSet OUT_DS = tmp.PostSapiV1Withdraw(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1DepositHistory(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1DepositHistory tmp = new WalletEndPointsGetSapiV1DepositHistory();
        DataSet OUT_DS = tmp.GetSapiV1DepositHistory(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1WithdrawHistory(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1WithdrawHistory tmp = new WalletEndPointsGetSapiV1WithdrawHistory();
        DataSet OUT_DS = tmp.GetSapiV1WithdrawHistory(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1DepositAddress(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1DepositAddress tmp = new WalletEndPointsGetSapiV1DepositAddress();
        DataSet OUT_DS = tmp.GetSapiV1DepositAddress(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1AccountStatus(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1AccountStatus tmp = new WalletEndPointsGetSapiV1AccountStatus();
        DataSet OUT_DS = tmp.GetSapiV1AccountStatus(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1AccountApiTradingStatus(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1AccountApiTradingStatus tmp = new WalletEndPointsGetSapiV1AccountApiTradingStatus();
        DataSet OUT_DS = tmp.GetSapiV1AccountApiTradingStatus(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1DustLog(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
        WalletEndPointsGetSapiV1DustLog tmp = new WalletEndPointsGetSapiV1DustLog();
        DataSet OUT_DS = tmp.GetSapiV1DustLog(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsPostSapiV1DustTransfer(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsPostSapiV1DustTransfer tmp = new WalletEndPointsPostSapiV1DustTransfer();
        DataSet OUT_DS = tmp.PostSapiV1DustTransfer(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1AssetDividendRecord(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1AssetDividendRecord tmp = new WalletEndPointsGetSapiV1AssetDividendRecord();
        DataSet OUT_DS = tmp.GetSapiV1AssetDividendRecord(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1AssetDetail(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1AssetDetail tmp = new WalletEndPointsGetSapiV1AssetDetail();
        DataSet OUT_DS = tmp.GetSapiV1AssetDetail(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1TradeFee(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1TradeFee tmp = new WalletEndPointsGetSapiV1TradeFee();
        DataSet OUT_DS = tmp.GetSapiV1TradeFee(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsPostSapiV1UserUniversalTransfer(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsPostSapiV1UserUniversalTransfer tmp = new WalletEndPointsPostSapiV1UserUniversalTransfer();
        DataSet OUT_DS = tmp.PostSapiV1UserUniversalTransfer(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1QueryUserUniversalTransferHistory(DataSet InDs, String InDsNames,
            String outDsNames) throws Exception {
        WalletEndPointsGetSapiV1QueryUserUniversalTransferHistory tmp = new WalletEndPointsGetSapiV1QueryUserUniversalTransferHistory();
        DataSet OUT_DS = tmp.GetSapiV1QueryUserUniversalTransferHistory(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsPostSapiV1FundingWallet(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsPostSapiV1FundingWallet tmp = new WalletEndPointsPostSapiV1FundingWallet();
        DataSet OUT_DS = tmp.PostSapiV1FundingWallet(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet WalletEndPointsGetSapiV1GetApiKeyPermission(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        WalletEndPointsGetSapiV1GetApiKeyPermission tmp = new WalletEndPointsGetSapiV1GetApiKeyPermission();
        DataSet OUT_DS = tmp.GetSapiV1GetApiKeyPermission(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeDeleteApiV3CancelAllOPenOrderOnSymbol(DataSet InDs, String InDsNames,
            String outDsNames) throws Exception {
        SpotAccountTradeDeleteApiV3CancelAllOPenOrderOnSymbol tmp = new SpotAccountTradeDeleteApiV3CancelAllOPenOrderOnSymbol();
        DataSet OUT_DS = tmp.DeleteApiV3CancelAllOPenOrderOnSymbol(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeDeleteApiV3CancelOco(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeDeleteApiV3CancelOco tmp = new SpotAccountTradeDeleteApiV3CancelOco();
        DataSet OUT_DS = tmp.DeleteApiV3CancelOco(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeDeleteApiV3CancelOrder(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeDeleteApiV3CancelOrder tmp = new SpotAccountTradeDeleteApiV3CancelOrder();
        DataSet OUT_DS = tmp.DeleteApiV3CancelOrder(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3AccountInformation(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3AccountInformation tmp = new SpotAccountTradeGetApiV3AccountInformation();
        DataSet OUT_DS = tmp.GetApiV3AccountInformation(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3AccountTradeList(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3AccountTradeList tmp = new SpotAccountTradeGetApiV3AccountTradeList();
        DataSet OUT_DS = tmp.GetApiV3AccountTradeList(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3AllOrders(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3AllOrders tmp = new SpotAccountTradeGetApiV3AllOrders();
        DataSet OUT_DS = tmp.GetApiV3AllOrders(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3CurrentOpenOrders(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3CurrentOpenOrders tmp = new SpotAccountTradeGetApiV3CurrentOpenOrders();
        DataSet OUT_DS = tmp.GetApiV3CurrentOpenOrders(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3QueryAllOco(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3QueryAllOco tmp = new SpotAccountTradeGetApiV3QueryAllOco();
        DataSet OUT_DS = tmp.GetApiV3QueryAllOco(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3QueryOco(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3QueryOco tmp = new SpotAccountTradeGetApiV3QueryOco();
        DataSet OUT_DS = tmp.GetApiV3QueryOco(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3QueryOpenOco(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3QueryOpenOco tmp = new SpotAccountTradeGetApiV3QueryOpenOco();
        DataSet OUT_DS = tmp.GetApiV3QueryOpenOco(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradeGetApiV3QueryOrder(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradeGetApiV3QueryOrder tmp = new SpotAccountTradeGetApiV3QueryOrder();
        DataSet OUT_DS = tmp.GetApiV3QueryOrder(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradePostApiV3NewOco(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
        SpotAccountTradePostApiV3NewOco tmp = new SpotAccountTradePostApiV3NewOco();
        DataSet OUT_DS = tmp.PostApiV3NewOco(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradePostApiV3NewOrder(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradePostApiV3NewOrder tmp = new SpotAccountTradePostApiV3NewOrder();
        DataSet OUT_DS = tmp.PostApiV3NewOrder(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

    public DataSet SpotAccountTradePostApiV3TestNewOrder(DataSet InDs, String InDsNames, String outDsNames)
            throws Exception {
        SpotAccountTradePostApiV3TestNewOrder tmp = new SpotAccountTradePostApiV3TestNewOrder();
        DataSet OUT_DS = tmp.PostApiV3TestNewOrder(InDs, InDsNames, outDsNames);
        return OUT_DS;
    }

}
