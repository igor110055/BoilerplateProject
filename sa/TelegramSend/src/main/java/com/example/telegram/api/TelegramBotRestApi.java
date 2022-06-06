package com.example.telegram.api;

import com.example.telegram.TelegramBotSendToMe;

import dataset.DataSet;
import running.common.SAProxy;

public class TelegramBotRestApi extends SAProxy {
    public DataSet TelegramBotSendToMe(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	TelegramBotSendToMe tmp = new TelegramBotSendToMe();
    	DataSet OUT_DS =tmp.BotSendToMe(InDs,InDsNames, outDsNames);
        return OUT_DS;
    } 
}
