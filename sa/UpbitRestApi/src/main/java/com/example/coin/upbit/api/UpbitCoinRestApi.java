package com.example.coin.upbit.api;

import java.util.HashMap;
import java.util.Map;

import com.example.coin.upbit.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;
public class UpbitCoinRestApi extends SAProxy {
    public DataSet ExchangeDeleteOrder(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeDeleteOrder tmp = new ExchangeDeleteOrder();
    	DataSet OUT_DS =tmp.DeleteOrder(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeGetAccount(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetAccount tmp = new ExchangeGetAccount();
    	DataSet OUT_DS =tmp.GetAccount(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeGetApiKeys(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetApiKeys tmp = new ExchangeGetApiKeys();
    	DataSet OUT_DS =tmp.GetApiKeys(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeGetDeposit(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetDeposit tmp = new ExchangeGetDeposit();
    	DataSet OUT_DS =tmp.GetDeposit(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }


    public DataSet ExchangeGetDeposits(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetDeposits tmp = new ExchangeGetDeposits();
    	DataSet OUT_DS =tmp.GetDeposits(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeGetDepositsCoinAddress(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetDepositsCoinAddress tmp = new ExchangeGetDepositsCoinAddress();
    	DataSet OUT_DS =tmp.GetDepositsCoinAddress(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeGetDepositsCoinAddresses(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetDepositsCoinAddresses tmp = new ExchangeGetDepositsCoinAddresses();
    	DataSet OUT_DS =tmp.GetDepositsCoinAddresses(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeGetOrder(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetOrder tmp = new ExchangeGetOrder();
    	DataSet OUT_DS =tmp.GetOrder(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet ExchangeGetOrderChance(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetOrderChance tmp = new ExchangeGetOrderChance();
    	DataSet OUT_DS =tmp.GetOrderChance(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    
    
    public DataSet ExchangeGetOrders(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetOrders tmp = new ExchangeGetOrders();
    	DataSet OUT_DS =tmp.GetOrders(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet ExchangeGetStatusWallet(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetStatusWallet tmp = new ExchangeGetStatusWallet();
    	DataSet OUT_DS =tmp.GetStatusWallet(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet ExchangeGetWithdraw(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetWithdraw tmp = new ExchangeGetWithdraw();
    	DataSet OUT_DS =tmp.GetWithdraw(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet ExchangeGetWithdraws(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetWithdraws tmp = new ExchangeGetWithdraws();
    	DataSet OUT_DS =tmp.GetWithdraws(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet ExchangeGetWithdrawsChance(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangeGetWithdrawsChance tmp = new ExchangeGetWithdrawsChance();
    	DataSet OUT_DS =tmp.GetWithdrawsChance(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    public DataSet ExchangePostDepositsGenerateCoinAddress(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangePostDepositsGenerateCoinAddress tmp = new ExchangePostDepositsGenerateCoinAddress();
    	DataSet OUT_DS =tmp.PostDepositsGenerateCoinAddress(InDs,InDsNames,outDsNames);
        return OUT_DS;
    } 
    
    public DataSet ExchangePostDepositsKrw(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangePostDepositsKrw tmp = new ExchangePostDepositsKrw();
    	DataSet OUT_DS =tmp.PostDepositsKrw(InDs,InDsNames,outDsNames);
        return OUT_DS;
    } 
    
    public DataSet ExchangePostOrders(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangePostOrders tmp = new ExchangePostOrders();
    	DataSet OUT_DS =tmp.PostOrders(InDs,InDsNames,outDsNames);
        return OUT_DS;
    } 
    
    public DataSet ExchangePostWithdrawsCoin(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangePostWithdrawsCoin tmp = new ExchangePostWithdrawsCoin();
    	DataSet OUT_DS =tmp.PostWithdrawsCoin(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet ExchangePostWithdrawsKrw(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	ExchangePostWithdrawsKrw tmp = new ExchangePostWithdrawsKrw();
    	DataSet OUT_DS =tmp.PostWithdrawsKrw(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet QuotationGetCandlesDays(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetCandlesDays tmp = new QuotationGetCandlesDays();
    	DataSet OUT_DS =tmp.GetCandlesDays(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet QuotationGetCandlesMinutes(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetCandlesMinutes tmp = new QuotationGetCandlesMinutes();
    	DataSet OUT_DS =tmp.GetCandlesMinutes(InDs,InDsNames,outDsNames);
        return OUT_DS;
    } 
    
    public DataSet QuotationGetCandlesMonths(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetCandlesMonths tmp = new QuotationGetCandlesMonths();
    	DataSet OUT_DS =tmp.GetCandlesMonths(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet QuotationGetCandlesWeeks(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetCandlesWeeks tmp = new QuotationGetCandlesWeeks();
    	DataSet OUT_DS =tmp.GetCandlesWeeks(InDs,InDsNames,outDsNames);
        return OUT_DS;
    } 
    
    public DataSet QuotationGetMarketAll(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetMarketAll tmp = new QuotationGetMarketAll();
    	DataSet OUT_DS =tmp.GetMarketAll(InDs,InDsNames,outDsNames);
        return OUT_DS;
    } 
    
    public DataSet QuotationGetMarketTicker(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetMarketTicker tmp = new QuotationGetMarketTicker();
    	DataSet OUT_DS =tmp.GetMarketTicker(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet QuotationGetOrderBook(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetOrderBook tmp = new QuotationGetOrderBook();
    	DataSet OUT_DS =tmp.GetOrderBook(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    
    public DataSet QuotationGetTradesTicks(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	QuotationGetTradesTicks tmp = new QuotationGetTradesTicks();
    	DataSet OUT_DS =tmp.GetTradesTicks(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }

    public DataSet ExchangeRateFrxKrwUsd(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	com.example.coin.hanabank.ExchangeRateFrxKrwUsd tmp = new com.example.coin.hanabank.ExchangeRateFrxKrwUsd();
    	DataSet OUT_DS =tmp.GetFrxKrwUsd(InDs,InDsNames,outDsNames);
        return OUT_DS;
    }
    

    

    

}
