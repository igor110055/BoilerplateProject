import Grid,{GridHandler} from '@/form-components/Grid';
import { FormProvider, useForm } from "react-hook-form";
import {FormSelect} from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import {MenuContext} from "@/store/MenuStore";
import { OptColumn } from 'tui-grid/types/options';

interface IFormInput {
}

const defaultValues = {

};

export const BINANCE_0215__교환정보= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const childRef_grid_iceberg_parts = useRef<GridHandler>(null);
    const childRef_grid_min_notional = useRef<GridHandler>(null);
    const childRef_grid_max_num_orders = useRef<GridHandler>(null);
    const childRef_grid_market_lot_size = useRef<GridHandler>(null);
    const childRef_grid_percent_price = useRef<GridHandler>(null);
    const childRef_grid_order_types = useRef<GridHandler>(null);
    const childRef_grid_max_num_algo_orders = useRef<GridHandler>(null);
    const childRef_grid_price_filter = useRef<GridHandler>(null);
    const childRef_grid_lot_size = useRef<GridHandler>(null);

    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;


    const fnSyncHandler= async (data: IFormInput) => {
        send('BR_BINANCE_MARKET_GET_API_V3_TIME',{
            brRq 		: ''
            ,brRs 		: 'OUT_RSET,OUT_RST'
        }).then(function(data:any){
            //_this.hideProgress();
            if(data){
                console.log(data);
                messageAlert(data.OUT_RSET[0].SERVER_TIME,function()  {
                    
                });
            }
        })                      
    }

    const fnSearch= async (data: IFormInput) => {
        var param ={
            brRq : 'IN_DATA'
            ,brRs : 'OUT_RSET'
            ,IN_DATA : [data]
        }
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfo",
                                        PARAM: param
                                    });
        }
        if(childRef_grid_iceberg_parts.current){
            childRef_grid_iceberg_parts.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersIcebergParts",
                                        PARAM: param
                                    });
        }
        if(childRef_grid_min_notional.current){
            childRef_grid_min_notional.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersMinNotional",
                                        PARAM: param
                                    });
        }
        if(childRef_grid_max_num_orders.current){
            childRef_grid_max_num_orders.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersMaxNumOrders",
                                        PARAM: param
                                    });
        }
        if(childRef_grid_market_lot_size.current){
            childRef_grid_market_lot_size.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersMarketLotSize",
                                        PARAM: param
                                    });
        }
        if(childRef_grid_percent_price.current){
            childRef_grid_percent_price.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersPercentPrice",
                                        PARAM: param
                                    });
        }

        if(childRef_grid_order_types.current){
            childRef_grid_order_types.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoOrderTypes",
                                        PARAM: param
                                    });
        }

        
        if(childRef_grid_max_num_algo_orders.current){
            childRef_grid_max_num_algo_orders.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersMaxNumAlgoOrders",
                                        PARAM: param
                                    });
        }

        if(childRef_grid_price_filter.current){
            childRef_grid_price_filter.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersPriceFilter",
                                        PARAM: param
                                    });
        }
        
        if(childRef_grid_lot_size.current){
            childRef_grid_lot_size.current.loadData({
                                        BR:"BR_BINANCE_MARKET_retrieveExchangeInfoFiltersLotSize",
                                        PARAM: param
                                    });
        }
    }
    
    return (
            <>
            <pre className="tal lh12">
            Parameters:

            There are 3 possible options:

            Options	Example
            No parameter	curl -X GET "https://api.binance.com/api/v3/exchangeInfo"
            symbol	curl -X GET "https://api.binance.com/api/v3/exchangeInfo?symbol=BNBBTC"
            symbols	curl -X GET "https://api.binance.com/api/v3/exchangeInfo?symbols=%5B%22BNBBTC%22,%22BTCUSDT%22%5D" or curl -g GET 'https://api.binance.com/api/v3/exchangeInfo?symbols=["BTCUSDT","BNBBTC"]'
            If any symbol provided in either symbol or symbols do not exist, the endpoint will throw an error.

            Data Source: Memory
            </pre>        
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                <FormTextFiled name="SYMBOL" control={control} label="SYMBOL(필수)" sx={{width:200}}  rules={{ required: false }}   />
                <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
                <Button variant="contained" color="success"  onClick={handleSubmit(fnSyncHandler)}>SYNC</Button>
            </Stack>
                <table>
                <tr>
                    <td>exchange info</td>
                    <td>Iceberg Parts</td>
                    <td>Min Notional</td>
                </tr>
                <tr>
                    <td>
                        <Grid style={{ height: 600, width: '100%' }} columns={ 
                            [ 
                                {header: 'STATUS',name: 'STATUS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'SYMBOL',name: 'SYMBOL',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'BASE_ASSET',name: 'BASE_ASSET',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                                ,{header: 'QUOTE_ASSET',name: 'QUOTE_ASSET',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'BASE_ASSET_PRECISION',name: 'BASE_ASSET_PRECISION',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'BASE_COMMISSION_PRECISION',name: 'BASE_COMMISSION_PRECISION',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'ICEBERG_ALLOWED',name: 'ICEBERG_ALLOWED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'IS_MARGIN_TRADING_ALLOWED',name: 'IS_MARGIN_TRADING_ALLOWED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'IS_SPOT_TRADING_ALLOWED',name: 'IS_SPOT_TRADING_ALLOWED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'OCO_ALLOWED',name: 'OCO_ALLOWED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'QUOTE_ORDER_QTY_MARKET_ALLOWED',name: 'QUOTE_ORDER_QTY_MARKET_ALLOWED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'QUOTE_ASSET_PRECISION',name: 'QUOTE_ASSET_PRECISION',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'QUOTE_COMMISSION_PRECISION',name: 'QUOTE_COMMISSION_PRECISION',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'QUOTE_PRECISION',name: 'QUOTE_PRECISION',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 200,resizable : false,sortable : true,align : "center"}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef} />
                    </td>
                    <td>
                        <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'LIMIT',name: 'LIMIT',width: 40,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 140,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}	}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_iceberg_parts} />
                    </td>
                    <td>
                        <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MIN_NOTIONAL',name: 'MIN_NOTIONAL',width: 120,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'APPLY_TO_MARKET',name: 'APPLY_TO_MARKET',width: 120,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'AVG_PRICE_MINS',name: 'AVG_PRICE_MINS',width: 120,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 140,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}	}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_min_notional} />                    
                    </td>
                </tr>
                </table>

                <table>
                <tr>
                    <td>Max Num Orders</td>
                    <td>Market Lot Size</td>
                    <td>Percent Price</td>
                </tr>
                <tr>
                    <td>
                        <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MAX_NUM_ORDERS',name: 'MAX_NUM_ORDERS',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 200,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_max_num_orders} />
                    </td>
                    <td>
                        <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 80,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MAX_QTY',name: 'MAX_QTY',width: 100,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MIN_QTY',name: 'MIN_QTY',width: 120,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'STEP_SIZE',name: 'STEP_SIZE',width: 120,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 200,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_market_lot_size} />                    
                    </td>
                    <td>
                    <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MULTIPLIER_UP',name: 'MULTIPLIER_UP',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MULTIPLIER_DOWN',name: 'MULTIPLIER_DOWN',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'AVG_PRICE_MINS',name: 'AVG_PRICE_MINS',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 200,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_percent_price} />
                    </td>    
                </tr>
                <tr>
                    <td>order Types</td>
                    <td>Market Num Algo Orders</td>
                    <td>Price Filter</td>
                </tr>
                <tr>
                    <td>
                    <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'ORDER_TYPE',name: 'ORDER_TYPE',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 140,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_order_types} />
                    </td>
                    <td>
                    <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 80,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MAX_NUM_ALGO_ORDERS',name: 'MAX_NUM_ALGO_ORDERS',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 120,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_max_num_algo_orders} />
                    </td>
                    <td>
                    <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MAX_PRICE',name: 'MAX_PRICE',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MIN_PRICE',name: 'MIN_PRICE',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'TICK_SIZE',name: 'TICK_SIZE',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 200,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_price_filter} />
                    </td>    
                </tr>
                <tr>
                    <td>Lot Size</td>
                    <td>123</td>
                    <td>123</td>
                </tr>
                <tr>
                    <td>
                    <Grid style={{ height: 600, width: '100%' }} columns={
                            [
                                {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MAX_QTY',name: 'MAX_QTY',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'MIN_QTY',name: 'MIN_QTY',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header: 'STEP_SIZE',name: 'STEP_SIZE',width: 120,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                                ,{header : '생성일',name : 'CRT_DTM',width : 140,resizable : false,sortable : true,align : "center",filter : {type : 'date',format : 'yyyy-MM-DD'}}
                            ]
                        }  checkbox={true} showRowStatus={true} ref={childRef_grid_lot_size} />
                    </td>    
                    <td>123</td>
                    <td>123</td>
                </tr>
                </table>
            </>
    )
  }
