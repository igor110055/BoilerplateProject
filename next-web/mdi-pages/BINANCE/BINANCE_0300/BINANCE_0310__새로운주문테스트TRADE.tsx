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
    SIDE:string;
    TYPE:string;
    TIME_IN_FORCE:string;
    QUANTITY:string;
    QUOTE_ORDER_QTY:string;
    PRICE:string;
    NEW_CLIENT_ORDER_ID:string;
    STOP_PRICE:string;
    ICEBERG_QTY:string;
}

const defaultValues = {
    SYMBOL:"",
    SIDE:"",
    TYPE:"",
    TIME_IN_FORCE:"",
    QUANTITY:"",
    QUOTE_ORDER_QTY:"",
    PRICE:"",
    NEW_CLIENT_ORDER_ID:"",
    STOP_PRICE:"",
    ICEBERG_QTY:""
};

export const BINANCE_0310__새로운주문테스트TRADE= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const childRef_fills = useRef<TuiGridHandler>(null);
    const [side,setSide] = useState([]);
    const [type,setType] = useState([]);
    const [timeInForce,setTimeInForce] = useState([]);


    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'BINANCE_TYPE_CD',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setType(tmp)

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

            
            const data3:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'BINANCE_TIME_IN_FORCE_CD',  USE_YN: 'Y'}]
            });

            const tmp3= data3.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setTimeInForce(tmp3)
        }
        getData();        
    },[]);
    
    

    const fnCreateHandler= async (data: IFormInput) => {
        
        send("BR_SPOT_ACCOUNT_TRADE_POST_API_V3_TestNewOrder", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET,OUT_RSET_FILLS'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }
            if(childRef_fills.current){
                childRef_fills.current.setData(data.OUT_RSET_FILLS);
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)

        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Test New Order (TRADE)</h3>
            POST /api/v3/order/test (HMAC SHA256)
            Test new order creation and signature/recvWindow long. Creates and validates a new order but does not send it into the matching engine.
            <b>Weight:</b> 1
            <b>Parameters:</b>
            Same as POST /api/v3/order
            <b>Data Source:</b> Memory
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL" sx={{width:200}}  rules={{ required: false }}   />

                    <FormSelect name="SIDE" control={control} label="SIDE(ENUM)" sx={{width:200}}   options={side}  />
                    <FormSelect name="TYPE" control={control} label="TYPE(ENUM)" sx={{width:200}}   options={type}  />
                    <FormSelect name="TIME_IN_FORCE" control={control} label="TIME_IN_FORCE(ENUM)" sx={{width:200}}   options={timeInForce}  />

                    <FormTextFiled name="QUANTITY" control={control} label="QUANTITY" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="QUOTE_ORDER_QTY" control={control} label="QUOTE_ORDER_QTY" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="PRICE" control={control} label="PRICE" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="NEW_CLIENT_ORDER_ID" control={control} label="NEW_CLIENT_ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="STOP_PRICE" control={control} label="STOP_PRICE" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="ICEBERG_QTY" control={control} label="ICEBERG_QTY" sx={{width:200}}  rules={{ required: false }}   />

                    <Button variant="contained" color="success"   onClick={handleSubmit(fnCreateHandler)}>테스트-신규주문</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'ORDER_ID',name: 'ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'CLIENT_ORDER_ID',name: 'CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'TRANSACT_TIME',name: 'TRANSACT_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'PRICE',name: 'PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'ORIG_QTY',name: 'ORIG_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'EXECUTED_QTY',name: 'EXECUTED_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'CUMMULATIVE_QUOTE_QTY',name: 'CUMMULATIVE_QUOTE_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'STATUS',name: 'STATUS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'TIME_IN_FORCE',name: 'TIME_IN_FORCE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'TYPE',name: 'TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                            ,{header: 'SIDE',name: 'SIDE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
                        ]
                        }   ref={childRef} />

            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'PRICE',name: 'PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'QTY',name: 'QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'COMMISSION',name: 'COMMISSION',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'COMMISSION_ASSET',name: 'COMMISSION_ASSET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef_fills} />                        
            </>
    )
}
