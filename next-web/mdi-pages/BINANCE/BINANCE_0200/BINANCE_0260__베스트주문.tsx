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
}

const defaultValues = {
    SYMBOL:"",
};


export const BINANCE_0260__베스트주문= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_MARKET_GET_API_V3_SymbolOrderBookTicker",
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
            <h3>Symbol Order Book Ticker</h3>
            GET /api/v3/ticker/bookTicker
            Best price/qty on the order book for a symbol or symbols.
            <b>Weight(IP):</b> 
            1 for a single symbol;
            2 when the symbol parameter is omitted;
            </pre>   

            <hr />
            <pre className="tal lh12">
            If the symbol is not sent, bookTickers for all symbols will be returned in an array.
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'BID_PRICE',name: 'BID_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'BID_QTY',name: 'BID_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ASK_PRICE',name: 'ASK_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ASK_QTY',name: 'ASK_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />
            </>
    )
}
