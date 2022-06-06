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
    ORDER_ID:string;
    START_TIME:string;
    END_TIME:string;
    LIMIT:string;
}

const defaultValues = {
    SYMBOL:"",
    ORDER_ID:"",
    START_TIME:"",
    END_TIME:"",
    LIMIT:"",
};

export const BINANCE_0340__모든주문USER_DATA= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);

    useEffect(()=>{

    },[]);
    
    

    const fnSearchHandler= async (data: IFormInput) => {
        
        send("BR_SPOT_ACCOUNT_TRADE_GET_API_V3_AllOrders", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)

        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>All Orders (USER_DATA)</h3>
            GET /api/v3/allOrders (HMAC SHA256)
            Get all account orders; active, canceled, or filled.
            <b>Weight(IP):</b> 10 with symbol
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="ORDER_ID" control={control} label="ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="LIMIT" control={control} label="LIMIT" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearchHandler)}>모든계좌 조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_ID',name: 'ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CLIENT_ORDER_ID',name: 'CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'PRICE',name: 'PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORIG_QTY',name: 'ORIG_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'EXECUTED_QTY',name: 'EXECUTED_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CUMMULATIVE_QUOTE_QTY',name: 'CUMMULATIVE_QUOTE_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'STATUS',name: 'STATUS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TIME_IN_FORCE',name: 'TIME_IN_FORCE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TYPE',name: 'TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'SIDE',name: 'SIDE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'STOP_PRICE',name: 'STOP_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ICEBERG_QTY',name: 'ICEBERG_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TIME',name: 'TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'UPDATE_TIME',name: 'UPDATE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'IS_WORKING',name: 'IS_WORKING',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORIG_QUOTE_ORDER_QTY',name: 'ORIG_QUOTE_ORDER_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />
            </>
    )
}
