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

export const BINANCE_0345__OCO신규주문TRADE= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const childRef_grid_orders = useRef<TuiGridHandler>(null);
    const childRef_grid_order_reports = useRef<TuiGridHandler>(null);
    
    


    const [timeInForce,setTimeInForce] = useState([]);
    const [side,setSide] = useState([]);

    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'BINANCE_TIME_IN_FORCE_CD',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setTimeInForce(tmp)

            
            const data2:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'BINANCE_SIDE_CD',  USE_YN: 'Y'}]
            });

            const tmp2= data2.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setSide(tmp2)
        }
        getData();

    },[]);
    
    

    const fnNewOcoSendHandler= async (data: IFormInput) => {
        
        send("BR_SPOT_ACCOUNT_TRADE_GET_API_V3_AllOrders", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET,OUT_RSET_ORDERS,OUT_RSET_ORDER_REPORTS'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }

            if(childRef_grid_orders.current){
                childRef_grid_orders.current.setData(data.OUT_RSET_ORDERS);
            }
            if(childRef_grid_order_reports.current){
                childRef_grid_order_reports.current.setData(data.OUT_RSET_ORDER_REPORTS);
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
                    <FormTextFiled name="LIST_CLIENT_ORDER_ID" control={control} label="LIST_CLIENT_ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />

                    <FormSelect name="SIDE" control={control} label="SIDE(ENUM)" sx={{width:200}}   options={side}  />

                    <FormTextFiled name="QUANTITY" control={control} label="QUANTITY" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="LIMIT_CLIENT_ORDER_ID" control={control} label="LIMIT_CLIENT_ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="PRICE" control={control} label="PRICE" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="LIMIT_ICEBERG_QTY" control={control} label="LIMIT_ICEBERG_QTY" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="STOP_CLIENT_ORDER_ID" control={control} label="STOP_CLIENT_ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />


                    <FormTextFiled name="STOP_PRICE" control={control} label="STOP_PRICE" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="STOP_LIMIT_PRICE" control={control} label="STOP_LIMIT_PRICE" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="STOP_ICEBERG_QTY" control={control} label="STOP_ICEBERG_QTY" sx={{width:200}}  rules={{ required: false }}   />
                                        
                    <FormSelect name="STOP_LIMIT_TIME_IN_FORCE" control={control} label="STOP_LIMIT_TIME_IN_FORCE" sx={{width:200}}   options={timeInForce}  />

                    <Button variant="contained" color="success"   onClick={handleSubmit(fnNewOcoSendHandler)}>OCO신규주문</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CONTINGENCY_TYPE',name: 'CONTINGENCY_TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LIST_STATUS_TYPE',name: 'LIST_STATUS_TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LIST_ORDER_STATUS',name: 'LIST_ORDER_STATUS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LIST_CLIENT_ORDER_ID',name: 'LIST_CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TRANSACTION_TIME',name: 'TRANSACTION_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />

            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_ID',name: 'ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CLIENT_ORDER_ID',name: 'CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef_grid_orders} />

            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_ID',name: 'ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CLIENT_ORDER_ID',name: 'CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TRANSACT_TIME',name: 'TRANSACT_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'PRICE',name: 'PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORIG_QTY',name: 'ORIG_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'EXECUTED_QTY',name: 'EXECUTED_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CUMMULATIVE_QUOTE_QTY',name: 'CUMMULATIVE_QUOTE_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'STATUS',name: 'STATUS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TIME_IN_FORCE',name: 'TIME_IN_FORCE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TYPE',name: 'TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'SIDE',name: 'SIDE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'STOP_PRICE',name: 'STOP_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef_grid_order_reports} />
            </>
    )
}
