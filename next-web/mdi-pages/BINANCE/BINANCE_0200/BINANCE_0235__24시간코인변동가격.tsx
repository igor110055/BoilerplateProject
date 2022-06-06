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
}

const defaultValues = {
};

export const BINANCE_0235__24시간코인변동가격= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_MARKET_GET_API_V3_TICKER_PRICE_CHANGES_STATISTICS24",
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
            <h3>24hr Ticker Price Change Statistics</h3>
            GET /api/v3/ticker/24hr
            24 hour rolling window price change statistics. Careful when accessing this with no symbol.
            <b>Weight(IP):</b> 
            1 for a single symbol;
            40 when the symbol parameter is omitted;
            </pre>   
            <hr />
            <pre className="tal lh12">
            If the symbol is not sent, tickers for all symbols will be returned in an array.
            </pre>  
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'PRICE_CHANGE',name: 'PRICE_CHANGE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'WEIGHTED_AVG_PRICE',name: 'WEIGHTED_AVG_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'PREV_CLOSE_PRICE',name: 'PREV_CLOSE_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'LAST_PRICE',name: 'LAST_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'LAST_QTY',name: 'LAST_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'BID_PRICE',name: 'BID_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'ASK_PRICE',name: 'ASK_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'OPEN_PRICE',name: 'OPEN_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'HIGH_PRICE',name: 'HIGH_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'LOW_PRICE',name: 'LOW_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'VOLUME',name: 'VOLUME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'QUOTE_VOLUME',name: 'QUOTE_VOLUME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'OPEN_TIME',name: 'OPEN_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'CLOSE_TIME',name: 'CLOSE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'FIRST_ID',name: 'FIRST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'LAST_ID',name: 'LAST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'COUNT',name: 'COUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                        ]
                        }   ref={childRef} />
            </>
    )
}
