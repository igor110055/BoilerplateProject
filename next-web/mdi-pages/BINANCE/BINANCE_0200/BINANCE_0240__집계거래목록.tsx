import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import {commaRendererRemoveDot} from '@/form-components/tui-grid-renderer/commaRendererRemoveDot'


import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';

import * as pjtUtil from '@/utils/pjtUtil'


interface IFormInput {
    SYMBOL:string;
    FROM_ID:string;
    START_TIME:string;
    END_TIME:string;
    LIMIT:string;
}

const defaultValues = {
    SYMBOL:"",
    FROM_ID:"",
    START_TIME:"",
    END_TIME:"",
    LIMIT:"",
};


export const BINANCE_0240__집계거래목록= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_MARKET_GET_API_V3_AggregateTradeList",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_DATA: [data]
                                        }
                                    });
        }
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Compressed/Aggregate Trades List</h3>
            GET /api/v3/aggTrades
            Get compressed, aggregate trades. Trades that fill at the time, from the same order, with the same price will have the quantity aggregated.
            <b>Weight(IP):</b>  1
            </pre>   
            <hr />
            <ul className="tal lh12">
            <li>If startTime and endTime are sent, time between startTime and endTime must be less than 1 hour.</li>
            <li>If fromId, startTime, and endTime are not sent, the most recent aggregate trades will be returned.</li>
            </ul>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL(필수)" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="FROM_ID" control={control} label="FROM_ID" sx={{width:200}}   />(id to get aggregate trades from INCLUSIVE.)    
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}   />(Timestamp in ms to get aggregate trades from INCLUSIVE.) 
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}   />(Timestamp in ms to get aggregate trades until INCLUSIVE.)
                    <FormTextFiled name="LIMIT" control={control} label="LIMIT" sx={{width:200}}   />(Default 500; max 1000.)
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'AGGREGATE_TRADE_ID',name: 'AGGREGATE_TRADE_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'PRICE',name: 'PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'QUANTITY',name: 'QUANTITY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'FIRST_TRADE_ID',name: 'FIRST_TRADE_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LAST_TRADE_ID',name: 'LAST_TRADE_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TIMESTAMP',name: 'TIMESTAMP',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'BUYER_MARKET',name: 'BUYER_MARKET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'BEST_PRICE_MATCH',name: 'BEST_PRICE_MATCH',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />
            </>
    )
}
