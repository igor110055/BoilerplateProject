import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';


interface IFormInput {
    COIN:string;
    WITHDRAW_ORDER_ID:string;
}

const defaultValues = {
    COIN:"",
    WITHDRAW_ORDER_ID:"",
};




export const FX_0190__바이낸스출금= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
        
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_BINANCE_WITHDRAW_retrieveBinanceWithdrawLog",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }


      const columns:OptColumn[] = [ 
        {header : 'COIN',name : 'COIN',width : 100,sortable : true,align : "center"}
        ,{header : 'WITHDRAW_ORDER_ID',name : 'WITHDRAW_ORDER_ID',width : 140,resizable : false}
        ,{header : 'NETWORK',name : 'NETWORK',width : 80,resizable : false,align : "center"}
        ,{header : 'ADDRESS',name : 'ADDRESS',width : 240,align : "center"}
        ,{header : 'ADDRESS_TAG',name : 'ADDRESS_TAG',width : 100,resizable : false,align : "right"}
        ,{header : 'AMOUNT',name : 'AMOUNT',width : 80,resizable : false,align : "right"}
        ,{header : 'TRANSACTION_FEE_FLAG',name : 'TRANSACTION_FEE_FLAG',width : 160,resizable : false,align : "center"}
        ,{header : 'NAME',name : 'NAME',width : 200,resizable : false,align : "right"}
        ,{header : 'RESULT_ID',name : 'RESULT_ID',width : 240,resizable : false,align : "center"}
        ,{header : '생성일',name : 'CRT_DTM',width : 140,sortable : true,align : "center"} 
    ];

    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="COIN" control={control} label="COIN" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="WITHDRAW_ORDER_ID" control={control} label="WITHDRAW_ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
function messageAlert(arg0: string) {
    throw new Error('Function not implemented.');
}

